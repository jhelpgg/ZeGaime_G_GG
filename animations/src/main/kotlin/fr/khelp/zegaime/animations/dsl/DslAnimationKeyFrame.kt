package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.animations.keytime.AnimationKeyTime

@AnimationDSL
abstract class DslAnimationKeyFrame<O : Any, V : Any>(protected val animationKeyTime : AnimationKeyTime<O,V>)
{
    @KeyTimeDSL
    fun V.at(timeMilliseconds : Long, interpolation : Interpolation = InterpolationLinear)
    {
        this@DslAnimationKeyFrame.animationKeyTime.addKeyTimeValue(timeMilliseconds, this, interpolation)
    }
}

