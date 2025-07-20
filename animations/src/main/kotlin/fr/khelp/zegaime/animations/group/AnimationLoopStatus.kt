package fr.khelp.zegaime.animations.group

/**
 * Animation loop status
 */
internal enum class AnimationLoopStatus
{
    /** When does nothing */
    IDLE,

    /** When plays the header */
    HEADER,

    /** When plays the loop animation */
    LOOP,

    /** When plays the footer */
    FOOTER
}