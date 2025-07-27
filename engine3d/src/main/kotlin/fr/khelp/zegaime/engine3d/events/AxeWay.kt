package fr.khelp.zegaime.engine3d.events

/**
 * Represents the way of a joystick axis.
 *
 * This enum is for internal use of the event system.
 */
internal enum class AxeWay
{
    /**
     * The axis is in the negative direction.
     */
    NEGATIVE,

    /**
     * The axis is in the neutral position.
     */
    NEUTRAL,

    /**
     * The axis is in the positive direction.
     */
    POSITIVE
}
