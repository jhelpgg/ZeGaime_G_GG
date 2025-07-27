package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.AnimationDoesNothing
import fr.khelp.zegaime.utils.argumentCheck

/**
 * Play an animation in loop.
 *
 * A loop animation is composed of three parts:
 * 1. An optional header animation, played one time at the beginning.
 * 2. A looped animation, played a certain number of times.
 * 3. An optional footer animation, played one time at the end.
 *
 * The loop stops if the number of loops reaches the maximum defined, or by calling [stopLooping].
 *
 * To create a loop animation, you can use the DSL:
 *
 * ```kotlin
 * val animation = animationLoop {
 *      animationHeader = animationParallel {
 *          +animationAction{
 *             action = { println("Animation started !" } }
 *          }
 *          // ...
 *      }
 *
 *      animationLooped = animationSequential {
 *          // ....
 *      }
 *
 *      animationFooter = animationAction {
 *             action = { println("Animation finished !" } }*
 *      }
 *
 *      numberMaximumLoop = 5
 * }
 * ```
 *
 * Or create an instance of [AnimationLoop]:
 *
 * ```kotlin
 * val animation = AnimationLoop(animationLooped = myAnimation, numberMaximumLoop = 3)
 * ```
 *
 * @property animationLooped Animation played in loop.
 * @property animationHeader Optional animation played before looping. Does nothing by default.
 * @property animationFooter Optional animation played after looping. Does nothing by default.
 * @property numberMaximumLoop Number maximum of loop to do. By default, it is "infinite" (`Int.MAX_VALUE`).
 * @constructor Create a new loop animation.
 * @throws IllegalArgumentException if the number of loops is not strictly positive.
 */
class AnimationLoop(private val animationLooped : Animation,
                    private val animationHeader : Animation = AnimationDoesNothing,
                    private val animationFooter : Animation = AnimationDoesNothing,
                    private val numberMaximumLoop : Int = Int.MAX_VALUE) : Animation
{
    /** Current status of the loop animation */
    private var status = AnimationLoopStatus.IDLE

    /** Current loop count */
    private var loop = 0

    /** Reference time for the current animation part */
    private var referenceTime = 0L

    init
    {
        argumentCheck(this.numberMaximumLoop > 0) { "Number of loops must be >=1 not ${this.numberMaximumLoop}" }
    }

    /**
     * Manually stops the looping.
     *
     * - If the animation is in the header part, the loop animation will not be played. It goes directly to the footer after the header.
     * - If the animation is in the loop part, the current loop will finish, and then it passes to the footer.
     * - If the animation is in the footer part or not playing, this method does nothing.
     */
    fun stopLooping()
    {
        this.loop = this.numberMaximumLoop
    }

    /**
     * Called at animation initialization.
     *
     * It resets the loop count and reference time, and initializes the header animation.
     */
    override fun initialization()
    {
        this.loop = 0
        this.referenceTime = 0L
        this.status = AnimationLoopStatus.HEADER
        this.animationHeader.initialization()
    }

    /**
     * Animate the current part of the loop.
     *
     * It plays the header, then the looped animation the specified number of times, and finally the footer.
     *
     * @param millisecondsSinceStarted Time since the animation started.
     * @return `true` if the animation must continue, `false` otherwise.
     */
    override fun animate(millisecondsSinceStarted : Long) : Boolean
    {
        var stillAnimated = true

        when (this.status)
        {
            AnimationLoopStatus.IDLE   -> stillAnimated = false

            AnimationLoopStatus.HEADER ->
                if (!this.animationHeader.animate(millisecondsSinceStarted - this.referenceTime))
                {
                    this.animationHeader.finalization()
                    this.status = AnimationLoopStatus.LOOP
                    this.animationLooped.initialization()
                    this.referenceTime = millisecondsSinceStarted
                }

            AnimationLoopStatus.LOOP   ->
                if (!this.animationLooped.animate(millisecondsSinceStarted - this.referenceTime))
                {
                    this.animationLooped.finalization()
                    this.loop++

                    if (this.loop < this.numberMaximumLoop)
                    {
                        this.animationLooped.initialization()
                    }
                    else
                    {
                        this.status = AnimationLoopStatus.FOOTER
                        this.animationFooter.initialization()
                    }

                    this.referenceTime = millisecondsSinceStarted
                }

            AnimationLoopStatus.FOOTER ->
                if (!this.animationFooter.animate(millisecondsSinceStarted - this.referenceTime))
                {
                    this.animationFooter.finalization()
                    this.status = AnimationLoopStatus.IDLE
                    stillAnimated = false
                }
        }

        return stillAnimated
    }

    /**
     * Called when the animation is finalized.
     *
     * It finalizes the current animation part and sets the status to idle.
     */
    override fun finalization()
    {
        when (this.status)
        {
            AnimationLoopStatus.IDLE   -> Unit
            AnimationLoopStatus.HEADER -> this.animationHeader.finalization()
            AnimationLoopStatus.LOOP   -> this.animationLooped.finalization()
            AnimationLoopStatus.FOOTER -> this.animationFooter.finalization()
        }

        this.status = AnimationLoopStatus.IDLE
    }
}
