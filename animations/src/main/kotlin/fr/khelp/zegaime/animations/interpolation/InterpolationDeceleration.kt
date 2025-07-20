package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max
import kotlin.math.pow

/**
 * Interpolation with deceleration effect
 * @param factor Deceleration factor
 */
class InterpolationDeceleration(factor : Double = 2.0) : Interpolation
{
    /**Deceleration factor*/
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolate value with deceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        1.0 - (1.0 - percent).pow(this.factor)
}
