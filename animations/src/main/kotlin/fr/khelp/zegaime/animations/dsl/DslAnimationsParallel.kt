package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.group.AnimationParallel
import fr.khelp.zegaime.animations.group.AnimationsSequential

@AnimationDSL
fun animationParallel(create : AnimationParallelCreator.() -> Unit) : AnimationParallel
{
    val animationsSequentialCreator = AnimationParallelCreator()
    animationsSequentialCreator.create()
    return animationsSequentialCreator.animationParallel
}

@AnimationDSL
class AnimationParallelCreator
{
    internal val animationParallel = AnimationParallel()

    operator fun Animation.unaryPlus()
    {
        this@AnimationParallelCreator.animationParallel += this
    }
}