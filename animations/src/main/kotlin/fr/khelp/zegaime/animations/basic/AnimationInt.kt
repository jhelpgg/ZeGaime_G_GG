package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of an [Int] value.
 *
 * The value is observable, so you can react to its changes.
 *
 * To create an int animation, you can use the DSL:
 *
 * ```kotlin
 * val animation =  animationInt(0) {
 *        1.at(0L)
 *        5.at(100L, InterpolationSine)
 *        2.at(200L)
 *        8.at(500L, InterpolationHesitate)
 *   }
 * ```
 *
 * Or create an instance of [AnimationInt] and add frames to it:
 *
 * ```kotlin
 * val animation = AnimationInt()
 * animation.addKeyTimeValue(0L, 1)
 * animation.addKeyTimeValue(100L, 5, InterpolationSine)
 * animation.addKeyTimeValue(250L, 2)
 * animation.addKeyTimeValue(500L, 8, InterpolationHesitate)
 * ```
 *
 * To observe the value changes:
 *
 * ```kotlin
 * animation.value.register { int ->
 *      println("Value changed: $int")
 * }
 * ```
 *
 * @property initialValue Initial value of the animation.
 * @constructor Create a new animation of [Int].
 */
class AnimationInt(private val initialValue : Int = 0) :
    AnimationKeyTime<ObservableSource<Int>, Int>(ObservableSource(initialValue))
{
    /** Observable value of the animation */
    val value : Observable<Int> = this.animated.observable

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
    override fun getValue(animated : ObservableSource<Int>) : Int = animated.value

    /**
     * Set the current value of the animation.
     *
     * @param animated The animated object.
     * @param value The new value.
     */
    override fun setValue(animated : ObservableSource<Int>, value : Int)
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
    override fun interpolate(animated : ObservableSource<Int>,
                             beforeValue : Int,
                             beforeCoefficient : Double,
                             afterValue : Int,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toInt()
    }
}
