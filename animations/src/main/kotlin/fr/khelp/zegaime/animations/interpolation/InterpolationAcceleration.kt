package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max
import kotlin.math.pow

/**
 * Interpolation with acceleration effect
 * @param factor Acceleration factor
 */
class InterpolationAcceleration(factor : Double = 2.0) : Interpolation
{
    /**Acceleration factor*/
    private val factor = 2.0 * max(EPSILON, factor)

    /**
     * Interpolate value with acceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = percent.pow(this.factor)
}

