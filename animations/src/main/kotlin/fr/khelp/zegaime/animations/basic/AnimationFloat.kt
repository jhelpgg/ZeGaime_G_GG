package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of a [Float] value.
 *
 * The value is observable, so you can react to its changes.
 *
 * To create a float animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = keyFrame<Float> {
 *      frame(0, 1f)
 *      frame(100, 5f)
 *      frame(250, 2f)
 *      frame(500, 8f)
 * }
 * ```
 *
 * Or create an instance of [AnimationFloat] and add frames to it:
 *
 * ```kotlin
 * val animation = AnimationFloat()
 * animation.frame(0, 1f)
 * animation.frame(100, 5f)
 * animation.frame(250, 2f)
 * animation.frame(500, 8f)
 * ```
 *
 * To observe the value changes:
 *
 * ```kotlin
 * animation.value.observedBy { float ->
 *      println("Value changed: $float")
 * }
 * ```
 *
 * @property initialValue Initial value of the animation.
 * @constructor Create a new animation of [Float].
 */
class AnimationFloat(private val initialValue: Float = 0f) :
    AnimationKeyTime<ObservableSource<Float>, Float>(ObservableSource(initialValue))
{
    /** Observable value of the animation */
    val value: Observable<Float> = this.animated.observable

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
    override fun getValue(animated: ObservableSource<Float>): Float = animated.value

    /**
     * Set the current value of the animation.
     *
     * @param animated The animated object.
     * @param value The new value.
     */
    override fun setValue(animated: ObservableSource<Float>, value: Float)
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
    override fun interpolate(animated: ObservableSource<Float>,
                             beforeValue: Float,
                             beforeCoefficient: Double,
                             afterValue: Float,
                             afterCoefficient: Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toFloat()
    }
}
