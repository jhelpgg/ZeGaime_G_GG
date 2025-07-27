package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.UpdateDSL
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import fr.khelp.zegaime.utils.stateCheck
import java.util.Calendar

/**
 * Represents an update query.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `Table.update` method.
 *
 * **Standard usage:**
 * ```kotlin
 * table.update {
 *     COLUMN_AGE IS 31
 *     where { condition = COLUMN_NAME EQUALS "John" }
 * }
 * ```
 *
 * @property table The table to update.
 * @constructor Creates a new update query. For internal use only.
 */
class Update internal constructor(val table : Table)
{
    /**
     * The new values for the columns.
     */
    private val columnValues = HashMap<Column, String>()

    /**
     * The condition of the update query.
     */
    private var condition : Condition? = null

    /**
     * Specifies the new value for a string column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_NAME IS "John"
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : String)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.STRING)
        this@Update.columnValues[this] = "'$value'"
    }

    /**
     * Specifies the new value for a string column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "name" IS "John"
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : String)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a boolean column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_ACTIVE IS true
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Boolean)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BOOLEAN)
        this@Update.columnValues[this] =
            if (value)
            {
                "TRUE"
            }
            else
            {
                "FALSE"
            }
    }

    /**
     * Specifies the new value for a boolean column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "active" IS true
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Boolean)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a byte column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_VALUE IS 1.toByte()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Byte)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BYTE)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for a byte column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "value" IS 1.toByte()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Byte)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a short column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_VALUE IS 1.toShort()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Short)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.SHORT)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for a short column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "value" IS 1.toShort()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Short)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for an integer column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_AGE IS 31
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Int)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.INTEGER)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for an integer column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "age" IS 31
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Int)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a long column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_TIMESTAMP IS 1234567890L
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Long)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.LONG)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for a long column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "timestamp" IS 1234567890L
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Long)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a float column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_PRICE IS 12.34f
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Float)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.FLOAT)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for a float column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "price" IS 12.34f
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Float)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a double column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_PRICE IS 12.34
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Double)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.DOUBLE)
        this@Update.columnValues[this] = value.toString()
    }

    /**
     * Specifies the new value for a double column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "price" IS 12.34
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Double)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a byte array column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_DATA IS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : ByteArray)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.BYTE_ARRAY)
        this@Update.columnValues[this] = "'${value.base64}'"
    }

    /**
     * Specifies the new value for a byte array column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "data" IS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : ByteArray)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for an integer array column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_DATA IS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : IntArray)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.INT_ARRAY)
        this@Update.columnValues[this] = "'${value.serializeToByteArray().base64}'"
    }

    /**
     * Specifies the new value for an integer array column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "data" IS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : IntArray)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a calendar column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_DATE IS Calendar.getInstance()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : Calendar)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.CALENDAR)
        this@Update.columnValues[this] = value.timeInMillis.toString()
    }

    /**
     * Specifies the new value for a calendar column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "date" IS Calendar.getInstance()
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : Calendar)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a date column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_DATE IS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : DataDate)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.DATE)
        this@Update.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies the new value for a date column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "date" IS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : DataDate)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for a time column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_TIME IS DataTime(12, 0, 0)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun Column.IS(value : DataTime)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.TIME)
        this@Update.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies the new value for a time column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "time" IS DataTime(12, 0, 0)
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun String.IS(value : DataTime)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the new value for an enum column.
     *
     * **Usage example**
     * ```kotlin
     * COLUMN_ENUM IS MyEnum.A
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun <E : Enum<E>> Column.IS(value : E)
    {
        this@Update.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Update.columnValues[this] = "'${value::class.java.name}:${value.name}'"
    }

    /**
     * Specifies the new value for an enum column by its name.
     *
     * **Usage example**
     * ```kotlin
     * "enum" IS MyEnum.A
     * ```
     *
     * @param value The new value.
     */
    @UpdateDSL
    infix fun <E : Enum<E>> String.IS(value : E)
    {
        this@Update.table.getColumn(this) IS value
    }

    /**
     * Specifies the condition for the update.
     *
     * **Usage example**
     * ```kotlin
     * where { condition = COLUMN_NAME EQUALS "John" }
     * ```
     *
     * @param whereCreator A lambda function to define the where clause.
     */
    @WhereDSL
    fun where(whereCreator : Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    /**
     * Transfers the column values from an insert query to this update query.
     *
     * For internal use only.
     *
     * @param columnValues The column values to transfer.
     */
    internal fun transfer(columnValues : HashMap<Column, String>)
    {
        for ((column, value) in columnValues)
        {
            this.columnValues[column] = value
        }
    }

    /**
     * Returns the SQL representation of the update query.
     *
     * For internal use only.
     *
     * @return The SQL representation of the update query.
     */
    internal fun updateSQL() : String
    {
        stateCheck(this.columnValues.isNotEmpty()) { "Must update at least one thing" }
        val query = StringBuilder()
        query.append("UPDATE ")
        query.append(this.table.name)
        query.append(" SET ")
        var notFirst = false

        for ((column, value) in this.columnValues)
        {
            if (notFirst)
            {
                query.append(", ")
            }

            query.append(column.name)
            query.append('=')
            query.append(value)
            notFirst = true
        }

        this.condition?.let { condition ->
            query.append(" WHERE ")
            query.append(condition.sqlCondition)
        }

        return query.toString()
    }
}