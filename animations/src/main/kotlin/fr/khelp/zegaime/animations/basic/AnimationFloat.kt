package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of Float value
 *
 * @param initialValue Initial value
 */
class AnimationFloat(private val initialValue : Float = 0f)
    : AnimationKeyTime<ObservableSource<Float>, Float>(ObservableSource<Float>(initialValue))
{
    val value : Observable<Float> = this.animated.observable

    override fun initialization()
    {
        this.animated.value = this.initialValue
        super.initialization()
    }

    override fun getValue(animated : ObservableSource<Float>) : Float = animated.value

    override fun setValue(animated : ObservableSource<Float>, value : Float)
    {
        animated.value = value
    }

    override fun interpolate(animated : ObservableSource<Float>,
                             beforeValue : Float,
                             beforeCoefficient : Double,
                             afterValue : Float,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toFloat()
    }
}