package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * Interpolation that overshoots.
 *
 * That is to say, it goes too far and then go back to the good place
 * @param tension Effect factor
 */
class InterpolationOvershoot(tension : Double = 2.0) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = percent - 1.0
        return (this.tension + 1.0) * value * value * value + this.tension * value * value + 1.0
    }
}
