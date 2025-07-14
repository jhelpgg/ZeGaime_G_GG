package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.DataRowResult
import fr.khelp.zegaime.database.Database
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import java.lang.reflect.Array
import java.util.Calendar
import kotlin.reflect.KClass

/**
 * Represents the result of a select query on a database object.
 *
 * This class is an iterator over the selected objects.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is returned by the `DatabaseObject.select` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val users = DatabaseObject.select<User>(database) {
 *     where { "age" GREATER_THAN 18 }
 * }
 * while (users.hasNext) {
 *     val user = users.next()
 *     // ...
 * }
 * users.close()
 * ```
 *
 * @param DO The type of the database object.
 * @property closed Indicates if the result set is closed.
 * @property hasNext Indicates if there are more objects in the result set.
 */
class DatabaseObjectResult<DO : DatabaseObject>(private val database : Database,
                                                private val dataObjectClass : KClass<DO>,
                                                private val rowResult : DataRowResult)
{
    val closed get() = this.rowResult.closed

    val hasNext get() = this.rowResult.hasNext

    /**
     * Closes the result set.
     *
     * **Usage example:**
     * ```kotlin
     * users.close()
     * ```
     */
    fun close()
    {
        this.rowResult.close()
    }

    /**
     * Returns the next object in the result set.
     *
     * **Usage example:**
     * ```kotlin
     * val user = users.next()
     * ```
     *
     * @return The next object in the result set.
     */
    fun next() : DO
    {
        val constructor = this.dataObjectClass.constructors.first()
        val declaredConstructor = this.dataObjectClass.java.declaredConstructors[0]
        val parameters = arrayOfNulls<Any>(constructor.parameters.size)

        this.rowResult.next {
            for ((index, parameter) in constructor.parameters.withIndex())
            {
                val columnName = parameter.name ?: continue
                val type = declaredConstructor.parameters[index].type

                when
                {
                    type == Database::class.java                                                    ->
                        parameters[index] = database

                    type.isArray && DatabaseObject::class.java.isAssignableFrom(type.componentType) ->
                    {
                        val arrayInt = getIntArray(table.getColumn(columnName))
                        val componentType = declaredConstructor.parameterTypes[index].componentType
                        val array = Array.newInstance(componentType, arrayInt.size)

                        for ((indexElement, id) in arrayInt.withIndex())
                        {
                            val result = DatabaseObject.table(database,
                                                              componentType as Class<out DatabaseObject>)
                                .select { where { condition = "ID" EQUALS_ID id } }
                            val dor = DatabaseObjectResult(database,
                                                           componentType.kotlin as KClass<out DatabaseObject>,
                                                           result)
                            Array.set(array, indexElement, dor.next())
                            dor.close()
                        }
                        parameters[index] = array
                    }

                    DatabaseObject::class.java.isAssignableFrom(type)                               ->
                    {
                        val id = getInt(table.getColumn(columnName))
                        val result = DatabaseObject.table(database,
                                                          declaredConstructor.parameterTypes[index] as Class<out DatabaseObject>)
                            .select { where { condition = "ID" EQUALS_ID id } }
                        val dor = DatabaseObjectResult(database,
                                                       declaredConstructor.parameterTypes[index].kotlin as KClass<out DatabaseObject>,
                                                       result)

                        if (dor.hasNext)
                        {
                            parameters[index] = dor.next()
                        }

                        dor.close()
                    }

                    type == Boolean::class.java                                                     ->
                        parameters[index] = getBoolean(table.getColumn(columnName))

                    type == Byte::class.java                                                        ->
                        parameters[index] = getByte(table.getColumn(columnName))

                    type == Short::class.java                                                       ->
                        parameters[index] = getShort(table.getColumn(columnName))

                    type == Int::class.java                                                         ->
                        parameters[index] = getInt(table.getColumn(columnName))

                    type == Long::class.java                                                        ->
                        parameters[index] = getLong(table.getColumn(columnName))

                    type == Float::class.java                                                       ->
                        parameters[index] = getFloat(table.getColumn(columnName))

                    type == Double::class.java                                                      ->
                        parameters[index] = getDouble(table.getColumn(columnName))

                    type == String::class.java                                                      ->
                        parameters[index] = getString(table.getColumn(columnName))

                    type == ByteArray::class.java                                                   ->
                        parameters[index] = getByteArray(table.getColumn(columnName))

                    type == IntArray::class.java                                                    ->
                        parameters[index] = getIntArray(table.getColumn(columnName))

                    type == Calendar::class.java                                                    ->
                        parameters[index] = getCalendar(table.getColumn(columnName))

                    type == DataDate::class.java                                                    ->
                        parameters[index] = getDate(table.getColumn(columnName))

                    type == DataTime::class.java                                                    ->
                        parameters[index] = getTime(table.getColumn(columnName))

                    type.isEnum                                                                     ->
                        parameters[index] = getEnumAny(table.getColumn(columnName))
                }
            }
        }

        return (declaredConstructor.newInstance(*parameters) as DO).waitCreated()
    }
}
