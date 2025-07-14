package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import java.util.Calendar

/**
 * Creates a condition that checks if the column's value is not equal to the given ID.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ID NOT_EQUALS_ID 1
 * ```
 *
 * @param id The ID to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS_ID(id : Int) : Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}!=$id")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given string.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME NOT_EQUALS "test"
 * ```
 *
 * @param string The string to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(string : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}!='$string'")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given boolean.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ACTIVE NOT_EQUALS true
 * ```
 *
 * @param value The boolean to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Boolean) : Condition
{
    this.checkType(DataType.BOOLEAN)
    return if (value)
    {
        Condition(arrayOf(this), "${this.name}!=TRUE")
    }
    else
    {
        Condition(arrayOf(this), "${this.name}!=FALSE")
    }
}

/**
 * Creates a condition that checks if the column's value is not equal to the given byte.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE NOT_EQUALS 1.toByte()
 * ```
 *
 * @param value The byte to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Byte) : Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given short.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE NOT_EQUALS 1.toShort()
 * ```
 *
 * @param value The short to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Short) : Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given integer.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE NOT_EQUALS 18
 * ```
 *
 * @param value The integer to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Int) : Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given long.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP NOT_EQUALS 1234567890L
 * ```
 *
 * @param value The long to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Long) : Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given float.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE NOT_EQUALS 12.34f
 * ```
 *
 * @param value The float to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Float) : Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given double.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE NOT_EQUALS 12.34
 * ```
 *
 * @param value The double to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Double) : Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given byte array.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA NOT_EQUALS byteArrayOf(1, 2, 3)
 * ```
 *
 * @param value The byte array to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : ByteArray) : Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}!='${value.base64}'")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given integer array.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA NOT_EQUALS intArrayOf(1, 2, 3)
 * ```
 *
 * @param value The integer array to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : IntArray) : Condition
{
    this.checkType(DataType.INT_ARRAY)
    return Condition(arrayOf(this), "${this.name}!='${value.serializeToByteArray().base64}'")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given calendar.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE NOT_EQUALS Calendar.getInstance()
 * ```
 *
 * @param value The calendar to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : Calendar) : Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}!=${value.timeInMillis}")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given date.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE NOT_EQUALS DataDate(2023, 1, 1)
 * ```
 *
 * @param value The date to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : DataDate) : Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}!=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given time.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME NOT_EQUALS DataTime(12, 0, 0)
 * ```
 *
 * @param value The time to compare with.
 * @return A new condition.
 */
infix fun Column.NOT_EQUALS(value : DataTime) : Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}!=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is not equal to the given enum.
 *
 * **Usage example:**
 * ```kotlin
 * enum class MyEnum { A, B }
 * val condition = COLUMN_ENUM NOT_EQUALS MyEnum.A
 * ```
 *
 * @param value The enum to compare with.
 * @return A new condition.
 */
infix fun <E : Enum<E>> Column.NOT_EQUALS(value : E) : Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}!='${value::class.java.name}:${value.name}'")
}
