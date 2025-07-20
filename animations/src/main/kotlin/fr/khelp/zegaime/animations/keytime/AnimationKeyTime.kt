package fr.khelp.zegaime.animations.keytime

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.collections.lists.SortedArray

/**
 * Generic animation based on key time relative to its starts
 *
 * It changes a value to an object
 *
 * @property animated Object that is animated
 * @param O Object type that cary information
 * @param V Value type to give at the object
 */
abstract class AnimationKeyTime<O : Any, V : Any>(protected val animated : O) : Animation
{
    /** Key times stored */
    private val keyTimes = SortedArray<KeyTime<V>>(unique = true)

    /** Initial value */
    private lateinit var initialValue : V

    /**
     * Sets a key time. That is to say, a value to give after the animation started for a given number of milliseconds
     *
     * @param timeMilliseconds Number of milliseconds after animation started
     * @param value Value to give
     * @param interpolation Interpolation to use to get there. By default [InterpolationLinear]
     */
    fun addKeyTimeValue(timeMilliseconds : Long, value : V, interpolation : Interpolation = InterpolationLinear)
    {
        argumentCheck(timeMilliseconds >= 0L) { "Time must be >= 0, not $timeMilliseconds" }

        synchronized(this.keyTimes)
        {
            val keyTime = KeyTime<V>(timeMilliseconds, value, interpolation)
            this.keyTimes -= keyTime
            this.keyTimes += keyTime
        }
    }

    override fun initialization()
    {
        this.initialValue = this.getValue(this.animated)
    }

    override fun animate(millisecondsSinceStarted : Long) : Boolean =
        synchronized(this.keyTimes)
        {
            if (this.keyTimes.empty)
            {
                return@synchronized false
            }

            val (minimum, maximum) = this.keyTimes.intervalOf(KeyTime(millisecondsSinceStarted,
                                                                      this.initialValue,
                                                                      InterpolationLinear))
            var stillAnimated = true

            when
            {
                minimum < 0        ->
                {
                    val keyTimeAfter = this.keyTimes[0]
                    val coefficient =
                        keyTimeAfter.interpolation(millisecondsSinceStarted.toDouble() /
                                                   keyTimeAfter.timeMilliseconds.toDouble())
                    this.interpolate(this.animated,
                                     this.initialValue, 1.0 - coefficient,
                                     keyTimeAfter.value, coefficient)
                }

                maximum < 0        ->
                {
                    stillAnimated = false
                    this.setValue(this.animated, this.keyTimes[this.keyTimes.size - 1].value)
                }

                minimum == maximum ->
                    this.setValue(this.animated, this.keyTimes[minimum].value)

                else               ->
                {
                    val keyBefore = this.keyTimes[minimum]
                    val keyAfter = this.keyTimes[maximum]
                    val coefficient =
                        keyAfter.interpolation((millisecondsSinceStarted - keyBefore.timeMilliseconds).toDouble() /
                                               (keyAfter.timeMilliseconds - keyBefore.timeMilliseconds).toDouble())
                    this.interpolate(this.animated,
                                     keyBefore.value, 1.0 - coefficient,
                                     keyAfter.value, coefficient)
                }
            }

            stillAnimated
        }

    /**
     * Gets an object current value
     *
     * @param animated Object to get is value
     *
     * @return Object current value
     */
    protected abstract fun getValue(animated : O) : V

    /**
     * Sets a value to an object
     *
     * @param animated Object to give the value
     * @param value Value to give to the object
     */
    protected abstract fun setValue(animated : O, value : V)

    /**
     * Applies an interpolated value
     *
     * It is up to implementation to compute the interpolated value and give the result to the object.
     *
     * For example, if the value is an integer
     *
     *The code can be
     * ```kotlin
     * override fun interpolate(animated : MyObject,
     *                          beforeValue:Int,beforeCoefficient:Double,
     *                          afterValue:Int, afterCoefficient:Double)
     * {
     *      val interpolated = (beforeValue*beforeCoefficient + afterValue*afterCoefficient).toInt()
     *      animated.setValue(interpolated)
     * }
     * ```
     *
     * See other implementations to have more examples
     *
     * @param animated Object to give the interpolated value
     * @param beforeValue Value before the instant
     * @param beforeCoefficient Coefficient influence of [beforeValue]
     * @param afterValue Value after the instant
     * @param afterCoefficient Coefficient influence of [afterValue]
     */
    protected abstract fun interpolate(animated : O,
                                       beforeValue : V, beforeCoefficient : Double,
                                       afterValue : V, afterCoefficient : Double)
}