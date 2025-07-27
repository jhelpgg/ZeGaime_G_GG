package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.cubic

/**
 * A cubic Bezier interpolation.
 *
 * The interpolation is defined by two control points.
 *
 * @property firstControl The first control point, between 0 and 1.
 * @property secondControl The second control point, between 0 and 1.
 * @constructor Creates a new cubic Bezier interpolation.
 */
class InterpolationCubic(private val firstControl : Double = 0.1,
                         private val secondControl : Double = 0.9) : Interpolation
{
    /**
     * Interpolates the value using a cubic Bézier curve.
     *
     * The curve is defined by values 0, firstControl, secondControl, 1.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        cubic(0.0, this.firstControl, this.secondControl, 1.0, percent)
}