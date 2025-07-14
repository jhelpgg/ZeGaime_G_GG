package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Database
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDatabaseObjectDSL
import fr.khelp.zegaime.database.condition.AND
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.condition.EQUALS
import fr.khelp.zegaime.database.condition.EQUALS_ENUM
import fr.khelp.zegaime.database.condition.NEVER_MATCH_CONDITION
import fr.khelp.zegaime.database.query.WhereDatabaseObject
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.utils.extensions.transformInt
import fr.khelp.zegaime.utils.tasks.delay
import fr.khelp.zegaime.utils.tasks.future.Future
import java.util.Calendar

/**
 * Represents an object stored in the database.
 *
 * This is an abstract class that should be extended by any class that needs to be stored in the database.
 * The fields of the class will be automatically mapped to the columns of the table.
 *
 * **Creation example:**
 * ```kotlin
 * @TableName("users")
 * class User(database: Database, val name: String, val age: Int) : DatabaseObject(database)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val user = User(database, "John", 30).waitCreated()
 * val id = user.databaseID
 * ```
 *
 * @property database The database instance.
 * @property databaseID The ID of the object in the database. It is -1 if the object is not yet stored.
 */
abstract class DatabaseObject(internal val database : Database)
{
    companion object
    {
        /**
         * Returns the table associated with the given class.
         *
         * **Usage example:**
         * ```kotlin
         * val table = DatabaseObject.table(database, User::class.java)
         * ```
         *
         * @param database The database instance.
         * @param clazz The class of the database object.
         * @return The table associated with the given class.
         */
        fun table(database : Database, clazz : Class<out DatabaseObject>) : Table =
            DataObjectManager.tableDescription(database, clazz).table

        /**
         * Selects objects from the database.
         *
         * **Usage example:**
         * ```kotlin
         * val users = DatabaseObject.select<User>(database) {
         *     where { "age" GREATER_THAN 18 }
         * }
         * ```
         *
         * @param DO The type of the database object to select.
         * @param database The database instance.
         * @param whereCreator A lambda function to define the where clause.
         * @return A result set of the selected objects.
         */
        @WhereDatabaseObjectDSL
        inline fun <reified DO : DatabaseObject> select(database : Database,
                                                        whereCreator : WhereDatabaseObject<DO>.() -> Unit) : DatabaseObjectResult<DO>
        {
            val table = DatabaseObject.table(database, DO::class.java)
            val selectDatabaseObject = WhereDatabaseObject<DO>(table)
            whereCreator(selectDatabaseObject)
            val result =
                selectDatabaseObject.condition?.let { cond -> table.select { where { condition = cond } } }
                ?: table.select {}
            return DatabaseObjectResult(database, DO::class, result)
        }

        /**
         * Deletes objects from the database.
         *
         * **Usage example:**
         * ```kotlin
         * val deletedRows = DatabaseObject.delete<User>(database) {
         *     where { User::age LESS_THAN 18 }
         * }
         * ```
         *
         * @param DO The type of the database object to delete.
         * @param database The database instance.
         * @param deleteCreator A lambda function to define the where clause.
         * @return The number of deleted rows.
         */
        @WhereDatabaseObjectDSL
        inline fun <reified DO : DatabaseObject> delete(database : Database,
                                                        deleteCreator : WhereDatabaseObject<DO>.() -> Unit) : Int
        {
            val table = DatabaseObject.table(database, DO::class.java)
            val deleteDatabaseObject = WhereDatabaseObject<DO>(table)
            deleteCreator(deleteDatabaseObject)
            return table.delete { where { condition = deleteDatabaseObject.condition } }
        }

        /**
         * Deletes a database object from the database.
         *
         * **Usage example:**
         * ```kotlin
         * val deleted = DatabaseObject.delete(user)
         * ```
         *
         * @param databaseObject The database object to delete.
         * @return `true` if the object was deleted successfully, `false` otherwise.
         */
        fun <DO : DatabaseObject> delete(databaseObject : DO) : Boolean
        {
            val table = DatabaseObject.table(databaseObject.database, databaseObject.javaClass)
            val done = table.delete { where { condition = "ID" EQUALS_ID databaseObject.databaseID } } > 0
            databaseObject.databaseID = -1
            return done
        }
    }

    var databaseID : Int = -1
        private set
    private val futureResult : Future<Unit>

    init
    {
        this.futureResult = delay(16) { this.update(true) }
    }

    /**
     * Waits for the object to be created in the database.
     *
     * This method should be called after creating a new database object to ensure that it is stored in the database
     * before being used.
     *
     * **Usage example:**
     * ```kotlin
     * val user = User(database, "John", 30).waitCreated()
     * ```
     *
     * @return The database object itself.
     */
    fun <DO : DatabaseObject> waitCreated() : DO
    {
        this.futureResult.waitForCompletion()
        ("UNCHECKED_CAST")
        return this as DO
    }

    /**
     * Updates the object in the database.
     *
     * This method is called automatically when a field of the object is modified.
     * It can also be called manually to force an update.
     *
     * **Usage example:**
     * ```kotlin
     * user.age = 31
     * user.update()
     * ```
     */
    protected fun update()
    {
        this.update(false)
    }

    private fun update(checkID : Boolean)
    {
        val tableDescription = DataObjectManager.tableDescription(this)

        if (this.databaseID >= 0)
        {
            val result = tableDescription.table.select {
                +"ID"
                where { condition = "ID" EQUALS_ID databaseID }
            }

            if (!result.hasNext)
            {
                this.databaseID = -1
            }

            result.close()
        }

        val clazz = this::class.java
        val table = tableDescription.table

        if (this.databaseID < 0)
        {
            val primaryKeys = tableDescription.primaryKeys

            if (primaryKeys.isEmpty())
            {
                this.databaseID = table.insert {
                    for (field in clazz.declaredFields)
                    {
                        field.isAccessible = true
                        val type = field.type

                        when
                        {
                            type.isArray && DatabaseObject::class.java.isAssignableFrom(type.componentType) ->
                            {
                                val array = (field[this@DatabaseObject] as Array<DatabaseObject>)
                                field.name IS array.transformInt { databaseObject ->
                                    databaseObject.update()
                                    databaseObject.databaseID
                                }
                            }

                            DatabaseObject::class.java.isAssignableFrom(type)                               ->
                            {
                                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                                dataObject.update()
                                field.name IS dataObject.databaseID
                            }

                            type == Boolean::class.java                                                     ->
                                field.name IS field.getBoolean(this@DatabaseObject)

                            type == Byte::class.java                                                        ->
                                field.name IS field.getByte(this@DatabaseObject)

                            type == Short::class.java                                                       ->
                                field.name IS field.getShort(this@DatabaseObject)

                            type == Int::class.java                                                         ->
                                field.name IS field.getInt(this@DatabaseObject)

                            type == Long::class.java                                                        ->
                                field.name IS field.getLong(this@DatabaseObject)

                            type == Float::class.java                                                       ->
                                field.name IS field.getFloat(this@DatabaseObject)

                            type == Double::class.java                                                      ->
                                field.name IS field.getDouble(this@DatabaseObject)

                            type == String::class.java                                                      ->
                                field.name IS field.get(this@DatabaseObject) as String

                            type == ByteArray::class.java                                                   ->
                                field.name IS field.get(this@DatabaseObject) as ByteArray

                            type == IntArray::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as IntArray

                            type == Calendar::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as Calendar

                            type == DataTime::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as DataTime

                            type == DataDate::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as DataDate

                            type.isEnum                                                                     ->
                                field.name IS_ENUM field.get(this@DatabaseObject)
                        }
                    }
                }
            }
            else
            {
                this.databaseID = table.insert {
                    for (field in clazz.declaredFields)
                    {
                        field.isAccessible = true
                        val type = field.type

                        when
                        {
                            type.isArray && DatabaseObject::class.java.isAssignableFrom(type.componentType) ->
                            {
                                val array = (field[this@DatabaseObject] as Array<DatabaseObject>)
                                field.name IS array.transformInt { databaseObject ->
                                    databaseObject.update()
                                    databaseObject.databaseID
                                }
                            }

                            DatabaseObject::class.java.isAssignableFrom(type)                               ->
                            {
                                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                                dataObject.update()
                                field.name IS dataObject.databaseID
                            }

                            type == Boolean::class.java                                                     ->
                                field.name IS field.getBoolean(this@DatabaseObject)

                            type == Byte::class.java                                                        ->
                                field.name IS field.getByte(this@DatabaseObject)

                            type == Short::class.java                                                       ->
                                field.name IS field.getShort(this@DatabaseObject)

                            type == Int::class.java                                                         ->
                                field.name IS field.getInt(this@DatabaseObject)

                            type == Long::class.java                                                        ->
                                field.name IS field.getLong(this@DatabaseObject)

                            type == Float::class.java                                                       ->
                                field.name IS field.getFloat(this@DatabaseObject)

                            type == Double::class.java                                                      ->
                                field.name IS field.getDouble(this@DatabaseObject)

                            type == String::class.java                                                      ->
                                field.name IS field.get(this@DatabaseObject) as String

                            type == ByteArray::class.java                                                   ->
                                field.name IS field.get(this@DatabaseObject) as ByteArray

                            type == IntArray::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as IntArray

                            type == Calendar::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as Calendar

                            type == DataTime::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as DataTime

                            type == DataDate::class.java                                                    ->
                                field.name IS field.get(this@DatabaseObject) as DataDate

                            type.isEnum                                                                     ->
                                field.name IS_ENUM field.get(this@DatabaseObject)
                        }
                    }
                    updateIfExactlyOneRowMatch {
                        var cond = createConditionPrimaryKey(clazz, table.getColumn(primaryKeys[0]), primaryKeys[0])

                        for (index in 1 until primaryKeys.size)
                        {
                            cond = cond AND createConditionPrimaryKey(clazz,
                                                                      table.getColumn(primaryKeys[index]),
                                                                      primaryKeys[index])
                        }

                        condition = cond
                    }
                }
            }
        }
        else
        {
            this.databaseID = table.insert {
                for (field in clazz.declaredFields)
                {
                    field.isAccessible = true
                    val type = field.type

                    when
                    {
                        DatabaseObject::class.java.isAssignableFrom(type) ->
                        {
                            val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                            dataObject.update()
                            field.name IS dataObject.databaseID
                        }

                        type == Boolean::class.java                       ->
                            field.name IS field.getBoolean(this@DatabaseObject)

                        type == Byte::class.java                          ->
                            field.name IS field.getByte(this@DatabaseObject)

                        type == Short::class.java                         ->
                            field.name IS field.getShort(this@DatabaseObject)

                        type == Int::class.java                           ->
                            field.name IS field.getInt(this@DatabaseObject)

                        type == Long::class.java                          ->
                            field.name IS field.getLong(this@DatabaseObject)

                        type == Float::class.java                         ->
                            field.name IS field.getFloat(this@DatabaseObject)

                        type == Double::class.java                        ->
                            field.name IS field.getDouble(this@DatabaseObject)

                        type == String::class.java                        ->
                            field.name IS field.get(this@DatabaseObject) as String

                        type == ByteArray::class.java                     ->
                            field.name IS field.get(this@DatabaseObject) as ByteArray

                        type == IntArray::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as IntArray

                        type == Calendar::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as Calendar

                        type == DataTime::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as DataTime

                        type == DataDate::class.java                      ->
                            field.name IS field.get(this@DatabaseObject) as DataDate

                        type.isEnum                                       ->
                            field.name IS_ENUM field.get(this@DatabaseObject)
                    }
                }
                updateIfExactlyOneRowMatch {
                    condition = "ID" EQUALS_ID databaseID
                }
            }
        }

        if (checkID)
        {
            this.database.checkIdForeignKey()
        }
    }

    private fun createConditionPrimaryKey(clazz : Class<out DatabaseObject>,
                                          column : Column,
                                          primaryKey : String) : Condition
    {
        val field = clazz.getDeclaredField(primaryKey)
        field.isAccessible = true
        val type = field.type

        return when
        {
            DatabaseObject::class.java.isAssignableFrom(type) ->
            {
                val dataObject = (field[this@DatabaseObject] as DatabaseObject)
                dataObject.update()
                column EQUALS dataObject.databaseID
            }

            type == Boolean::class.java                       ->
                column EQUALS field.getBoolean(this@DatabaseObject)

            type == Byte::class.java                          ->
                column EQUALS field.getByte(this@DatabaseObject)

            type == Short::class.java                         ->
                column EQUALS field.getShort(this@DatabaseObject)

            type == Int::class.java                           ->
                column EQUALS field.getInt(this@DatabaseObject)

            type == Long::class.java                          ->
                column EQUALS field.getLong(this@DatabaseObject)

            type == Float::class.java                         ->
                column EQUALS field.getFloat(this@DatabaseObject)

            type == Double::class.java                        ->
                column EQUALS field.getDouble(this@DatabaseObject)

            type == String::class.java                        ->
                column EQUALS field.get(this@DatabaseObject) as String

            type == ByteArray::class.java                     ->
                column EQUALS field.get(this@DatabaseObject) as ByteArray

            type == IntArray::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as IntArray

            type == Calendar::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as Calendar

            type == DataTime::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as DataTime

            type == DataDate::class.java                      ->
                column EQUALS field.get(this@DatabaseObject) as DataDate

            type.isEnum                                       ->
                column EQUALS_ENUM field.get(this@DatabaseObject)

            else                                              ->
                NEVER_MATCH_CONDITION
        }
    }
}
