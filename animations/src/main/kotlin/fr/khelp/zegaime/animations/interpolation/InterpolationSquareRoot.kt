package fr.khelp.zegaime.animations.interpolation

import kotlin.math.sqrt

/**
 * Interpolation follow square root progression
 */
data object InterpolationSquareRoot : Interpolation
{
    /**
     * Interpolate value with the following equation:
     *
     *     ___
     *    V t
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = sqrt(percent)
}
