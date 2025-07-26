package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.AnimationDoesNothing
import fr.khelp.zegaime.animations.group.AnimationLoop

@AnimationDSL
fun animationLoop(create : AnimationLoopCreator.() -> Unit) : AnimationLoop
{
    val animationLoopCreator = AnimationLoopCreator()
    animationLoopCreator.create()
    return animationLoopCreator()
}

@AnimationDSL
class AnimationLoopCreator
{
    var animationHeader : Animation = AnimationDoesNothing

    var animationLooped : Animation = AnimationDoesNothing

    var animationFooter : Animation = AnimationDoesNothing

    var numberMaximumLoop : Int = Int.MAX_VALUE

    internal operator fun invoke() : AnimationLoop =
        AnimationLoop(this.animationLooped, this.animationHeader, this.animationFooter, this.numberMaximumLoop)
}