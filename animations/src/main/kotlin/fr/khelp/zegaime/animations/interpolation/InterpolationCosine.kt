package fr.khelp.zegaime.animations.interpolation

import kotlin.math.PI
import kotlin.math.cos

/**
 * Interpolation follow cosines function
 */
data object InterpolationCosine : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + cos((t + 1) * PI)
     *    ---------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (1.0 + cos((percent + 1.0) * PI)) / 2.0
}
