package fr.khelp.zegaime.animations.group

/**
 * Status of an [AnimationLoop].
 *
 * This enum is for internal use of the animation system.
 */
internal enum class AnimationLoopStatus
{
    /**
     * The animation is not playing.
     */
    IDLE,

    /**
     * The animation is playing the header part.
     */
    HEADER,

    /**
     * The animation is playing the loop part.
     */
    LOOP,

    /**
     * The animation is playing the footer part.
     */
    FOOTER
}
