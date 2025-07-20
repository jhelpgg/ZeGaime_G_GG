package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.nul
import fr.khelp.zegaime.utils.math.equals
import fr.khelp.zegaime.utils.math.random

/**
 * Interpolation with random progression
 */
data object InterpolationRandom : Interpolation
{
    /**
     * Interpolate value with random progression
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.nul || equals(percent, 1.0) -> percent
            else                                -> random(percent, 1.0)
        }
}
