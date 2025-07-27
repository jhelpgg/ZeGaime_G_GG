package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.InsertDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.condition.EQUALS_ID
import fr.khelp.zegaime.database.exception.NoValueDefinedAndNoDefaultValue
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import java.util.Calendar
import java.util.TreeSet

/**
 * Represents an insert query.
 *
 * It can be used as an insert or update query if a condition is specified in [updateIfExactlyOneRowMatch]
 * and that condition matches one and only one row.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `Table.insert` method.
 *
 * **Standard usage:**
 * ```kotlin
 * table.insert {
 *     COLUMN_NAME IS "John"
 *     COLUMN_AGE IS 30
 * }
 * ```
 *
 * @property table The table to insert into.
 * @constructor Creates a new insert query. For internal use only.
 */
class Insert internal constructor(val table: Table)
{
    /**
     * The values to insert.
     */
    private val columnValues = HashMap<Column, String>()
    /**
     * The condition for the update query.
     */
    internal var conditionUpdateOneMatch: Condition? = null

    /**
     * Specifies the value for a string column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_NAME IS "John"
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: String)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.STRING)
        this@Insert.columnValues[this] = "'$value'"
    }

    /**
     * Specifies the value for a string column.
     *
     * **Usage example:**
     * ```kotlin
     * "name" IS "John"
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: String)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a boolean column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_ACTIVE IS true
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Boolean)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BOOLEAN)
        this@Insert.columnValues[this] =
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
     * Specifies the value for a boolean column.
     *
     * **Usage example:**
     * ```kotlin
     * "active" IS true
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Boolean)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a byte column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_VALUE IS 1.toByte()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Byte)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for a byte column.
     *
     * **Usage example:**
     * ```kotlin
     * "value" IS 1.toByte()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Byte)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a short column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_VALUE IS 1.toShort()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Short)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.SHORT)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for a short column.
     *
     * **Usage example:**
     * ```kotlin
     * "value" IS 1.toShort()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Short)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for an integer column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_AGE IS 30
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Int)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.INTEGER)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for an integer column.
     *
     * **Usage example:**
     * ```kotlin
     * "age" IS 30
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Int)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a long column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_TIMESTAMP IS 1234567890L
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Long)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.LONG)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for a long column.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" IS 1234567890L
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Long)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a float column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_PRICE IS 12.34f
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Float)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.FLOAT)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for a float column.
     *
     * **Usage example:**
     * ```kotlin
     * "price" IS 12.34f
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Float)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a double column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_PRICE IS 12.34
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Double)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DOUBLE)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies the value for a double column.
     *
     * **Usage example:**
     * ```kotlin
     * "price" IS 12.34
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Double)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a byte array column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_DATA IS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: ByteArray)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE_ARRAY)
        this@Insert.columnValues[this] = "'${value.base64}'"
    }

    /**
     * Specifies the value for a byte array column.
     *
     * **Usage example:**
     * ```kotlin
     * "data" IS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: ByteArray)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for an integer array column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_DATA IS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: IntArray)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.INT_ARRAY)
        this@Insert.columnValues[this] = "'${value.serializeToByteArray().base64}'"
    }

    /**
     * Specifies the value for an integer array column.
     *
     * **Usage example:**
     * ```kotlin
     * "data" IS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: IntArray)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a calendar column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_DATE IS Calendar.getInstance()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: Calendar)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.CALENDAR)
        this@Insert.columnValues[this] = value.timeInMillis.toString()
    }

    /**
     * Specifies the value for a calendar column.
     *
     * **Usage example:**
     * ```kotlin
     * "date" IS Calendar.getInstance()
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: Calendar)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a date column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_DATE IS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: DataDate)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DATE)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies the value for a date column.
     *
     * **Usage example:**
     * ```kotlin
     * "date" IS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: DataDate)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for a time column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_TIME IS DataTime(12, 0, 0)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun Column.IS(value: DataTime)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.TIME)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies the value for a time column.
     *
     * **Usage example:**
     * ```kotlin
     * "time" IS DataTime(12, 0, 0)
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun String.IS(value: DataTime)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for an enum column.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_ENUM IS MyEnum.A
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun <E : Enum<E>> Column.IS(value: E)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Insert.columnValues[this] = "'${value::class.java.name}:${value.name}'"
    }

    /**
     * Specifies the value for an enum column.
     *
     * **Usage example:**
     * ```kotlin
     * "enum" IS MyEnum.A
     * ```
     *
     * @param value The value to insert.
     */
    @InsertDSL
    infix fun <E : Enum<E>> String.IS(value: E)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies the value for an enum column.
     *
     * For internal use only.
     *
     * @param value The value to insert.
     */
    @InsertDSL
    internal infix fun Column.IS_ENUM(value: Any)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Insert.columnValues[this] =
            "'${value::class.java.name}:${value::class.java.getDeclaredField("name").get(value)}'"
    }

    /**
     * Specifies the value for an enum column.
     *
     * For internal use only.
     *
     * @param value The value to insert.
     */
    @InsertDSL
    internal infix fun String.IS_ENUM(value: Any)
    {
        this@Insert.table.getColumn(this) IS_ENUM value
    }

    /**
     * If specified and the condition matches one and only one row, this row is updated.
     * Otherwise, an insert is performed.
     *
     * **Usage example:**
     * ```kotlin
     * updateIfExactlyOneRowMatch {
     *     condition = COLUMN_NAME EQUALS "John"
     * }
     * ```
     *
     * @param whereCreator A lambda function to define the where clause.
     */
    @WhereDSL
    fun updateIfExactlyOneRowMatch(whereCreator: Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        condition.checkAllColumns(this.table)
        this.conditionUpdateOneMatch = condition
    }

    /**
     * Returns the SQL representation of the insert query.
     *
     * For internal use only.
     *
     * @param suggestedID The suggested ID for the new row.
     * @return The SQL representation of the insert query.
     */
    internal fun insertSQL(suggestedID: Int): String
    {
        val columnLeft = TreeSet<Column>()

        for (column in this.table)
        {
            if (column.type != DataType.ID)
            {
                columnLeft.add(column)
            }
        }

        val query = StringBuilder()
        query.append("INSERT INTO ")
        query.append(this.table.name)
        query.append(" (")
        val queryValues = StringBuilder()
        queryValues.append(") VALUES (")

        query.append(COLUMN_ID.name)
        queryValues.append(suggestedID)

        for ((column, value) in this.columnValues)
        {
            query.append(", ")
            queryValues.append(", ")
            query.append(column.name)
            queryValues.append(value)
            columnLeft.remove(column)
        }

        var defaultValue: String

        for (column in columnLeft)
        {
            defaultValue = column.type.defaultValueSerialized

            if (defaultValue.isEmpty())
            {
                throw NoValueDefinedAndNoDefaultValue(column)
            }

            query.append(", ")
            queryValues.append(", ")
            query.append(column.name)
            queryValues.append(defaultValue)
        }

        query.append(queryValues)
        query.append(")")

        return query.toString()
    }

    /**
     * Returns the SQL representation of the update query.
     *
     * For internal use only.
     *
     * @param id The ID of the row to update.
     * @return The SQL representation of the update query.
     */
    internal fun updateSQL(id: Int): String
    {
        val update = Update(this.table)
        update.transfer(this.columnValues)
        update.where { condition = COLUMN_ID EQUALS_ID id }
        return update.updateSQL()
    }
}