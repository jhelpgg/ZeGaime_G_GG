package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.cubic

/**
 * Cubic interpolation
 * @param firstControl First control point
 * @param secondControl Second control point
 */
class InterpolationCubic(private val firstControl : Double = 0.1,
                         private val secondControl : Double = 0.9) : Interpolation
{
    /**
     * Compute cubic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        cubic(0.0, this.firstControl, this.secondControl, 1.0, percent)
}
