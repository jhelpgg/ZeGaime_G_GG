package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.quadratic

/**
 * Quadratic interpolation
 * @param control Control point
 */
class InterpolationQuadratic(private val control : Double = 0.25) : Interpolation
{
    /**
     * Compute quadratic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        quadratic(0.0, this.control, 1.0, percent)
}
