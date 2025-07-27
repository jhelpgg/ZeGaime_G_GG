package fr.khelp.zegaime.engine3d.events

/**
 * Defines the limits of a joystick axis.
 *
 * This class is for internal use of the event system.
 *
 * @param initialValue The initial value of the axis.
 * @constructor Creates a new axe limits.
 */
internal class AxeLimits(initialValue: Float)
{
    private val negativeDetection: Float
    private val positiveDetection: Float

    init
    {
        when
        {
            initialValue < -0.25f ->
            {
                this.negativeDetection = -2f
                this.positiveDetection = 0.5f
            }
            initialValue > 0.25f ->
            {
                this.negativeDetection = -0.5f
                this.positiveDetection = 2f
            }
            else ->
            {
                this.negativeDetection = -0.25f
                this.positiveDetection = 0.25f
            }
        }
    }

    /**
     * Returns the way of the axis based on its value.
     *
     * @param value The value of the axis.
     * @return The way of the axis.
     */
    fun way(value: Float): AxeWay =
        when
        {
            value < this.negativeDetection -> AxeWay.NEGATIVE
            value > this.positiveDetection -> AxeWay.POSITIVE
            else -> AxeWay.NEUTRAL
        }
}
