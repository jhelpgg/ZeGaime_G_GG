package fr.khelp.zegaime.animations.interpolation

import kotlin.math.ln
import kotlin.math.ln1p

/**
 * An interpolation that follows a logarithmic progression.
 *
 * The animation has a fast start and a slow end.
 */
data object InterpolationLogarithm : Interpolation
{
    /**
     * Interpolates the value using a logarithmic function.
     *
     * The formula used is `ln(percent + 1) / ln(2)`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) =
        ln1p(percent) / ln(2.0)
}