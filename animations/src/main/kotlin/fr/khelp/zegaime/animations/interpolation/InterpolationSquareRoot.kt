package fr.khelp.zegaime.animations.interpolation

import kotlin.math.sqrt

/**
 * An interpolation that follows a square root progression.
 *
 * The animation has a fast start and a slow end (deceleration).
 */
data object InterpolationSquareRoot : Interpolation
{
    /**
     * Interpolates the value using a square root function.
     *
     * The formula used is `sqrt(percent)`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) = sqrt(percent)
}