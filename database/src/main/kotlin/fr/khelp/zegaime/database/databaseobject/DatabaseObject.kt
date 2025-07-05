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

abstract class DatabaseObject(internal val database : Database)
{
    companion object
    {
        fun table(database : Database, clazz : Class<out DatabaseObject>) : Table =
            DataObjectManager.tableDescription(database, clazz).table

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

        @WhereDatabaseObjectDSL
        inline fun <reified DO : DatabaseObject> delete(database : Database,
                                                        deleteCreator : WhereDatabaseObject<DO>.() -> Unit) : Int
        {
            val table = DatabaseObject.table(database, DO::class.java)
            val deleteDatabaseObject = WhereDatabaseObject<DO>(table)
            deleteCreator(deleteDatabaseObject)
            return table.delete { where { condition = deleteDatabaseObject.condition } }
        }

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

    fun <DO : DatabaseObject> waitCreated() : DO
    {
        this.futureResult.waitForCompletion()
        @Suppress("UNCHECKED_CAST")
        return this as DO
    }

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