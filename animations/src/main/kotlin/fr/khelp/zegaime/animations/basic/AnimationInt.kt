package fr.khelp.zegaime.animations.basic

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Animation of Int value
 */
class AnimationInt(private val initialValue : Int = 0)
    : AnimationKeyTime<ObservableSource<Int>, Int>(ObservableSource<Int>(initialValue))
{
    val value : Observable<Int> = this.animated.observable

    override fun initialization()
    {
        this.animated.value = this.initialValue
        super.initialization()
    }

    override fun getValue(animated : ObservableSource<Int>) : Int = animated.value

    override fun setValue(animated : ObservableSource<Int>, value : Int)
    {
        animated.value = value
    }

    override fun interpolate(animated : ObservableSource<Int>,
                             beforeValue : Int,
                             beforeCoefficient : Double,
                             afterValue : Int,
                             afterCoefficient : Double)
    {
        animated.value = (beforeValue * beforeCoefficient + afterValue * afterCoefficient).toInt()
    }
}