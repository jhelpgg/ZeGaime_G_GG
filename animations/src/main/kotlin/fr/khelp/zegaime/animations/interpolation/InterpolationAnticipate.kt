package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * An interpolation with an anticipation effect.
 *
 * The animation goes backward before going forward, like a run-up.
 *
 * @property tension The tension of the anticipation. A higher value means a more pronounced effect.
 * @constructor Creates a new anticipation interpolation.
 */
class InterpolationAnticipate(tension: Double = 2.0) : Interpolation
{
    /** The tension of the anticipation, ensured to be positive. */
    private val tension = max(EPSILON, tension)

    /**
     * Interpolates the value with an anticipation effect.
     *
     * The formula used is `(tension + 1) * percent^3 - tension * percent^2`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) =
        (this.tension + 1.0) * percent * percent * percent - this.tension * percent * percent
}
