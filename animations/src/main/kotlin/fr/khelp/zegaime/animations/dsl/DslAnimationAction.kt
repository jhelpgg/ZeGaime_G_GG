package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.AnimationAction
import fr.khelp.zegaime.utils.tasks.TaskContext

@AnimationDSL
fun animationAction(create : AnimationActionCreator.() -> Unit) : AnimationAction
{
    val animationActionCreator = AnimationActionCreator()
    animationActionCreator.create()
    return animationActionCreator()
}

@AnimationDSL
class AnimationActionCreator
{
    var action : () -> Unit = {}
    var taskContext = TaskContext.INDEPENDENT

    internal operator fun invoke() : AnimationAction =
        AnimationAction(this.action, this.taskContext)
}