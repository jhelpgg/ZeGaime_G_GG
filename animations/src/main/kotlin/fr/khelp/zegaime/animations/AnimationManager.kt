package fr.khelp.zegaime.animations

import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.sleep
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Animations manager that plays animations
 */
object AnimationManager
{
    /** Currents animations */
    private val animations = ArrayList<AnimationElement>()

    /** Whether the animation thread loop is running */
    private val animationsPlaying = AtomicBoolean(false)

    /**
     * Plays an animation
     *
     * @param animation Animation to play
     */
    fun play(animation : Animation)
    {
        synchronized(this.animations)
        {
            if (this.animations.none { (animationStored, _) -> animationStored == animation })
            {
                animation.initialization()
                this.animations.add(AnimationElement(animation))

                if (this.animationsPlaying.compareAndSet(false, true))
                {
                    this::playing.parallel()
                }
            }
        }
    }

    /**
     * Stops an animation
     *
     * @property animation Animation to stop
     */
    fun stop(animation : Animation)
    {
        synchronized(this.animations)
        {
            if (this.animations.removeIf { (animationStored, _) -> animationStored == animation })
            {
                animation.finalization()
            }
        }
    }

    /**
     * Animation thread loop
     */
    @AnimationTask
    private suspend fun playing()
    {
        while (this.animationsPlaying.get())
        {
            synchronized(this.animations)
            {
                if (this.animations.isEmpty())
                {
                    this.animationsPlaying.set(false)
                    return
                }

                val iterator = this.animations.iterator()

                while (iterator.hasNext())
                {
                    val animationElement = iterator.next()

                    if (!animationElement.animation.animate(System.currentTimeMillis() - animationElement.startTimeInMilliseconds))
                    {
                        animationElement.animation.finalization()
                        iterator.remove()
                    }
                }
            }

            sleep(32)
        }
    }
}