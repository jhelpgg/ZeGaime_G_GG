package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * An interpolation with an anticipation and overshoot effect.
 *
 * The animation goes backward before going forward (anticipation), and goes too far before coming back to the final position (overshoot).
 *
 * @property tension The tension of the effect. A higher value means a more pronounced effect.
 * @constructor Creates a new anticipation-overshoot interpolation.
 */
class InterpolationAnticipateOvershoot(tension: Double = 2.0) : Interpolation
{
    /** The tension of the effect, ensured to be positive. */
    private val tension = max(EPSILON, tension)

    /**
     * Interpolates the value with an anticipation and overshoot effect.
     *
     * The interpolation is divided into two parts:
     * - The first half is an anticipation effect.
     * - The second half is an overshoot effect.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) =
        when
        {
            percent.compare(0.5) < 0 ->
            {
                val value = 2.0 * percent
                0.5 * ((this.tension + 1.0) * value * value * value - this.tension * value * value)
            }

            else ->
            {
                val value = 2.0 * percent - 2.0
                0.5 * ((this.tension + 1.0) * value * value * value + this.tension * value * value) + 1.0
            }
        }
}