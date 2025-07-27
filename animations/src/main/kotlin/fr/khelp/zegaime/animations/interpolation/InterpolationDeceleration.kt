package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max
import kotlin.math.pow

/**
 * An interpolation with a deceleration effect.
 *
 * The animation starts quickly and finishes slowly.
 *
 * @property factor The deceleration factor. A value of 1.0 means no deceleration (linear).
 * A value greater than 1.0 means more deceleration.
 * @constructor Creates a new deceleration interpolation.
 */
class InterpolationDeceleration(factor: Double = 2.0) : Interpolation
{
    /** The deceleration factor, ensured to be positive. */
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolates the value with a deceleration effect.
     *
     * The formula used is `1 - (1 - percent) ^ factor`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) =
        1.0 - (1.0 - percent).pow(this.factor)
}