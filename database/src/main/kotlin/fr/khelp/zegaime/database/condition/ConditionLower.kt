package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import java.util.Calendar

/**
 * Creates a condition that checks if the column's value is lower than the given string.
 *
 * The column type must be `DataType.STRING`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME LOWER "test"
 * ```
 *
 * @param string The string to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
infix fun Column.LOWER(string: String): Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}<'$string'")
}

/**
 * Creates a condition that checks if the column's value is lower than the given byte.
 *
 * The column type must be `DataType.BYTE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE LOWER 1.toByte()
 * ```
 *
 * @param value The byte to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.BYTE`.
 */
infix fun Column.LOWER(value: Byte): Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given short.
 *
 * The column type must be `DataType.SHORT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE LOWER 1.toShort()
 * ```
 *
 * @param value The short to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.SHORT`.
 */
infix fun Column.LOWER(value: Short): Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given integer.
 *
 * The column type must be `DataType.INTEGER`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE LOWER 18
 * ```
 *
 * @param value The integer to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.INTEGER`.
 */
infix fun Column.LOWER(value: Int): Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given long.
 *
 * The column type must be `DataType.LONG`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP LOWER 1234567890L
 * ```
 *
 * @param value The long to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.LONG`.
 */
infix fun Column.LOWER(value: Long): Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given float.
 *
 * The column type must be `DataType.FLOAT`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE LOWER 12.34f
 * ```
 *
 * @param value The float to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.FLOAT`.
 */
infix fun Column.LOWER(value: Float): Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given double.
 *
 * The column type must be `DataType.DOUBLE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE LOWER 12.34
 * ```
 *
 * @param value The double to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DOUBLE`.
 */
infix fun Column.LOWER(value: Double): Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Creates a condition that checks if the column's value is lower than the given calendar.
 *
 * The column type must be `DataType.CALENDAR`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE LOWER Calendar.getInstance()
 * ```
 *
 * @param value The calendar to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.CALENDAR`.
 */
infix fun Column.LOWER(value: Calendar): Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}<${value.timeInMillis}")
}

/**
 * Creates a condition that checks if the column's value is lower than the given date.
 *
 * The column type must be `DataType.DATE`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE LOWER DataDate(2023, 1, 1)
 * ```
 *
 * @param value The date to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.DATE`.
 */
infix fun Column.LOWER(value: DataDate): Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is lower than the given time.
 *
 * The column type must be `DataType.TIME`.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME LOWER DataTime(12, 0, 0)
 * ```
 *
 * @param value The time to compare with.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.TIME`.
 */
infix fun Column.LOWER(value: DataTime): Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}