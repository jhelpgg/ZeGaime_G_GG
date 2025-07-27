package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation

/**
 * Plays a set of animations in sequential order.
 *
 * The sequential animation is finished when all its children animations are finished.
 *
 * To create a sequential animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = animationsSequential {
 *      + animationAction {
 *          // ...
 *      }
 *
 *      + animationDouble(42.73) {
 *          // ...
 *      }
 *
 *      // ....
 * }
 * ```
 *
 * Or create an instance of [AnimationsSequential] and add animations to it:
 *
 * ```kotlin
 * val animation = AnimationsSequential()
 * animation += myAnimation1
 * animation += myAnimation2
 * ```
 *
 * @constructor Create a new sequential animation.
 */
class AnimationsSequential : Animation
{
    /** List of animations to play in sequence */
    private val animations = ArrayList<Animation>()

    /** Start time of the current animation */
    private var currentAnimationStartTime = 0L

    /** Index of the current animation */
    private var animationIndex = 0

    /**
     * Adds an animation to the sequence.
     *
     * @param animation The animation to add.
     */
    operator fun plusAssign(animation : Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
        }
    }

    /**
     * Called at animation initialization.
     *
     * It initializes the first animation of the sequence.
     */
    override fun initialization()
    {
        this.animationIndex = 0
        this.currentAnimationStartTime = 0L

        synchronized(this.animations)
        {
            if (this.animations.isNotEmpty())
            {
                this.animations[0].initialization()
            }
        }
    }

    /**
     * Animates the current animation of the sequence.
     *
     * When the current animation is finished, it passes to the next one.
     *
     * @param millisecondsSinceStarted Time since the animation started.
     * @return `true` if there are still animations to play, `false` otherwise.
     */
    override fun animate(millisecondsSinceStarted : Long) : Boolean
    {
        synchronized(this.animations)
        {
            if (this.animationIndex >= this.animations.size)
            {
                return false
            }

            if (!this.animations[this.animationIndex].animate(millisecondsSinceStarted - this.currentAnimationStartTime))
            {
                this.animations[this.animationIndex].finalization()
                this.animationIndex++

                if (this.animationIndex >= this.animations.size)
                {
                    return false
                }

                this.currentAnimationStartTime = millisecondsSinceStarted
                this.animations[this.animationIndex].initialization()
            }
        }

        return true
    }

    /**
     * Called when the animation is finalized.
     *
     * It finalizes the current animation if it is still playing.
     */
    override fun finalization()
    {
        synchronized(this.animations)
        {
            if (this.animationIndex < this.animations.size)
            {
                this.animations[this.animationIndex].finalization()
            }
        }
    }
}
