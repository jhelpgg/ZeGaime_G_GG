package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.AnimationDoesNothing
import fr.khelp.zegaime.utils.argumentCheck

/**
 * Play an animation in loop.
 *
 * Loop stops if number of loops reaches the maximum or by call [stopLooping]
 *
 * @property animationLooped Animation played in loop
 * @property animationHeader Optional animation plays before looping. Does nothing by default
 * @property animationFooter Optional animation plays after looping. Does nothing by default
 * @property numberMaximumLoop Number maximum of loop to do. By default, "infinite"
 */
class AnimationLoop(private val animationLooped : Animation,
                    private val animationHeader : Animation = AnimationDoesNothing,
                    private val animationFooter : Animation = AnimationDoesNothing,
                    private val numberMaximumLoop : Int = Int.MAX_VALUE) : Animation
{
    /** Current status */
    private var status = AnimationLoopStatus.IDLE

    /** Current loop count */
    private var loop = 0

    /** Reference time */
    private var referenceTime = 0L

    init
    {
        argumentCheck(this.numberMaximumLoop > 0) { "Number of loops must be >=1 not ${this.numberMaximumLoop}" }
    }

    /**
     * Manually stops the looping
     *
     * * If we are in header, the loop animation will not play. We directly go to the footer after the header
     * * If we are in loop, the loop animation will finish and pass to footer
     * * If we are in footer or not playing, it does nothing
     */
    fun stopLooping()
    {
        this.loop = this.numberMaximumLoop
    }

    override fun initialization()
    {
        this.loop = 0
        this.referenceTime = 0L
        this.status = AnimationLoopStatus.HEADER
        this.animationHeader.initialization()
    }

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