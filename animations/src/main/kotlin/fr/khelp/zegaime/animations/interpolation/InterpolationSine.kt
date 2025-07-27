package fr.khelp.zegaime.animations.interpolation

import kotlin.math.PI
import kotlin.math.sin

/**
 * An interpolation that follows a sine function.
 *
 * The animation has a smooth start and a smooth end.
 */
data object InterpolationSine : Interpolation
{
    /**
     * Interpolates the value using a sine function.
     *
     * The formula used is `(1 + sin(percent * PI - PI / 2)) / 2`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        (1.0 + sin(percent * PI - PI / 2.0)) / 2.0
}