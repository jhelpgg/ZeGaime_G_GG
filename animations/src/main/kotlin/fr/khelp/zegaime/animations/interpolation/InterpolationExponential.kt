package fr.khelp.zegaime.animations.interpolation

import kotlin.math.E
import kotlin.math.expm1

/**
 * An interpolation that follows an exponential progression.
 *
 * The animation has a very slow start and a very fast end.
 */
data object InterpolationExponential : Interpolation
{
    /**
     * Interpolates the value using an exponential function.
     *
     * The formula used is `(e^percent - 1) / (e - 1)`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) =
        expm1(percent) / (E - 1.0)
}