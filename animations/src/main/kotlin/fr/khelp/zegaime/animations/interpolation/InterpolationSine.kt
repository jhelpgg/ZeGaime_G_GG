package fr.khelp.zegaime.animations.interpolation

import kotlin.math.PI
import kotlin.math.sin

/**
 * Interpolation follow sines function
 */
data object InterpolationSine : Interpolation
{
    /**
     * Interpolate value with following equation :
     *
     *    1 + sin(t * PI - PI/2)
     *    ----------------------
     *              2
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (1.0 + sin(percent * PI - PI / 2.0)) / 2.0
}
