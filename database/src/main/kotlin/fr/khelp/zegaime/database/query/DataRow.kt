package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.RowResultDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.fullString
import fr.khelp.zegaime.utils.extensions.parseToIntArray
import fr.khelp.zegaime.utils.extensions.string
import java.sql.ResultSet
import java.util.Calendar

/**
 * Represents a row in a result set.
 *
 * This class provides methods to get the values of the columns of the current row.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is provided by the `DataRowResult.next` method.
 *
 * **Standard usage:**
 * ```kotlin
 * result.next {
 *     val name = getString(COLUMN_NAME)
 *     val age = getInt(COLUMN_AGE)
 *     // ...
 * }
 * ```
 *
 * @property table The table from which the result was obtained.
 * @property numberOfColumns The number of columns in the row.
 */
class DataRow internal constructor(private val resultSet : ResultSet, private val select : Select, val table : Table)
{

    /**Number of columns in the answer*/
    val numberOfColumns = this.select.numberColumns

    /**
     * Returns the index of the column with the given name.
     *
     * **Usage example:**
     * ```kotlin
     * val nameIndex = columnIndex("name")
     * ```
     *
     * @param columnName The name of the column.
     * @return The index of the column.
     */
    fun columnIndex(columnName : String) = this.select.columnIndex(columnName)

    /**
     * Returns the index of the given column.
     *
     * **Usage example:**
     * ```kotlin
     * val nameIndex = columnIndex(COLUMN_NAME)
     * ```
     *
     * @param column The column.
     * @return The index of the column.
     */
    fun columnIndex(column : Column) = this.select.columnIndex(column)

    /**
     * Returns the 1-based index of the column with the given name.
     *
     * **Usage example:**
     * ```kotlin
     * val nameIndex = columnRange("name")
     * ```
     *
     * @param columnName The name of the column.
     * @return The 1-based index of the column.
     */
    fun columnRange(columnName : String) : Int
    {
        val index = this.columnIndex(columnName)
        return if (index >= 0)
        {
            index + 1
        }
        else
        {
            index
        }
    }

    /**
     * Returns the 1-based index of the given column.
     *
     * **Usage example:**
     * ```kotlin
     * val nameIndex = columnRange(COLUMN_NAME)
     * ```
     *
     * @param column The column.
     * @return The 1-based index of the column.
     */
    fun columnRange(column : Column) : Int
    {
        val index = this.columnIndex(column)
        return if (index >= 0)
        {
            index + 1
        }
        else
        {
            index
        }
    }

    /**
     * Returns the column at the given 1-based index.
     *
     * **Usage example:**
     * ```kotlin
     * val column = column(1)
     * ```
     *
     * @param columnRange The 1-based index of the column.
     * @return The column at the given index.
     */
    fun column(columnRange : Int) = this.select[columnRange - 1]

    /**
     * Reads the ID from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val id = getID(COLUMN_ID)
     * ```
     *
     * @param column The column to read from.
     * @return The ID.
     */
    @RowResultDSL
    fun getID(column : Column) : Int
    {
        column.checkType(DataType.ID)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getInt(index + 1)
    }

    /**
     * Reads the ID from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val id = getID(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The ID.
     */
    @RowResultDSL
    fun getID(columnRange : Int) =
        this.getID(this.select[columnRange - 1])

    /**
     * Reads the string from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val name = getString(COLUMN_NAME)
     * ```
     *
     * @param column The column to read from.
     * @return The string.
     */
    @RowResultDSL
    fun getString(column : Column) : String
    {
        column.checkType(DataType.STRING)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1)
    }

    /**
     * Reads the string from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val name = getString(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The string.
     */
    @RowResultDSL
    fun getString(columnRange : Int) =
        this.getString(this.select[columnRange - 1])

    /**
     * Reads the boolean from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val active = getBoolean(COLUMN_ACTIVE)
     * ```
     *
     * @param column The column to read from.
     * @return The boolean.
     */
    @RowResultDSL
    fun getBoolean(column : Column) : Boolean
    {
        column.checkType(DataType.BOOLEAN)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getBoolean(index + 1)
    }

    /**
     * Reads the boolean from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val active = getBoolean(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The boolean.
     */
    @RowResultDSL
    fun getBoolean(columnRange : Int) =
        this.getBoolean(this.select[columnRange - 1])

    /**
     * Reads the byte from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val value = getByte(COLUMN_VALUE)
     * ```
     *
     * @param column The column to read from.
     * @return The byte.
     */
    @RowResultDSL
    fun getByte(column : Column) : Byte
    {
        column.checkType(DataType.BYTE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getByte(index + 1)
    }

    /**
     * Reads the byte from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val value = getByte(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The byte.
     */
    @RowResultDSL
    fun getByte(columnRange : Int) =
        this.getByte(this.select[columnRange - 1])

    /**
     * Reads the short from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val value = getShort(COLUMN_VALUE)
     * ```
     *
     * @param column The column to read from.
     * @return The short.
     */
    @RowResultDSL
    fun getShort(column : Column) : Short
    {
        column.checkType(DataType.SHORT)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getShort(index + 1)
    }

    /**
     * Reads the short from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val value = getShort(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The short.
     */
    @RowResultDSL
    fun getShort(columnRange : Int) =
        this.getShort(this.select[columnRange - 1])

    /**
     * Reads the integer from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val age = getInt(COLUMN_AGE)
     * ```
     *
     * @param column The column to read from.
     * @return The integer.
     */
    @RowResultDSL
    fun getInt(column : Column) : Int
    {
        column.checkType(DataType.INTEGER)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getInt(index + 1)
    }

    /**
     * Reads the integer from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val age = getInt(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The integer.
     */
    @RowResultDSL
    fun getInt(columnRange : Int) =
        this.getInt(this.select[columnRange - 1])

    /**
     * Reads the long from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val timestamp = getLong(COLUMN_TIMESTAMP)
     * ```
     *
     * @param column The column to read from.
     * @return The long.
     */
    @RowResultDSL
    fun getLong(column : Column) : Long
    {
        column.checkType(DataType.LONG)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getLong(index + 1)
    }

    /**
     * Reads the long from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val timestamp = getLong(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The long.
     */
    @RowResultDSL
    fun getLong(columnRange : Int) =
        this.getLong(this.select[columnRange - 1])

    /**
     * Reads the float from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val price = getFloat(COLUMN_PRICE)
     * ```
     *
     * @param column The column to read from.
     * @return The float.
     */
    @RowResultDSL
    fun getFloat(column : Column) : Float
    {
        column.checkType(DataType.FLOAT)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getDouble(index + 1)
            .toFloat()
    }

    /**
     * Reads the float from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val price = getFloat(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The float.
     */
    @RowResultDSL
    fun getFloat(columnRange : Int) =
        this.getFloat(this.select[columnRange - 1])

    /**
     * Reads the double from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val price = getDouble(COLUMN_PRICE)
     * ```
     *
     * @param column The column to read from.
     * @return The double.
     */
    @RowResultDSL
    fun getDouble(column : Column) : Double
    {
        column.checkType(DataType.DOUBLE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getDouble(index + 1)
    }

    /**
     * Reads the double from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val price = getDouble(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The double.
     */
    @RowResultDSL
    fun getDouble(columnRange : Int) =
        this.getDouble(this.select[columnRange - 1])

    /**
     * Reads the byte array from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val data = getByteArray(COLUMN_DATA)
     * ```
     *
     * @param column The column to read from.
     * @return The byte array.
     */
    @RowResultDSL
    fun getByteArray(column : Column) : ByteArray
    {
        column.checkType(DataType.BYTE_ARRAY)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1).base64
    }

    /**
     * Reads the integer array from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val data = getIntArray(COLUMN_DATA)
     * ```
     *
     * @param column The column to read from.
     * @return The integer array.
     */
    @RowResultDSL
    fun getIntArray(column : Column) : IntArray
    {
        column.checkType(DataType.INT_ARRAY)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return this.resultSet.getString(index + 1).base64.parseToIntArray()
    }

    /**
     * Reads the byte array from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val data = getByteArray(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The byte array.
     */
    @RowResultDSL
    fun getByteArray(columnRange : Int) =
        this.getByteArray(this.select[columnRange - 1])

    /**
     * Reads the integer array from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val data = getIntArray(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The integer array.
     */
    @RowResultDSL
    fun getIntArray(columnRange : Int) =
        this.getIntArray(this.select[columnRange - 1])

    /**
     * Reads the calendar from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val date = getCalendar(COLUMN_DATE)
     * ```
     *
     * @param column The column to read from.
     * @return The calendar.
     */
    @RowResultDSL
    fun getCalendar(column : Column) : Calendar
    {
        column.checkType(DataType.CALENDAR)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this.resultSet.getLong(index + 1)
        return calendar
    }

    /**
     * Reads the calendar from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val date = getCalendar(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The calendar.
     */
    @RowResultDSL
    fun getCalendar(columnRange : Int) =
        this.getCalendar(this.select[columnRange - 1])

    /**
     * Reads the date from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val date = getDate(COLUMN_DATE)
     * ```
     *
     * @param column The column to read from.
     * @return The date.
     */
    @RowResultDSL
    fun getDate(column : Column) : DataDate
    {
        column.checkType(DataType.DATE)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return DataDate(this.resultSet.getInt(index + 1))
    }

    /**
     * Reads the date from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val date = getDate(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The date.
     */
    @RowResultDSL
    fun getDate(columnRange : Int) =
        this.getDate(this.select[columnRange - 1])

    /**
     * Reads the time from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val time = getTime(COLUMN_TIME)
     * ```
     *
     * @param column The column to read from.
     * @return The time.
     */
    @RowResultDSL
    fun getTime(column : Column) : DataTime
    {
        column.checkType(DataType.TIME)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        return DataTime(this.resultSet.getInt(index + 1))
    }

    /**
     * Reads the time from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val time = getTime(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The time.
     */
    @RowResultDSL
    fun getTime(columnRange : Int) =
        this.getTime(this.select[columnRange - 1])

    /**
     * Reads the enum from the specified column.
     *
     * **Usage example:**
     * ```kotlin
     * val enumValue = getEnum<MyEnum>(COLUMN_ENUM)
     * ```
     *
     * @param E The type of the enum.
     * @param column The column to read from.
     * @return The enum value.
     */
    @RowResultDSL
    fun <E : Enum<E>> getEnum(column : Column) : E
    {
        column.checkType(DataType.ENUM)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val serialized = this.resultSet.getString(index + 1)
        val indexSeparator = serialized.indexOf(':')
        val className = serialized.substring(0, indexSeparator)
        val enumName = serialized.substring(indexSeparator + 1)
        val valueOf = Class.forName(className)
            .getDeclaredMethod("valueOf", String::class.java)
        ("UNCHECKED_CAST")
        return valueOf.invoke(null, enumName) as E
    }

    /**
     * 
     */
    @RowResultDSL
    internal fun getEnumAny(column : Column) : Any
    {
        column.checkType(DataType.ENUM)
        val index = this.columnIndex(column)
        argumentCheck(index >= 0) { "Column ${column.name} not in selection list" }
        val serialized = this.resultSet.getString(index + 1)
        val indexSeparator = serialized.indexOf(':')
        val className = serialized.substring(0, indexSeparator)
        val enumName = serialized.substring(indexSeparator + 1)
        val valueOf = Class.forName(className)
            .getDeclaredMethod("valueOf", String::class.java)
        return valueOf.invoke(null, enumName)
    }

    /**
     * Reads the enum from the specified column range.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val enumValue = getEnum<MyEnum>(1)
     * ```
     *
     * @param E The type of the enum.
     * @param columnRange The 1-based index of the column to read from.
     * @return The enum value.
     */
    @RowResultDSL
    fun <E : Enum<E>> getEnum(columnRange : Int) =
        this.getEnum<E>(this.select[columnRange - 1])

    /**
     * Reads the value from the specified column and returns a string representation of it.
     *
     * **Warning** Column range start at **1** not **0**
     *
     * **Usage example:**
     * ```kotlin
     * val value = toString(1)
     * ```
     *
     * @param columnRange The 1-based index of the column to read from.
     * @return The string representation of the value.
     */
    @RowResultDSL
    fun toString(columnRange : Int) =
        when (this.select[columnRange - 1].type)
        {
            DataType.ID         -> this.resultSet.getInt(columnRange).toString()
            DataType.STRING     -> this.resultSet.getString(columnRange)
            DataType.BOOLEAN    -> this.resultSet.getBoolean(columnRange).toString()
            DataType.BYTE       -> this.resultSet.getByte(columnRange).toString()
            DataType.SHORT      -> this.resultSet.getShort(columnRange).toString()
            DataType.INTEGER    -> this.resultSet.getInt(columnRange).toString()
            DataType.LONG       -> this.resultSet.getLong(columnRange).toString()
            DataType.FLOAT      -> this.resultSet.getDouble(columnRange).toString()
            DataType.DOUBLE     -> this.resultSet.getDouble(columnRange).toString()
            DataType.BYTE_ARRAY -> this.resultSet.getString(columnRange).base64.string()
            DataType.INT_ARRAY  -> this.resultSet.getString(columnRange).base64.parseToIntArray().string()
            DataType.CALENDAR   ->
            {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = this.resultSet.getLong(columnRange)
                calendar.fullString()
            }

            DataType.DATE       -> DataDate(this.resultSet.getInt(columnRange)).toString()
            DataType.TIME       -> DataTime(this.resultSet.getInt(columnRange)).toString()
            DataType.ENUM       -> this.resultSet.getString(columnRange)
        }
}
