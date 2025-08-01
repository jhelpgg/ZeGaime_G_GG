package fr.khelp.zegaime.engine3d.events

/**
 * Joystick event type.
 */
enum class JoystickInputType
{
    /**
     * Axis with positive value.
     */
    AXIS_POSITIVE,

    /**
     * Axis with negative value.
     */
    AXIS_NEGATIVE,

    /**
     * Joystick button.
     */
    BUTTON,

    /**
     * Undefined type.
     */
    NONE
}
