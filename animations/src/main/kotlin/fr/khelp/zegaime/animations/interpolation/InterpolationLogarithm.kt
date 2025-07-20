package fr.khelp.zegaime.animations.interpolation

import kotlin.math.ln
import kotlin.math.ln1p

/**
 * Interpolation follow logarithm progression
 */
data object InterpolationLogarithm : Interpolation
{
    /**
     * Interpolate value with the following equation:
     *
     *    ln(t + 1)
     *    --------
     *     ln(2)
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        ln1p(percent) / ln(2.0)
}
