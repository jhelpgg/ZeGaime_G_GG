package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.nul
import fr.khelp.zegaime.utils.math.equals
import fr.khelp.zegaime.utils.math.random

/**
 * An interpolation with a random progression.
 *
 * The animation progression is chaotic.
 */
data object InterpolationRandom : Interpolation
{
    /**
     * Interpolates the value with a random progression.
     *
     * If the value is 0 or 1, it returns the same value.
     * Otherwise, it returns a random value between the given value and 1.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.nul || equals(percent, 1.0) -> percent
            else                                -> random(percent, 1.0)
        }
}