package fr.khelp.zegaime.animations

/**
 * Generic animation
 */
interface Animation
{
    /**
     * Called each time animation is started a zero.
     *
     * Opportunity to initialize things
     *
     * Does nothing by default
     */
    @AnimationTask
    fun initialization()
    {
    }

    /**
     * Called when need to refresh animation
     *
     * @param millisecondsSinceStarted Number of milliseconds since animation started
     *
     * @return `true` if animation should continue. `false` if animation reaches its end
     */
    @AnimationTask
    fun animate(millisecondsSinceStarted : Long) : Boolean

    /**
     * Called when animation stopped for any reason
     *
     * Opportunity to properly close things opened in [initialization]
     *
     * Does nothing by default
     */
    @AnimationTask
    fun finalization()
    {
    }
}