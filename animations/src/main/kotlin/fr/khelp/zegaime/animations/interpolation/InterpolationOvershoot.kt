package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * An interpolation that overshoots.
 *
 * The animation goes too far and then comes back to the final position.
 *
 * @property tension The tension of the overshoot. A higher value means a more pronounced effect.
 * @constructor Creates a new overshoot interpolation.
 */
class InterpolationOvershoot(tension : Double = 2.0) : Interpolation
{
    /** The tension of the overshoot, ensured to be positive. */
    private val tension = max(EPSILON, tension)

    /**
     * Interpolates the value with an overshoot effect.
     *
     * The formula used is `(tension + 1) * (percent - 1)^3 + tension * (percent - 1)^2 + 1`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = percent - 1.0
        return (this.tension + 1.0) * value * value * value + this.tension * value * value + 1.0
    }
}