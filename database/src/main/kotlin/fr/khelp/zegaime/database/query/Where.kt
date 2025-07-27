package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.MatchDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.condition.EQUALS
import fr.khelp.zegaime.database.condition.EQUALS_ID
import fr.khelp.zegaime.database.condition.IN
import fr.khelp.zegaime.database.condition.LIKE
import fr.khelp.zegaime.database.condition.LOWER
import fr.khelp.zegaime.database.condition.LOWER_EQUALS
import fr.khelp.zegaime.database.condition.NOT_EQUALS
import fr.khelp.zegaime.database.condition.NOT_EQUALS_ID
import fr.khelp.zegaime.database.condition.ONE_OF
import fr.khelp.zegaime.database.condition.ONE_OF_ID
import fr.khelp.zegaime.database.condition.UPPER
import fr.khelp.zegaime.database.condition.UPPER_EQUALS
import fr.khelp.zegaime.database.condition.regex
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.utils.regex.RegularExpression
import java.util.Calendar
import java.util.regex.Pattern

/**
 * Represents a `WHERE` clause in a query.
 *
 * This class provides methods to create conditions for the `WHERE` clause.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is used in the `where` block of a query.
 *
 * **Standard usage:**
 * ```kotlin
 * where {
 *     condition = COLUMN_NAME EQUALS "John" AND COLUMN_AGE GREATER_THAN 18
 * }
 * ```
 *
 * @property condition The condition for the `WHERE` clause.
 * @constructor Creates a new where clause. For internal use only.
 */
open class Where internal constructor(private val table: Table)
{
    /**
     * The condition for the `WHERE` clause.
     */
    var condition: Condition? = null

    // EQUALS

    /**
     * Creates a condition that checks if the column's value is equal to the given ID.
     *
     * **Usage example:**
     * ```kotlin
     * "ID" EQUALS_ID 1
     * ```
     *
     * @param id The ID to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS_ID(id: Int) = this@Where.table.getColumn(this) EQUALS_ID id

    /**
     * Creates a condition that checks if the column's value is equal to the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" EQUALS "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: String) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given boolean.
     *
     * **Usage example:**
     * ```kotlin
     * "active" EQUALS true
     * ```
     *
     * @param value The boolean to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Boolean) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" EQUALS 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Byte) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" EQUALS 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Short) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" EQUALS 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Int) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" EQUALS 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Long) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" EQUALS 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Float) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" EQUALS 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Double) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given byte array.
     *
     * **Usage example:**
     * ```kotlin
     * "data" EQUALS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The byte array to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: ByteArray) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given integer array.
     *
     * **Usage example:**
     * ```kotlin
     * "data" EQUALS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The integer array to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: IntArray) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" EQUALS Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: Calendar) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" EQUALS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: DataDate) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" EQUALS DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.EQUALS(value: DataTime) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Creates a condition that checks if the column's value is equal to the given enum.
     *
     * **Usage example:**
     * ```kotlin
     * "enum" EQUALS MyEnum.A
     * ```
     *
     * @param value The enum to compare with.
     * @return A new condition.
     */
    infix fun <E : Enum<E>> String.EQUALS(value: E) = this@Where.table.getColumn(this) EQUALS value

    // NOT_EQUALS

    /**
     * Creates a condition that checks if the column's value is not equal to the given ID.
     *
     * **Usage example:**
     * ```kotlin
     * "ID" NOT_EQUALS_ID 1
     * ```
     *
     * @param id The ID to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS_ID(id: Int) = this@Where.table.getColumn(this) NOT_EQUALS_ID id

    /**
     * Creates a condition that checks if the column's value is not equal to the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" NOT_EQUALS "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: String) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given boolean.
     *
     * **Usage example:**
     * ```kotlin
     * "active" NOT_EQUALS true
     * ```
     *
     * @param value The boolean to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Boolean) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" NOT_EQUALS 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Byte) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" NOT_EQUALS 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Short) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" NOT_EQUALS 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Int) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" NOT_EQUALS 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Long) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" NOT_EQUALS 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Float) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" NOT_EQUALS 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Double) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given byte array.
     *
     * **Usage example:**
     * ```kotlin
     * "data" NOT_EQUALS byteArrayOf(1, 2, 3)
     * ```
     *
     * @param value The byte array to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: ByteArray) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given integer array.
     *
     * **Usage example:**
     * ```kotlin
     * "data" NOT_EQUALS intArrayOf(1, 2, 3)
     * ```
     *
     * @param value The integer array to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: IntArray) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" NOT_EQUALS Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: Calendar) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" NOT_EQUALS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: DataDate) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" NOT_EQUALS DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.NOT_EQUALS(value: DataTime) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Creates a condition that checks if the column's value is not equal to the given enum.
     *
     * **Usage example:**
     * ```kotlin
     * "enum" NOT_EQUALS MyEnum.A
     * ```
     *
     * @param value The enum to compare with.
     * @return A new condition.
     */
    infix fun <E : Enum<E>> String.NOT_EQUALS(value: E) = this@Where.table.getColumn(this) NOT_EQUALS value

    // LOWER

    /**
     * Creates a condition that checks if the column's value is lower than the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" LOWER "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: String) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" LOWER 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Byte) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" LOWER 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Short) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" LOWER 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Int) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" LOWER 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Long) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" LOWER 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Float) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" LOWER 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Double) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" LOWER Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: Calendar) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" LOWER DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: DataDate) = this@Where.table.getColumn(this) LOWER value

    /**
     * Creates a condition that checks if the column's value is lower than the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" LOWER DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER(value: DataTime) = this@Where.table.getColumn(this) LOWER value

    // LOWER_EQUALS

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" LOWER_EQUALS "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: String) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" LOWER_EQUALS 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Byte) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" LOWER_EQUALS 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Short) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" LOWER_EQUALS 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Int) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" LOWER_EQUALS 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Long) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" LOWER_EQUALS 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Float) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" LOWER_EQUALS 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Double) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" LOWER_EQUALS Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: Calendar) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" LOWER_EQUALS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: DataDate) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is lower than or equal to the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" LOWER_EQUALS DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.LOWER_EQUALS(value: DataTime) = this@Where.table.getColumn(this) LOWER_EQUALS value

    // UPPER

    /**
     * Creates a condition that checks if the column's value is upper than the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" UPPER "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: String) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" UPPER 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Byte) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" UPPER 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Short) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" UPPER 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Int) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" UPPER 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Long) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" UPPER 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Float) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" UPPER 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Double) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" UPPER Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: Calendar) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" UPPER DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: DataDate) = this@Where.table.getColumn(this) UPPER value

    /**
     * Creates a condition that checks if the column's value is upper than the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" UPPER DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER(value: DataTime) = this@Where.table.getColumn(this) UPPER value

    // UPPER_EQUALS

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given string.
     *
     * **Usage example:**
     * ```kotlin
     * "name" UPPER_EQUALS "John"
     * ```
     *
     * @param value The string to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: String) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given byte.
     *
     * **Usage example:**
     * ```kotlin
     * "value" UPPER_EQUALS 1.toByte()
     * ```
     *
     * @param value The byte to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Byte) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given short.
     *
     * **Usage example:**
     * ```kotlin
     * "value" UPPER_EQUALS 1.toShort()
     * ```
     *
     * @param value The short to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Short) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given integer.
     *
     * **Usage example:**
     * ```kotlin
     * "age" UPPER_EQUALS 30
     * ```
     *
     * @param value The integer to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Int) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given long.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" UPPER_EQUALS 1234567890L
     * ```
     *
     * @param value The long to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Long) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given float.
     *
     * **Usage example:**
     * ```kotlin
     * "price" UPPER_EQUALS 12.34f
     * ```
     *
     * @param value The float to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Float) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given double.
     *
     * **Usage example:**
     * ```kotlin
     * "price" UPPER_EQUALS 12.34
     * ```
     *
     * @param value The double to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Double) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given calendar.
     *
     * **Usage example:**
     * ```kotlin
     * "date" UPPER_EQUALS Calendar.getInstance()
     * ```
     *
     * @param value The calendar to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: Calendar) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given date.
     *
     * **Usage example:**
     * ```kotlin
     * "date" UPPER_EQUALS DataDate(2023, 1, 1)
     * ```
     *
     * @param value The date to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: DataDate) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Creates a condition that checks if the column's value is upper than or equal to the given time.
     *
     * **Usage example:**
     * ```kotlin
     * "time" UPPER_EQUALS DataTime(12, 0, 0)
     * ```
     *
     * @param value The time to compare with.
     * @return A new condition.
     */
    infix fun String.UPPER_EQUALS(value: DataTime) = this@Where.table.getColumn(this) UPPER_EQUALS value

    // Match select

    /**
     * Creates a condition that checks if the column's value is in the result of a subquery.
     *
     * **Usage example:**
     * ```kotlin
     * "ID" IN {
     *     select(otherTable) {
     *         +COLUMN_ID
     *         where { "name" EQUALS "test" }
     *     }
     * }
     * ```
     *
     * @param matchCreator A lambda function to create the subquery.
     * @return A new condition.
     */
    @MatchDSL
    infix fun String.IN(matchCreator: Match.() -> Unit) = this@Where.table.getColumn(this) IN matchCreator

    // One of

    /**
     * Creates a condition that checks if the column's value is one of the given IDs.
     *
     * **Usage example:**
     * ```kotlin
     * "ID" ONE_OF_ID intArrayOf(1, 2, 3)
     * ```
     *
     * @param selection The array of IDs to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF_ID(selection: IntArray) = this@Where.table.getColumn(this) ONE_OF_ID selection

    /**
     * Creates a condition that checks if the column's value is one of the given strings.
     *
     * **Usage example:**
     * ```kotlin
     * "name" ONE_OF arrayOf("John", "Jane")
     * ```
     *
     * @param selection The array of strings to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<String>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given booleans.
     *
     * **Usage example:**
     * ```kotlin
     * "active" ONE_OF booleanArrayOf(true, false)
     * ```
     *
     * @param selection The array of booleans to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: BooleanArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given bytes.
     *
     * **Usage example:**
     * ```kotlin
     * "value" ONE_OF byteArrayOf(1.toByte(), 2.toByte())
     * ```
     *
     * @param selection The array of bytes to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: ByteArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given shorts.
     *
     * **Usage example:**
     * ```kotlin
     * "value" ONE_OF shortArrayOf(1.toShort(), 2.toShort())
     * ```
     *
     * @param selection The array of shorts to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: ShortArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given integers.
     *
     * **Usage example:**
     * ```kotlin
     * "age" ONE_OF intArrayOf(30, 40)
     * ```
     *
     * @param selection The array of integers to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: IntArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given longs.
     *
     * **Usage example:**
     * ```kotlin
     * "timestamp" ONE_OF longArrayOf(1234567890L, 1234567891L)
     * ```
     *
     * @param selection The array of longs to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: LongArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given floats.
     *
     * **Usage example:**
     * ```kotlin
     * "price" ONE_OF floatArrayOf(12.34f, 56.78f)
     * ```
     *
     * @param selection The array of floats to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: FloatArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given doubles.
     *
     * **Usage example:**
     * ```kotlin
     * "price" ONE_OF doubleArrayOf(12.34, 56.78)
     * ```
     *
     * @param selection The array of doubles to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: DoubleArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given byte arrays.
     *
     * **Usage example:**
     * ```kotlin
     * "data" ONE_OF arrayOf(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6))
     * ```
     *
     * @param selection The array of byte arrays to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<ByteArray>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given integer arrays.
     *
     * **Usage example:**
     * ```kotlin
     * "data" ONE_OF arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6))
     * ```
     *
     * @param selection The array of integer arrays to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<IntArray>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given calendars.
     *
     * **Usage example:**
     * ```kotlin
     * "date" ONE_OF arrayOf(Calendar.getInstance(), Calendar.getInstance())
     * ```
     *
     * @param selection The array of calendars to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<Calendar>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given dates.
     *
     * **Usage example:**
     * ```kotlin
     * "date" ONE_OF arrayOf(DataDate(2023, 1, 1), DataDate(2023, 1, 2))
     * ```
     *
     * @param selection The array of dates to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<DataDate>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value is one of the given times.
     *
     * **Usage example:**
     * ```kotlin
     * "time" ONE_OF arrayOf(DataTime(12, 0, 0), DataTime(13, 0, 0))
     * ```
     *
     * @param selection The array of times to compare with.
     * @return A new condition.
     */
    infix fun String.ONE_OF(selection: Array<DataTime>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Creates a condition that checks if the column's value matches the given pattern.
     *
     * **Usage example:**
     * ```kotlin
     * "name" LIKE "J%"
     * ```
     *
     * @param pattern The pattern to match.
     * @return A new condition.
     */
    infix fun String.LIKE(pattern: String) = this@Where.table.getColumn(this) LIKE pattern

    /**
     * Creates a condition that checks if the column's value is one of the given enums.
     *
     * **Usage example:**
     * ```kotlin
     * "enum" ONE_OF arrayOf(MyEnum.A, MyEnum.B)
     * ```
     *
     * @param selection The array of enums to compare with.
     * @return A new condition.
     */
    infix fun <E : Enum<E>> String.ONE_OF(selection: Array<E>) = this@Where.table.getColumn(this) ONE_OF selection

    // Regex

    /**
     * Creates a condition that checks if the column's value matches the given regular expression pattern.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_NAME REGEX Pattern.compile("J.*")
     * ```
     *
     * @param pattern The regex pattern to match.
     * @return A new condition.
     */
    infix fun Column.REGEX(pattern: Pattern) =
        this.regex(this@Where.table, pattern)

    /**
     * Creates a condition that checks if the column's value matches the given regular expression pattern.
     *
     * **Usage example:**
     * ```kotlin
     * "name" REGEX Pattern.compile("J.*")
     * ```
     *
     * @param pattern The regex pattern to match.
     * @return A new condition.
     */
    infix fun String.REGEX(pattern: Pattern) =
        this@Where.table.getColumn(this)
            .regex(this@Where.table, pattern)

    /**
     * Creates a condition that checks if the column's value matches the given regular expression.
     *
     * **Usage example:**
     * ```kotlin
     * COLUMN_NAME REGEX RegularExpression("J.*")
     * ```
     *
     * @param regularExpression The regular expression to match.
     * @return A new condition.
     */
    infix fun Column.REGEX(regularExpression: RegularExpression) =
        this.regex(this@Where.table, regularExpression)

    /**
     * Creates a condition that checks if the column's value matches the given regular expression.
     *
     * **Usage example:**
     * ```kotlin
     * "name" REGEX RegularExpression("J.*")
     * ```
     *
     * @param regularExpression The regular expression to match.
     * @return A new condition.
     */
    infix fun String.REGEX(regularExpression: RegularExpression) =
        this@Where.table.getColumn(this)
            .regex(this@Where.table, regularExpression)
}