package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of a [Long] value.
 *
 * The value is observable, so you can react to its changes.
 *
 * To create a long animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = animationLong(0L) {
 *        1L.at(0L)
 *        5L.at(100L, InterpolationSine)
 *        2L.at(200L)
 *        8L.at(500L, InterpolationHesitate)
 *   }
 * ```
 *
 * Or create an instance of [AnimationLong] and add frames to it:
 *
 * ```kotlin
 * val animation = AnimationLong()
 * animation.addKeyTimeValue(0L, 1L)
 * animation.addKeyTimeValue(100L, 5L, InterpolationSine)
 * animation.addKeyTimeValue(250L, 2L)
 * animation.addKeyTimeValue(500L, 8L, InterpolationHesitate)
 * ```
 *
 * To observe the value changes:
 *
 * ```kotlin
 * animation.value.register { long ->
 *      println("Value changed: $long")
 * }
 * ```
 *
 * @property initialValue Initial value of the animation.
 * @constructor Create a new animation of [Long].
 */
class AnimationLong(private val initialValue : Long = 0L) :
    AnimationKeyTime<ObservableSource<Long>, Long>(ObservableSource(initialValue))
{
    /** Observable value of the animation */
    val value : Observable<Long> = this.animated.observable

    /**
     * Called at animation initialization.
     *
     * It sets the animated value to the initial value.
     */
    override fun initialization()
    {
        this.animated.value = this.initialValue
        super.initialization()
    }

    /**
     * Get the current value of the animation.
     *
     * @param animated The animated object.
     * @return The current value.
     */
    override fun getValue(animated : ObservableSource<Long>) : Long = animated.value

    /**
     * Set the current value of the animation.
     *
     * @param animated The animated object.
     * @param value The new value.
     */
    override fun setValue(animated : ObservableSource<Long>, value : Long)
    {
        animated.value = value
    }

    /**
     * Interpolate the value between two frames.
     *
     * @param animated The animated object.
     * @param beforeValue The value of the previous frame.
     * @param beforeCoefficient The coefficient of the previous frame.
     * @param afterValue The value of the next frame.
     * @param afterCoefficient The coefficient of the next frame.
     */
    override fun interpolate(animated : ObservableSource<Long>,
                             beforeValue : Long,
                             beforeCoefficient : Double,
                             afterValue : Long,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toLong()
    }
}
