package fr.khelp.zegaime.animations

/**
 * An animation that does nothing and finishes immediately.
 *
 * It is designed to be a header and/or footer of [fr.khelp.zegaime.animations.group.AnimationLoop].
 * For example, if we want a loop that does nothing at the beginning, but something at each loop and at the end:
 *
 * ```kotlin
 * val animation = loop(start = AnimationDoesNothing,
 *                      loop = AnimationAction { println("Loop") },
 *                      end = AnimationAction { println("End") })
 * ```
 */
object AnimationDoesNothing : Animation
{
    /**
     * Does nothing and returns `false` to indicate that the animation is finished.
     *
     * @param millisecondsSinceStarted Time since animation started. Not used here.
     * @return `false` to indicate that the animation is finished.
     */
    override fun animate(millisecondsSinceStarted: Long): Boolean = false
}
