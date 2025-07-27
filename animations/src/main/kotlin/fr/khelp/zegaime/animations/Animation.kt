package fr.khelp.zegaime.animations

/**
 * Generic animation.
 *
 * An animation is a task that is played by an [AnimationManager] during a certain time.
 *
 * The animation life cycle is:
 * 1. [initialization] is called one time at the beginning.
 * 2. [animate] is called regularly to update the animation state.
 * 3. [finalization] is called one time when animation is finished.
 *
 * Here a simple example of a custom animation that print the progress in console:
 *
 * ```kotlin
 * class PrintAnimation(private val duration: Long) : Animation
 * {
 *     override fun animate(millisecondsSinceStarted: Long): Boolean
 *     {
 *         println("Progress : ${millisecondsSinceStarted * 100 / this.duration}%")
 *         return millisecondsSinceStarted < this.duration
 *     }
 * }
 *
 * fun main(args: Array<String>)
 * {
 *     val animationManager = AnimationManager()
 *     animationManager.play(PrintAnimation(1000))
 * }
 * ```
 */
interface Animation
{
    /**
     * Called each time animation is started at time zero.
     *
     * It is an opportunity to initialize things before animation start.
     *
     * For example, if an animation need to create a temporary file, it can be done here.
     *
     * Does nothing by default
     */
    @AnimationTask
    fun initialization()
    {
    }

    /**
     * Called regularly to refresh the animation state.
     *
     * The given parameter is the number of milliseconds since the animation started.
     *
     * The method must return `true` if the animation have to continue, `false` if the animation is finished.
     *
     * For example, for an animation that lasts 1 second:
     *
     * ```kotlin
     * override fun animate(millisecondsSinceStarted: Long): Boolean
     * {
     *     // Do animation stuff here
     *     return millisecondsSinceStarted < 1000
     * }
     * ```
     *
     * @param millisecondsSinceStarted Number of milliseconds since animation started
     * @return `true` if animation should continue. `false` if animation reaches its end
     */
    @AnimationTask
    fun animate(millisecondsSinceStarted: Long): Boolean

    /**
     * Called when animation stopped for any reason (finished, canceled, ...).
     *
     * It is an opportunity to properly close things opened in [initialization].
     *
     * For example, if a temporary file was created in [initialization], it can be deleted here.
     *
     * Does nothing by default
     */
    @AnimationTask
    fun finalization()
    {
    }
}
