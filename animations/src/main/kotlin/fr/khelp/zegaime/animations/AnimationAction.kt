package fr.khelp.zegaime.animations

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.parallel

/**
 * Animation that launches an action when it's played.
 *
 * The action is launched in parallel, so it doesn't block the animation flow.
 *
 * This animation is designed to be used with [fr.khelp.zegaime.animations.group.AnimationParallel],
 * [fr.khelp.zegaime.animations.group.AnimationsSequential] or [fr.khelp.zegaime.animations.group.AnimationLoop].
 *
 * To create an animation action, we can use the DSL:
 *
 * ```kotlin
 * val animation = sequential {
 *    // ...
 *    action { println("Hello") }
 *    // ...
 * }
 * ```
 *
 * Or use directly the constructor:
 *
 * ```kotlin
 * val animation = AnimationAction { println("Hello") }
 * ```
 *
 * @property action Action to play.
 * @property taskContext Context where launch the action. By default, it uses an independent thread.
 * @constructor Create the animation action.
 */
class AnimationAction(private val action: () -> Unit,
                      private val taskContext: TaskContext = TaskContext.INDEPENDENT) : Animation
{
    /**
     * Launch the action in parallel and indicates that the animation is finished.
     *
     * @param millisecondsSinceStarted Time since animation started. Not used here.
     * @return `false` to indicate that the animation is finished.
     */
    override fun animate(millisecondsSinceStarted: Long): Boolean
    {
        this::action.parallel(this.taskContext)
        return false
    }
}