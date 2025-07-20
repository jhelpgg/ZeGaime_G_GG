package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation

/**
 * Animations played in sequential order
 */
class AnimationsSequential : Animation
{
    /** Animations to play */
    private val animations = ArrayList<Animation>()

    /** Current animation start relative time */
    private var currentAnimationStartTime = 0L

    /** Current animation index */
    private var animationIndex = 0

    /**
     * Add an animation to the sequence
     */
    operator fun plusAssign(animation : Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
        }
    }

    override fun initialization()
    {
        this.animationIndex = 0
        this.currentAnimationStartTime = 0L

        synchronized(this.animations)
        {
            if (this.animations.isNotEmpty())
            {
                this.animations[0].initialization()
                this.animations[0].animate(0L)
            }
        }
    }

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
                this.animations[this.animationIndex].animate(0L)
            }
        }

        return true
    }

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