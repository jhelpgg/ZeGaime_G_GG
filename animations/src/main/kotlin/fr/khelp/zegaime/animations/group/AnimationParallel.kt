package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.utils.collections.iterations.select

/**
 * Play animations in parallel
 */
class AnimationParallel : Animation
{
    /** Whether animation is playing */
    private var playing = false

    /** Animations to play */
    private val animations = ArrayList<AnimationParallelElement>()

    /**
     * Add an animation to play
     */
    operator fun plusAssign(animation : Animation)
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

    override fun initialization()
    {
        synchronized(this.animations)
        {
            this.playing = true

            for (animationParallelElement in this.animations)
            {
                animationParallelElement.playing = true
                animationParallelElement.animation.initialization()
                animationParallelElement.animation.animate(0L)
            }
        }
    }

    override fun animate(millisecondsSinceStarted : Long) : Boolean
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