package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of Long value
 */
class AnimationLong(private val initialValue : Long = 0L)
    : AnimationKeyTime<ObservableSource<Long>, Long>(ObservableSource<Long>(initialValue))
{
    val value : Observable<Long> = this.animated.observable

    override fun initialization()
    {
        this.animated.value = this.initialValue
        super.initialization()
    }

    override fun getValue(animated : ObservableSource<Long>) : Long = animated.value

    override fun setValue(animated : ObservableSource<Long>, value : Long)
    {
        animated.value = value
    }

    override fun interpolate(animated : ObservableSource<Long>,
                             beforeValue : Long,
                             beforeCoefficient : Double,
                             afterValue : Long,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toLong()
    }
}