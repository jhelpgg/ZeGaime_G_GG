package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * Interpolation with anticipation effect.
 *
 * Thai is to say it looks goes reverse and then go to the good way, like if it takes a run-up
 * @param tension Effect factor
 */
class InterpolationAnticipate(tension : Double = 2.0) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with anticipation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        (this.tension + 1.0) * percent * percent * percent - this.tension * percent * percent
}