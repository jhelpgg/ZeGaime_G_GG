package fr.khelp.zegaime.animations

/**
 * Does nothing animation
 *
 * Design to be a header and/or footer of [fr.khelp.zegaime.animations.group.AnimationLoop]
 */
object AnimationDoesNothing : Animation
{
    override fun animate(millisecondsSinceStarted : Long) : Boolean = false
}