package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import java.util.Calendar

/**
 * Creates a condition that checks if the column's value is equal to the given ID.
 *
 * The column type must be `DataType.ID`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ID EQUALS_ID 1
 * ```
 *
 * @param id The ID to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.ID`.
 */
infix fun Column.EQUALS_ID(id: Int): Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}=$id")
}

/**
 * Creates a condition that checks if the column's value is equal to the given string.
 *
 * The column type must be `DataType.STRING`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME EQUALS "test"
 * ```
 *
 * @param string The string to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
infix fun Column.EQUALS(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}='$string'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given boolean.
 *
 * The column type must be `DataType.BOOLEAN`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ACTIVE EQUALS true
 * ```
 *
 * @param value The boolean to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BOOLEAN`.
 */
infix fun Column.EQUALS(value: Boolean): Condition
{
    this.checkType(DataType.BOOLEAN)
    return if (value)
    {
        Condition(arrayOf(this), "${this.name}=TRUE")
    }
    else
    {
        Condition(arrayOf(this), "${this.name}=FALSE")
    }
}

/**
 * Creates a condition that checks if the column's value is equal to the given byte.
 *
 * The column type must be `DataType.BYTE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE EQUALS 1.toByte()
 * ```
 *
 * @param value The byte to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BYTE`.
 */
infix fun Column.EQUALS(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given short.
 *
 * The column type must be `DataType.SHORT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE EQUALS 1.toShort()
 * ```
 *
 * @param value The short to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.SHORT`.
 */
infix fun Column.EQUALS(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given integer.
 *
 * The column type must be `DataType.INTEGER`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE EQUALS 18
 * ```
 *
 * @param value The integer to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.INTEGER`.
 */
infix fun Column.EQUALS(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given long.
 *
 * The column type must be `DataType.LONG`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP EQUALS 1234567890L
 * ```
 *
 * @param value The long to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.LONG`.
 */
infix fun Column.EQUALS(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given float.
 *
 * The column type must be `DataType.FLOAT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE EQUALS 12.34f
 * ```
 *
 * @param value The float to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.FLOAT`.
 */
infix fun Column.EQUALS(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given double.
 *
 * The column type must be `DataType.DOUBLE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE EQUALS 12.34
 * ```
 *
 * @param value The double to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DOUBLE`.
 */
infix fun Column.EQUALS(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given byte array.
 *
 * The column type must be `DataType.BYTE_ARRAY`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA EQUALS byteArrayOf(1, 2, 3)
 * ```
 *
 * @param value The byte array to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BYTE_ARRAY`.
 */
infix fun Column.EQUALS(value: ByteArray): Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.base64}'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given integer array.
 *
 * The column type must be `DataType.INT_ARRAY`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA EQUALS intArrayOf(1, 2, 3)
 * ```
 *
 * @param value The integer array to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.INT_ARRAY`.
 */
infix fun Column.EQUALS(value: IntArray): Condition
{
    this.checkType(DataType.INT_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.serializeToByteArray().base64}'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given calendar.
 *
 * The column type must be `DataType.CALENDAR`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE EQUALS Calendar.getInstance()
 * ```
 *
 * @param value The calendar to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.CALENDAR`.
 */
infix fun Column.EQUALS(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}=${value.timeInMillis}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given date.
 *
 * The column type must be `DataType.DATE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE EQUALS DataDate(2023, 1, 1)
 * ```
 *
 * @param value The date to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DATE`.
 */
infix fun Column.EQUALS(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given time.
 *
 * The column type must be `DataType.TIME`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME EQUALS DataTime(12, 0, 0)
 * ```
 *
 * @param value The time to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.TIME`.
 */
infix fun Column.EQUALS(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given enum.
 *
 * The column type must be `DataType.ENUM`.
 *
 * **Usage example:**
 * ```kotlin
 * enum class MyEnum { A, B }
 * val condition = COLUMN_ENUM EQUALS MyEnum.A
 * ```
 *
 * @param value The enum to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.ENUM`.
 */
infix fun <E : Enum<E>> Column.EQUALS(value: E): Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}='${value::class.java.name}:${value.name}'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given enum.
 *
 * This method is for internal use of the database system.
 *
 * @param value The enum to compare with.
 * @return A new condition.
 */
internal infix fun Column.EQUALS_ENUM(value: Any): Condition
{
    this.checkType(DataType.ENUM)
    val valueEnum = value::class.java.getField("name")
        .get(value) as String
    return Condition(arrayOf(this),
                     "${this.name}='${value::class.java.name}:$valueEnum'")
}