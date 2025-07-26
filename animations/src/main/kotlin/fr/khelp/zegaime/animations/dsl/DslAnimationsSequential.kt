package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.group.AnimationsSequential

@AnimationDSL
fun animationsSequential(create : AnimationsSequentialCreator.() -> Unit) : AnimationsSequential
{
    val animationsSequentialCreator = AnimationsSequentialCreator()
    animationsSequentialCreator.create()
    return animationsSequentialCreator.animationsSequential
}

@AnimationDSL
class AnimationsSequentialCreator
{
    internal val animationsSequential = AnimationsSequential()

    operator fun Animation.unaryPlus()
    {
        this@AnimationsSequentialCreator.animationsSequential += this
    }
}