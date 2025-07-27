package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of a [Double] value.
 *
 * The value is observable, so you can react to its changes.
 *
 * To create a double animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = animationDouble(0.0) {
 *      1.0.at(0L)
 *      5.0.at(100L, InterpolationSine)
 *      2.0.at(200L)
 *      8.0.at(500L, InterpolationHesitate)
 * }
 * ```
 *
 * Or create an instance of [AnimationDouble] and add frames to it:
 *
 * ```kotlin
 * val animation = AnimationDouble()
 * animation.addKeyTimeValue(0L, 1.0)
 * animation.addKeyTimeValue(100L, 5.0, InterpolationSine)
 * animation.addKeyTimeValue(250L, 2.0)
 * animation.addKeyTimeValue(500L, 8.0, InterpolationHesitate)
 * ```
 *
 * To observe the value changes:
 *
 * ```kotlin
 * animation.value.register { double ->
 *      println("Value changed: $double")
 * }
 * ```
 *
 * @property initialValue Initial value of the animation.
 * @constructor Create a new animation of [Double].
 */
class AnimationDouble(private val initialValue : Double = 0.0) :
    AnimationKeyTime<ObservableSource<Double>, Double>(ObservableSource<Double>(initialValue))
{
    /** Observable value of the animation */
    val value : Observable<Double> = this.animated.observable

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
    override fun getValue(animated : ObservableSource<Double>) : Double = animated.value

    /**
     * Set the current value of the animation.
     *
     * @param animated The animated object.
     * @param value The new value.
     */
    override fun setValue(animated : ObservableSource<Double>, value : Double)
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
    override fun interpolate(animated : ObservableSource<Double>,
                             beforeValue : Double,
                             beforeCoefficient : Double,
                             afterValue : Double,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient)
    }
}
