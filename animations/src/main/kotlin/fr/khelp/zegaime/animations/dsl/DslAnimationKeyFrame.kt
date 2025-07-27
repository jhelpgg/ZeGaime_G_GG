package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.animations.keytime.AnimationKeyTime

/**
 * Generic [AnimationKeyTime] creator, for a DSL way of the class that's extends [AnimationKeyTime]
 *
 * @param animationKeyTime [AnimationKeyTime] implementation to add key times
 */
@AnimationDSL
abstract class DslAnimationKeyFrame<O : Any, V : Any>(protected val animationKeyTime : AnimationKeyTime<O, V>)
{
    /**
     * Associate to a value the time it should appear and the interpolation to use for reach the value
     *
     * @param timeMilliseconds Time after animation started to take the value
     * @param interpolation Interpolation to use for reach the value
     */
    @KeyTimeDSL
    fun V.at(timeMilliseconds : Long, interpolation : Interpolation = InterpolationLinear)
    {
        this@DslAnimationKeyFrame.animationKeyTime.addKeyTimeValue(timeMilliseconds, this, interpolation)
    }
}

