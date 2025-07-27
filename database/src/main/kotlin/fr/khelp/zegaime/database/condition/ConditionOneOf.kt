package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import fr.khelp.zegaime.utils.extensions.string
import fr.khelp.zegaime.utils.extensions.transformArray
import fr.khelp.zegaime.utils.extensions.transformInt
import fr.khelp.zegaime.utils.extensions.transformLong
import java.util.Calendar

/**
 * Creates a condition that checks if the column's value is one of the given IDs.
 *
 * The column type must be `DataType.ID`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ID ONE_OF_ID intArrayOf(1, 2, 3)
 * ```
 *
 * @param selection The array of IDs to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.ID`.
 */
infix fun Column.ONE_OF_ID(selection: IntArray): Condition
{
    this.checkType(DataType.ID)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS_ID selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given strings.
 *
 * The column type must be `DataType.STRING`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME ONE_OF arrayOf("test1", "test2")
 * ```
 *
 * @param selection The array of strings to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
infix fun Column.ONE_OF(selection: Array<String>): Condition
{
    this.checkType(DataType.STRING)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("('", "', '", "')")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given booleans.
 *
 * The column type must be `DataType.BOOLEAN`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ACTIVE ONE_OF booleanArrayOf(true, false)
 * ```
 *
 * @param selection The array of booleans to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BOOLEAN`.
 */
infix fun Column.ONE_OF(selection: BooleanArray): Condition
{
    this.checkType(DataType.BOOLEAN)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given bytes.
 *
 * The column type must be `DataType.BYTE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE ONE_OF byteArrayOf(1.toByte(), 2.toByte())
 * ```
 *
 * @param selection The array of bytes to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BYTE`.
 */
infix fun Column.ONE_OF(selection: ByteArray): Condition
{
    this.checkType(DataType.BYTE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given shorts.
 *
 * The column type must be `DataType.SHORT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE ONE_OF shortArrayOf(1.toShort(), 2.toShort())
 * ```
 *
 * @param selection The array of shorts to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.SHORT`.
 */
infix fun Column.ONE_OF(selection: ShortArray): Condition
{
    this.checkType(DataType.SHORT)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given integers.
 *
 * The column type must be `DataType.INTEGER`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE ONE_OF intArrayOf(18, 19, 20)
 * ```
 *
 * @param selection The array of integers to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.INTEGER`.
 */
infix fun Column.ONE_OF(selection: IntArray): Condition
{
    this.checkType(DataType.INTEGER)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given longs.
 *
 * The column type must be `DataType.LONG`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP ONE_OF longArrayOf(1234567890L, 1234567891L)
 * ```
 *
 * @param selection The array of longs to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.LONG`.
 */
infix fun Column.ONE_OF(selection: LongArray): Condition
{
    this.checkType(DataType.LONG)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given floats.
 *
 * The column type must be `DataType.FLOAT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE ONE_OF floatArrayOf(12.34f, 56.78f)
 * ```
 *
 * @param selection The array of floats to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.FLOAT`.
 */
infix fun Column.ONE_OF(selection: FloatArray): Condition
{
    this.checkType(DataType.FLOAT)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given doubles.
 *
 * The column type must be `DataType.DOUBLE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE ONE_OF doubleArrayOf(12.34, 56.78)
 * ```
 *
 * @param selection The array of doubles to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DOUBLE`.
 */
infix fun Column.ONE_OF(selection: DoubleArray): Condition
{
    this.checkType(DataType.DOUBLE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${selection.string("(", ", ", ")")}")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given byte arrays.
 *
 * The column type must be `DataType.BYTE_ARRAY`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA ONE_OF arrayOf(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6))
 * ```
 *
 * @param selection The array of byte arrays to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BYTE_ARRAY`.
 */
infix fun Column.ONE_OF(selection: Array<ByteArray>): Condition
{
    this.checkType(DataType.BYTE_ARRAY)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> array.base64 }
                              .string("('", "', '", "')")
                      }")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given integer arrays.
 *
 * The column type must be `DataType.INT_ARRAY`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA ONE_OF arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6))
 * ```
 *
 * @param selection The array of integer arrays to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.INT_ARRAY`.
 */
infix fun Column.ONE_OF(selection: Array<IntArray>): Condition
{
    this.checkType(DataType.INT_ARRAY)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> array.serializeToByteArray().base64 }
                              .string("('", "', '", "')")
                      }")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given calendars.
 *
 * The column type must be `DataType.CALENDAR`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE ONE_OF arrayOf(Calendar.getInstance(), Calendar.getInstance())
 * ```
 *
 * @param selection The array of calendars to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.CALENDAR`.
 */
infix fun Column.ONE_OF(selection: Array<Calendar>): Condition
{
    this.checkType(DataType.CALENDAR)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformLong { array -> array.timeInMillis }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given dates.
 *
 * The column type must be `DataType.DATE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE ONE_OF arrayOf(DataDate(2023, 1, 1), DataDate(2023, 1, 2))
 * ```
 *
 * @param selection The array of dates to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DATE`.
 */
infix fun Column.ONE_OF(selection: Array<DataDate>): Condition
{
    this.checkType(DataType.DATE)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformInt { array -> array.serialized }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given times.
 *
 * The column type must be `DataType.TIME`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME ONE_OF arrayOf(DataTime(12, 0, 0), DataTime(13, 0, 0))
 * ```
 *
 * @param selection The array of times to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.TIME`.
 */
infix fun Column.ONE_OF(selection: Array<DataTime>): Condition
{
    this.checkType(DataType.TIME)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformInt { array -> array.serialized }
                              .string("(", ", ", ")")
                      }")
    }
}

/**
 * Creates a condition that checks if the column's value is one of the given enums.
 *
 * The column type must be `DataType.ENUM`.
 *
 * **Usage example:**
 * ```kotlin
 * enum class MyEnum { A, B }
 * val condition = COLUMN_ENUM ONE_OF arrayOf(MyEnum.A, MyEnum.B)
 * ```
 *
 * @param selection The array of enums to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.ENUM`.
 */
infix fun <E : Enum<E>> Column.ONE_OF(selection: Array<E>): Condition
{
    this.checkType(DataType.ENUM)

    return when
    {
        selection.isEmpty() -> NEVER_MATCH_CONDITION
        selection.size == 1 -> this EQUALS selection[0]
        else ->
            Condition(arrayOf(this),
                      "${this.name} IN ${
                          selection.transformArray { array -> "'${array::class.java.name}:${array.name}'" }
                              .string("(", ", ", ")")
                      }")
    }
}