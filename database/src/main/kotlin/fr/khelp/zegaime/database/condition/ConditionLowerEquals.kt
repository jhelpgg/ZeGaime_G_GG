package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import java.util.Calendar

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given string.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME LOWER_EQUALS "test"
 * ```
 *
 * @param string The string to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(string : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}<='$string'")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given byte.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE LOWER_EQUALS 1.toByte()
 * ```
 *
 * @param value The byte to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Byte) : Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given short.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE LOWER_EQUALS 1.toShort()
 * ```
 *
 * @param value The short to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Short) : Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given integer.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE LOWER_EQUALS 18
 * ```
 *
 * @param value The integer to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Int) : Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given long.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP LOWER_EQUALS 1234567890L
 * ```
 *
 * @param value The long to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Long) : Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given float.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE LOWER_EQUALS 12.34f
 * ```
 *
 * @param value The float to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Float) : Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given double.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE LOWER_EQUALS 12.34
 * ```
 *
 * @param value The double to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Double) : Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}<=$value")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given calendar.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE LOWER_EQUALS Calendar.getInstance()
 * ```
 *
 * @param value The calendar to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : Calendar) : Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}<=${value.timeInMillis}")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given date.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE LOWER_EQUALS DataDate(2023, 1, 1)
 * ```
 *
 * @param value The date to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : DataDate) : Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}<=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is lower than or equal to the given time.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME LOWER_EQUALS DataTime(12, 0, 0)
 * ```
 *
 * @param value The time to compare with.
 * @return A new condition.
 */
infix fun Column.LOWER_EQUALS(value : DataTime) : Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}<=${value.serialized}")
}

