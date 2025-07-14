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
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ID EQUALS_ID 1
 * ```
 *
 * @param id The ID to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS_ID(id : Int) : Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}=$id")
}

/**
 * Creates a condition that checks if the column's value is equal to the given string.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_NAME EQUALS "test"
 * ```
 *
 * @param string The string to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(string : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}='$string'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given boolean.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ACTIVE EQUALS true
 * ```
 *
 * @param value The boolean to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Boolean) : Condition
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
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE EQUALS 1.toByte()
 * ```
 *
 * @param value The byte to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Byte) : Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given short.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_VALUE EQUALS 1.toShort()
 * ```
 *
 * @param value The short to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Short) : Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given integer.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_AGE EQUALS 18
 * ```
 *
 * @param value The integer to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Int) : Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given long.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIMESTAMP EQUALS 1234567890L
 * ```
 *
 * @param value The long to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Long) : Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given float.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE EQUALS 12.34f
 * ```
 *
 * @param value The float to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Float) : Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given double.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_PRICE EQUALS 12.34
 * ```
 *
 * @param value The double to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Double) : Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}=$value")
}

/**
 * Creates a condition that checks if the column's value is equal to the given byte array.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA EQUALS byteArrayOf(1, 2, 3)
 * ```
 *
 * @param value The byte array to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : ByteArray) : Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.base64}'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given integer array.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATA EQUALS intArrayOf(1, 2, 3)
 * ```
 *
 * @param value The integer array to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : IntArray) : Condition
{
    this.checkType(DataType.INT_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.serializeToByteArray().base64}'")
}

/**
 * Creates a condition that checks if the column's value is equal to the given calendar.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE EQUALS Calendar.getInstance()
 * ```
 *
 * @param value The calendar to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : Calendar) : Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}=${value.timeInMillis}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given date.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_DATE EQUALS DataDate(2023, 1, 1)
 * ```
 *
 * @param value The date to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : DataDate) : Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given time.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_TIME EQUALS DataTime(12, 0, 0)
 * ```
 *
 * @param value The time to compare with.
 * @return A new condition.
 */
infix fun Column.EQUALS(value : DataTime) : Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}=${value.serialized}")
}

/**
 * Creates a condition that checks if the column's value is equal to the given enum.
 *
 * **Usage example:**
 * ```kotlin
 * enum class MyEnum { A, B }
 * val condition = COLUMN_ENUM EQUALS MyEnum.A
 * ```
 *
 * @param value The enum to compare with.
 * @return A new condition.
 */
infix fun <E : Enum<E>> Column.EQUALS(value : E) : Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}='${value::class.java.name}:${value.name}'")
}

/**
 * 
 */
internal infix fun Column.EQUALS_ENUM(value : Any) : Condition
{
    this.checkType(DataType.ENUM)
    val valueEnum = value::class.java.getField("name")
        .get(value) as String
    return Condition(arrayOf(this),
                     "${this.name}='${value::class.java.name}:$valueEnum'")
}
