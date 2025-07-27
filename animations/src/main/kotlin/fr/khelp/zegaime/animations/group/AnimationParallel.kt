package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.utils.collections.iterations.select

/**
 * Plays a set of animations in parallel.
 *
 * The parallel animation is finished when all its children animations are finished.
 *
 * To create a parallel animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = parallel {
 *      // Add animations here
 * }
 * ```
 *
 * Or create an instance of [AnimationParallel] and add animations to it:
 *
 * ```kotlin
 * val animation = AnimationParallel()
 * animation += myAnimation1
 * animation += myAnimation2
 * ```
 *
 * @constructor Create a new parallel animation.
 */
class AnimationParallel : Animation
{
    /** Indicates if the animation is currently playing */
    private var playing = false
    /** List of animations to play in parallel */
    private val animations = ArrayList<AnimationParallelElement>()

    /**
     * Adds an animation to the parallel group.
     *
     * If the parallel animation is already playing, the new animation is initialized and started immediately.
     *
     * @param animation The animation to add.
     */
    operator fun plusAssign(animation: Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(AnimationParallelElement(animation, this.playing))

            if (this.playing)
            {
                animation.initialization()
            }
        }
    }

    /**
     * Called at animation initialization.
     *
     * It initializes all the children animations.
     */
    override fun initialization()
    {
        synchronized(this.animations)
        {
            this.playing = true

            for (animationParallelElement in this.animations)
            {
                animationParallelElement.playing = true
                animationParallelElement.animation.initialization()
            }
        }
    }

    /**
     * Animates all the children animations.
     *
     * @param millisecondsSinceStarted Time since the animation started.
     * @return `true` if at least one child animation is still playing, `false` otherwise.
     */
    override fun animate(millisecondsSinceStarted: Long): Boolean
    {
        var stillPlaying = false

        synchronized(this.animations)
        {
            for (animationParallelElement in this.animations.select { element -> element.playing })
            {
                if (animationParallelElement.animation.animate(millisecondsSinceStarted))
                {
                    stillPlaying = true
                }
                else
                {
                    animationParallelElement.playing = false
                    animationParallelElement.animation.finalization()
                }
            }
        }

        return stillPlaying
    }

    /**
     * Called when the animation is finalized.
     *
     * It finalizes all the children animations that are still playing.
     */
    override fun finalization()
    {
        synchronized(this.animations)
        {
            this.playing = false

            for (animationParallelElement in this.animations.select { element -> element.playing })
            {
                animationParallelElement.playing = false
                animationParallelElement.animation.finalization()
            }
        }
    }
}
