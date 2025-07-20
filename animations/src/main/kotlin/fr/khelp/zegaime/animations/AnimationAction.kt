package fr.khelp.zegaime.animations

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.parallel

/**
 * Animation that launches an action when it's played.
 *
 * Design to be used with [fr.khelp.zegaime.animations.group.AnimationParallel],
 * [fr.khelp.zegaime.animations.group.AnimationsSequential] or [fr.khelp.zegaime.animations.group.AnimationLoop]
 *
 * @property action Action to play
 * @property taskContext Context where launch the action. By default, it uses an independent thread
 */
class AnimationAction(private val action : () -> Unit,
                      private val taskContext : TaskContext = TaskContext.INDEPENDENT) : Animation
{
    override fun animate(millisecondsSinceStarted : Long) : Boolean
    {
        this::action.parallel(this.taskContext)
        return false
    }
}
