package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max
import kotlin.math.pow

/**
 * An interpolation with an acceleration effect.
 *
 * The animation starts slowly and finishes quickly.
 *
 * @property factor The acceleration factor. A value of 1.0 means no acceleration (linear).
 * A value greater than 1.0 means more acceleration.
 * @constructor Creates a new acceleration interpolation.
 */
class InterpolationAcceleration(factor : Double = 2.0) : Interpolation
{
    /** The acceleration factor, ensured to be positive. */
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolates the value with an acceleration effect.
     *
     * The formula used is `percent ^ factor`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) = percent.pow(this.factor)
}