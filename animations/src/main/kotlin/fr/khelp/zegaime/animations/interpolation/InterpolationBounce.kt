package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.math.square

/**
 * Interpolation that makes bounce effect
 */
data object InterpolationBounce : Interpolation
{
    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
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

