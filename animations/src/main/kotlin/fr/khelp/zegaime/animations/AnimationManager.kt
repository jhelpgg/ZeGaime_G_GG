package fr.khelp.zegaime.animations

import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.sleep
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Animations manager that plays animations.
 *
 * It is a singleton that manages all animations of the application.
 *
 * To play an animation, just call the [play] method.
 *
 * For example:
 *
 * ```kotlin
 * fun main(args: Array<String>)
 * {
 *     val animation = // Create an animation
 *     AnimationManager.play(animation)
 * }
 * ```
 */
object AnimationManager
{
    /** Currently playing animations */
    private val animations = ArrayList<AnimationElement>()

    /** Indicates if the animation thread loop is running */
    private val animationsPlaying = AtomicBoolean(false)

    /**
     * Plays an animation.
     *
     * If the animation is already playing, this method does nothing.
     *
     * When an animation is played, its [Animation.initialization] is called.
     * Then its [Animation.animate] is called regularly until it returns `false`.
     * Finally, its [Animation.finalization] is called.
     *
     * @param animation Animation to play.
     */
    fun play(animation: Animation)
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
     * Stops an animation.
     *
     * If the animation is not playing, this method does nothing.
     *
     * When an animation is stopped, its [Animation.finalization] is called.
     *
     * @param animation Animation to stop.
     */
    fun stop(animation: Animation)
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
     * The animation thread loop.
     *
     * This method is launched in a parallel task when the first animation is played.
     * It loops as long as there are animations to play.
     *
     * At each loop, it calls the [Animation.animate] method of each playing animation.
     * If the [Animation.animate] method returns `false`, the animation is removed from the playing list
     * and its [Animation.finalization] method is called.
     *
     * When there are no more animations to play, the loop stops.
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
