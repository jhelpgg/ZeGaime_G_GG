package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.quadratic

/**
 * A quadratic Bezier interpolation.
 *
 * The interpolation is defined by a single control point.
 *
 * @property control The control point, between 0 and 1.
 * @constructor Creates a new quadratic Bezier interpolation.
 */
class InterpolationQuadratic(private val control : Double = 0.25) : Interpolation
{
    /**
     * Interpolates the value using a quadratic Bezier curve.
     *
     * The curve is defined by values 0, control, 1.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        quadratic(0.0, this.control, 1.0, percent)
}