package fr.khelp.zegaime.animations.keytime

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.collections.lists.SortedArray

/**
 * A generic animation based on key times.
 *
 * This animation interpolates a value of an object over time, based on a set of keyframes.
 * Each keyframe defines a value at a specific time.
 *
 * To create a key time animation, you need to extend this class and implement the abstract methods.
 *
 * For example, to animate an integer value:
 *
 * ```kotlin
 * class MyIntAnimation(private val myObject: MyObject) : AnimationKeyTime<MyObject, Int>(myObject)
 * {
 *     override fun getValue(animated: MyObject): Int = animated.value
 *
 *     override fun setValue(animated: MyObject, value: Int)
 *     {
 *         animated.value = value
 *     }
 *
 *     override fun interpolate(animated: MyObject,
 *                              beforeValue: Int, beforeCoefficient: Double,
 *                              afterValue: Int, afterCoefficient: Double)
 *     {
 *         animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toInt()
 *     }
 * }
 *
 * val animation = MyIntAnimation(myObject)
 * animation.addKeyTimeValue(0, 0)
 * animation.addKeyTimeValue(1000, 100)
 * ```
 *
 * @param O The type of the object to animate.
 * @param V The type of the value to animate.
 * @property animated The object to animate.
 * @constructor Create a new key time animation.
 */
abstract class AnimationKeyTime<O : Any, V : Any>(protected val animated : O) : Animation
{
    /** The sorted list of key times. */
    private val keyTimes = SortedArray<KeyTime<V>>(unique = true)

    /** The initial value of the animated object. */
    private lateinit var initialValue : V

    /**
     * Adds a key time value to the animation.
     *
     * A key time defines a value at a specific time, and an interpolation method to reach this value.
     *
     * @param timeMilliseconds The time in milliseconds from the start of the animation.
     * @param value The value at the given time.
     * @param interpolation The interpolation to use to reach this value.
     * @throws IllegalArgumentException if the time is negative.
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

    /**
     * Called at animation initialization.
     *
     * It stores the initial value of the animated object.
     */
    override fun initialization()
    {
        this.initialValue = this.getValue(this.animated)
    }

    /**
     * Animates the object by interpolating the value based on the current time.
     *
     * @param millisecondsSinceStarted The time since the animation started.
     * @return `true` if the animation is still playing, `false` otherwise.
     */
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
     * Gets the current value of the animated object.
     *
     * @param animated The object to get the value from.
     * @return The current value of the object.
     */
    protected abstract fun getValue(animated : O) : V

    /**
     * Sets the value of the animated object.
     *
     * @param animated The object to set the value to.
     * @param value The value to set.
     */
    protected abstract fun setValue(animated : O, value : V)

    /**
     * Interpolates the value between two keyframes and applies it to the animated object.
     *
     * The implementation should calculate the interpolated value and apply it to the animated object.
     *
     * For example, for an integer value:
     *
     * ```kotlin
     * override fun interpolate(animated: MyObject,
     *                          beforeValue: Int, beforeCoefficient: Double,
     *                          afterValue: Int, afterCoefficient: Double)
     * {
     *      val interpolated = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toInt()
     *      animated.setValue(interpolated)
     * }
     * ```
     *
     * @param animated The object to animate.
     * @param beforeValue The value of the previous keyframe.
     * @param beforeCoefficient The coefficient of the previous keyframe.
     * @param afterValue The value of the next keyframe.
     * @param afterCoefficient The coefficient of the next keyframe.
     */
    protected abstract fun interpolate(animated : O,
                                       beforeValue : V, beforeCoefficient : Double,
                                       afterValue : V, afterCoefficient : Double)
}
