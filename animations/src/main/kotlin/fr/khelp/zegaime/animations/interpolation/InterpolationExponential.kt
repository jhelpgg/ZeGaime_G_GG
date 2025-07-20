package fr.khelp.zegaime.animations.interpolation

import kotlin.math.E
import kotlin.math.expm1

/**
 * Interpolation follow exponential progression
 */
data object InterpolationExponential : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *     t
     *    e - 1
     *    ------
     *    e - 1
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        expm1(percent) / (E - 1.0)
}
