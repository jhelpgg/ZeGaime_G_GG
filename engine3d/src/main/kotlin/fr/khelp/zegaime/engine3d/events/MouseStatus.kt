package fr.khelp.zegaime.engine3d.events

/**
 * Represents the status of the mouse.
 */
enum class MouseStatus
{
    /**
     * The mouse is not moving.
     */
    STAY,

    /**
     * The mouse is outside the 3D window.
     */
    OUTSIDE,

    /**
     * The mouse is moving.
     */
    MOVE,

    /**
     * The mouse is being dragged.
     */
    DRAG,

    ENTER,

    EXIT
}