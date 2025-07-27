package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.AnimationAction
import fr.khelp.zegaime.utils.tasks.TaskContext

/**
 * Creates an animation that launches an action in DSL way
 *
 * ```kotlin
 * val animationPrint = animationAction {
 *      action = { println("Action played !") }
 * }
 *
 * val animationLoad = animationAction {
 *      action = {  loadObj() }
 *      taskContext = TaskContext.FILE
 * }
 * ```
 *
 * @param create The animation creation
 *
 * @return Created animation
 */
@AnimationDSL
fun animationAction(create : AnimationActionCreator.() -> Unit) : AnimationAction
{
    val animationActionCreator = AnimationActionCreator()
    animationActionCreator.create()
    return animationActionCreator()
}

/**
 * Creator of [AnimationAction]
 */
@AnimationDSL
class AnimationActionCreator
{
    /** Action to play */
    var action : () -> Unit = {}

    /** Task context to use for launch the action */
    var taskContext = TaskContext.INDEPENDENT

    /**
     * Creates the animation
     *
     * @return Created animation
     */
    internal operator fun invoke() : AnimationAction =
        AnimationAction(this.action, this.taskContext)
}