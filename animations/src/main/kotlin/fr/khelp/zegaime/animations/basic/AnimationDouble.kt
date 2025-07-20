package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of Double value
 *
 * @property initialValue Initial value
 */
class AnimationDouble(private val initialValue : Double = 0.0)
    : AnimationKeyTime<ObservableSource<Double>, Double>(ObservableSource<Double>(initialValue))
{
    val value : Observable<Double> = this.animated.observable

    override fun initialization()
    {
        this.animated.value = this.initialValue
        super.initialization()
    }

    override fun getValue(animated : ObservableSource<Double>) : Double = animated.value

    override fun setValue(animated : ObservableSource<Double>, value : Double)
    {
        animated.value = value
    }

    override fun interpolate(animated : ObservableSource<Double>,
                             beforeValue : Double,
                             beforeCoefficient : Double,
                             afterValue : Double,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toDouble()
    }
}