package fr.khelp.zegaime.database.type

import fr.khelp.zegaime.utils.extensions.hour
import fr.khelp.zegaime.utils.extensions.millisecond
import fr.khelp.zegaime.utils.extensions.minute
import fr.khelp.zegaime.utils.extensions.second
import java.util.Calendar

private const val MILLISECONDS_MASK = 0b0000_0000_0000_0000_0000_0011_1111_1111
private const val SECONDS_MASK = 0b0000_0000_0000_0000_1111_1100_0000_0000
private const val MINUTES_MASK = 0b0000_0000_0011_1111_0000_0000_0000_0000
private const val HOURS_MASK = 0b0000_0111_1100_0000_0000_0000_0000_0000
private const val DAYS_MASK = 0b1111_1000_0000_0000_0000_0000_0000_0000.toInt()
private const val SECONDS_SHIFT = 10
private const val MINUTES_SHIFT = SECONDS_SHIFT + 6
private const val HOURS_SHIFT = MINUTES_SHIFT + 6
private const val DAYS_SHIFT = HOURS_SHIFT + 5
private const val MAX_DAYS = 0b1_1111

/**
 * Represents a time duration in days, hours, minutes, seconds and milliseconds.
 *
 * This class is mutable.
 *
 * **Creation example:**
 * ```kotlin
 * val time = DataTime(1, 2, 3, 4) // 1h 2m 3s 4ms
 * val now = DataTime() // Current time
 * val copy = DataTime(now)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val time = DataTime(1, 2, 3, 4)
 * time.addHours(5)
 * println(time) // 06H 02M 03S 004MS
 * ```
 *
 * @property days The number of days.
 * @property hours The number of hours.
 * @property minutes The number of minutes.
 * @property seconds The number of seconds.
 * @property milliseconds The number of milliseconds.
 * @constructor Creates a new time duration.
 */
class DataTime() : Comparable<DataTime>
{
    /**
     * The number of days.
     */
    var days = 0
        private set
    /**
     * The number of hours.
     */
    var hours = 0
        private set
    /**
     * The number of minutes.
     */
    var minutes = 0
        private set
    /**
     * The number of seconds.
     */
    var seconds = 0
        private set
    /**
     * The number of milliseconds.
     */
    var milliseconds = 0
        private set
    /**
     * The serialized representation of the time.
     *
     * For internal use only.
     */
    internal var serialized = 0
        private set

    init
    {
        this.now()
    }

    /**
     * Creates a new time duration by copying another one.
     *
     * @param dataTime The time duration to copy.
     */
    constructor(dataTime: DataTime) : this()
    {
        this.copy(dataTime)
    }

    /**
     * Creates a new time duration with the given values.
     *
     * @param hours The number of hours.
     * @param minutes The number of minutes.
     * @param seconds The number of seconds.
     * @param milliseconds The number of milliseconds.
     */
    constructor(hours: Int, minutes: Int, seconds: Int, milliseconds: Int) : this()
    {
        this.days = 0
        this.hours = hours
        this.minutes = minutes
        this.seconds = seconds
        this.milliseconds = milliseconds
        this.validate()
    }

    /**
     * Creates a new time duration from a serialized representation.
     *
     * For internal use only.
     *
     * @param serialized The serialized representation.
     */
    internal constructor(serialized: Int) : this()
    {
        this.parse(serialized)
    }

    /**
     * Copies the values of another time duration to this one.
     *
     * @param dataTime The time duration to copy.
     */
    fun copy(dataTime: DataTime)
    {
        this.days = dataTime.days
        this.hours = dataTime.hours
        this.minutes = dataTime.minutes
        this.seconds = dataTime.seconds
        this.milliseconds = dataTime.milliseconds
        this.serialized = dataTime.serialized
    }

    /**
     * Sets the time to the current time.
     */
    fun now()
    {
        this.days = 0
        val calendar = Calendar.getInstance()
        this.hours = calendar.hour
        this.minutes = calendar.minute
        this.seconds = calendar.second
        this.milliseconds = calendar.millisecond
        this.update()
    }

    /**
     * Adds the given number of milliseconds to the time.
     *
     * @param milliseconds The number of milliseconds to add.
     */
    fun addMilliseconds(milliseconds: Int)
    {
        this.milliseconds += milliseconds
        this.validate()
    }

    /**
     * Subtracts the given number of milliseconds from the time.
     *
     * @param milliseconds The number of milliseconds to subtract.
     */
    fun subtractMilliseconds(milliseconds: Int)
    {
        this.milliseconds -= milliseconds
        this.validate()
    }

    /**
     * Adds the given number of seconds to the time.
     *
     * @param seconds The number of seconds to add.
     */
    fun addSeconds(seconds: Int)
    {
        this.seconds += seconds
        this.validate()
    }

    /**
     * Subtracts the given number of seconds from the time.
     *
     * @param seconds The number of seconds to subtract.
     */
    fun subtractSeconds(seconds: Int)
    {
        this.seconds -= seconds
        this.validate()
    }

    /**
     * Adds the given number of minutes to the time.
     *
     * @param minutes The number of minutes to add.
     */
    fun addMinutes(minutes: Int)
    {
        this.minutes += minutes
        this.validate()
    }

    /**
     * Subtracts the given number of minutes from the time.
     *
     * @param minutes The number of minutes to subtract.
     */
    fun subtractMinutes(minutes: Int)
    {
        this.minutes -= minutes
        this.validate()
    }

    /**
     * Adds the given number of hours to the time.
     *
     * @param hours The number of hours to add.
     */
    fun addHours(hours: Int)
    {
        this.hours += hours
        this.validate()
    }

    /**
     * Subtracts the given number of hours from the time.
     *
     * @param hours The number of hours to subtract.
     */
    fun subtractHours(hours: Int)
    {
        this.hours -= hours
        this.validate()
    }

    /**
     * Adds the given duration to the time.
     *
     * @param hours The number of hours to add.
     * @param minutes The number of minutes to add.
     * @param seconds The number of seconds to add.
     * @param milliseconds The number of milliseconds to add.
     */
    fun add(hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0)
    {
        this.hours += hours
        this.minutes += minutes
        this.seconds += seconds
        this.milliseconds += milliseconds
        this.validate()
    }

    /**
     * Subtracts the given duration from the time.
     *
     * @param hours The number of hours to subtract.
     * @param minutes The number of minutes to subtract.
     * @param seconds The number of seconds to subtract.
     * @param milliseconds The number of milliseconds to subtract.
     */
    fun subtract(hours: Int = 0, minutes: Int = 0, seconds: Int = 0, milliseconds: Int = 0)
    {
        this.hours -= hours
        this.minutes -= minutes
        this.seconds -= seconds
        this.milliseconds -= milliseconds
        this.validate()
    }

    /**
     * Adds the given time duration to this one.
     *
     * @param time The time duration to add.
     */
    operator fun plusAssign(time: DataTime)
    {
        this.add(time.hours, time.minutes, time.seconds, time.milliseconds)
    }

    /**
     * Returns a new time duration that is the sum of this one and the given one.
     *
     * @param time The time duration to add.
     * @return A new time duration.
     */
    operator fun plus(time: DataTime): DataTime
    {
        val dataTime = DataTime(this)
        dataTime += time
        return dataTime
    }

    /**
     * Subtracts the given time duration from this one.
     *
     * @param time The time duration to subtract.
     */
    operator fun minusAssign(time: DataTime)
    {
        this.subtract(time.hours, time.minutes, time.seconds, time.milliseconds)
    }

    /**
     * Returns a new time duration that is the difference between this one and the given one.
     *
     * @param time The time duration to subtract.
     * @return A new time duration.
     */
    operator fun minus(time: DataTime): DataTime
    {
        val dataTime = DataTime(this)
        dataTime -= time
        return dataTime
    }

    /**
     * Compares this time duration with another one.
     *
     * @param other The other time duration to compare with.
     * @return A negative integer, zero, or a positive integer as this time duration is less than, equal to, or greater than the specified time duration.
     */
    override operator fun compareTo(other: DataTime): Int
    {
        var comparison = this.days - other.days

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.hours - other.hours

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.minutes - other.minutes

        if (comparison != 0)
        {
            return comparison
        }

        comparison = this.seconds - other.seconds

        if (comparison != 0)
        {
            return comparison
        }

        return this.milliseconds - other.milliseconds
    }

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

        if (null == other || other !is DataTime)
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
     * Returns a string representation of the time duration.
     *
     * The format is `[days]d [hours]H [minutes]M [seconds]S [milliseconds]MS`.
     *
     * @return A string representation of the time duration.
     */
    override fun toString(): String
    {
        val stringBuilder = StringBuilder()
        var writing = false

        if (this.days > 0)
        {
            writing = true
            stringBuilder.append(this.days)
            stringBuilder.append("d ")
        }

        if (writing || this.hours > 0)
        {
            writing = true
            stringBuilder.append(String.format("%02d", this.hours))
            stringBuilder.append("H ")
        }

        if (writing || this.minutes > 0)
        {
            writing = true
            stringBuilder.append(String.format("%02d", this.minutes))
            stringBuilder.append("M ")
        }

        if (writing || this.seconds > 0)
        {
            stringBuilder.append(String.format("%02d", this.seconds))
            stringBuilder.append("S ")
        }

        stringBuilder.append(String.format("%03d", this.milliseconds))
        stringBuilder.append("MS")

        return stringBuilder.toString()
    }

    private fun parse(serialized: Int)
    {
        this.serialized = serialized
        this.days = (serialized and DAYS_MASK) shr DAYS_SHIFT
        this.hours = (serialized and HOURS_MASK) shr HOURS_SHIFT
        this.minutes = (serialized and MINUTES_MASK) shr MINUTES_SHIFT
        this.seconds = (serialized and SECONDS_MASK) shr SECONDS_SHIFT
        this.milliseconds = serialized and MILLISECONDS_MASK
    }

    private fun validate()
    {
        while (this.milliseconds < 0)
        {
            this.seconds--
            this.milliseconds += 1000
        }

        this.seconds += this.milliseconds / 1000
        this.milliseconds %= 1000

        while (this.seconds < 0)
        {
            this.minutes--
            this.seconds += 60
        }

        this.minutes += this.seconds / 60
        this.seconds %= 60

        while (this.minutes < 0)
        {
            this.hours--
            this.minutes += 60
        }

        this.hours += this.minutes / 60
        this.minutes %= 60

        while (this.hours < 0)
        {
            this.days--
            this.hours += 24
        }

        this.days += this.hours / 24
        this.hours %= 24

        this.days = this.days.coerceIn(0, MAX_DAYS)

        this.update()
    }

    private fun update()
    {
        this.serialized = (this.days shl DAYS_SHIFT) or
                (this.hours shl HOURS_SHIFT) or
                (this.minutes shl MINUTES_SHIFT) or
                (this.seconds shl SECONDS_SHIFT) or
                this.milliseconds
    }
}