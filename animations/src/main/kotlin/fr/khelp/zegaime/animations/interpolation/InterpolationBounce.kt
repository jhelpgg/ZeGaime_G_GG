package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.math.square

/**
 * An interpolation that creates a bounce effect.
 *
 * The animation simulates an object bouncing, with decreasing amplitude.
 */
data object InterpolationBounce : Interpolation
{
    /**
     * Interpolates the value with a bounce effect.
     *
     * The interpolation is defined by a piecewise function that simulates bounces.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.compare(0.31489) < 0 -> 8.0 * square(1.1226 * percent)
            percent.compare(0.65990) < 0 -> 8.0 * square(1.1226 * percent - 0.54719) + 0.7
            percent.compare(0.85908) < 0 -> 8.0 * square(1.1226 * percent - 0.8526) + 0.9
            else                         -> 8.0 * square(1.1226 * percent - 1.0435) + 0.95
        }
}