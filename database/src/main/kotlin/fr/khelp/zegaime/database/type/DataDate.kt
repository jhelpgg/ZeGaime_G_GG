package fr.khelp.zegaime.database.type

import fr.khelp.zegaime.utils.extensions.day
import fr.khelp.zegaime.utils.extensions.month
import fr.khelp.zegaime.utils.extensions.year
import java.util.Calendar

private const val DAY_MASK = 0b0000_0000_0000_0000_0000_0000_0001_1111
private const val MONTH_MASK = 0b0000_0000_0000_0000_0000_0001_1110_0000
private const val YEAR_MASK = 0b0000_0001_1111_1111_1111_1110_0000_0000
private const val MONTH_SHIFT = 5
private const val YEAR_SHIFT = MONTH_SHIFT + 4

/**
 * Represents a date (year, month, day).
 *
 * This class is immutable.
 *
 * **Creation example:**
 * ```kotlin
 * val date = DataDate(2023, 1, 1)
 * val today = DataDate()
 * val fromCalendar = DataDate(Calendar.getInstance())
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val date = DataDate(2023, 1, 1)
 * println(date.year)
 * println(date.month)
 * println(date.day)
 * println(date) // 2023/01/01
 * ```
 *
 * @property year The year.
 * @property month The month (1-12).
 * @property day The day of the month.
 * @constructor Creates a new date.
 */
class DataDate(year: Int, month: Int, day: Int) : Comparable<DataDate>
{
    /**
     * The year.
     */
    val year: Int
    /**
     * The month (1-12).
     */
    val month: Int
    /**
     * The day of the month.
     */
    val day: Int
    /**
     * The serialized representation of the date.
     *
     * For internal use only.
     */
    internal val serialized: Int

    init
    {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        this.year = calendar.year
        this.month = calendar.month + 1
        this.day = calendar.day
        this.serialized = (this.year shl YEAR_SHIFT) or
                (this.month shl MONTH_SHIFT) or
                this.day
    }

    /**
     * Creates a new date from a calendar instance.
     *
     * If no calendar is provided, the current date is used.
     *
     * @param calendar The calendar instance.
     */
    constructor(calendar: Calendar = Calendar.getInstance()) : this(calendar.year, calendar.month + 1, calendar.day)

    /**
     * Creates a new date from a serialized representation.
     *
     * For internal use only.
     *
     * @param serialized The serialized representation.
     */
    internal constructor(serialized: Int) :
            this((serialized and YEAR_MASK) shr YEAR_SHIFT,
                 (serialized and MONTH_MASK) shr MONTH_SHIFT,
                 serialized and DAY_MASK)

    /**
     * Compares this date with another one.
     *
     * @param other The other date to compare with.
     * @return A negative integer, zero, or a positive integer as this date is less than, equal to, or greater than the specified date.
     */
    override operator fun compareTo(other: DataDate): Int =
        this.serialized - other.serialized

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other The reference object with which to compare.
     * @return `true` if this object is the same as the obj argument; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is DataDate)
        {
            return false
        }

        return this.serialized == other.serialized
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    override fun hashCode(): Int =
        this.serialized

    /**
     * Returns a string representation of the date.
     *
     * The format is `YYYY/MM/DD`.
     *
     * @return A string representation of the date.
     */
    override fun toString(): String =
        String.format("%04d/%02d/%02d", this.year, this.month, this.day)
}