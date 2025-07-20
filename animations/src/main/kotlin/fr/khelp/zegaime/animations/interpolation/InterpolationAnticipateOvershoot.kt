package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.math.EPSILON
import kotlin.math.max

/**
 * Interpolation with expecting and overshoot effect
 *
 * Expect: Like if it takes a run-up
 *
 * Overshoot: Go too far and return
 * @param tension Effect factor
 */
class InterpolationAnticipateOvershoot(tension : Double = 2.0) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON, tension)

    /**
     * Interpolate value with anticipation and overshoot effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) =
        when
        {
            percent.compare(0.5) < 0 ->
            {
                val value = 2.0 * percent
                0.5 * ((this.tension + 1.0) * value * value * value - this.tension * value * value)
            }

            else                     ->
            {
                val value = 2.0 * percent - 2.0
                0.5 * ((this.tension + 1.0) * value * value * value + this.tension * value * value) + 1.0
            }
        }
}
