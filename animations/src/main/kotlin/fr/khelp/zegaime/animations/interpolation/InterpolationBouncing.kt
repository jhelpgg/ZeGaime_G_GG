package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.extensions.nul
import fr.khelp.zegaime.utils.math.square
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Interpolation that bounces
 * @param numberBounce Number of bounces
 */
class InterpolationBouncing(numberBounce : Int = 2) : Interpolation
{
    /**Number of bounces*/
    private val numberBounce = max(0, numberBounce)

    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        if (this.numberBounce == 0)
        {
            return square(percent)
        }

        var amplitude = 1.0 / (this.numberBounce + 1)

        if (percent.compare(amplitude) < 0)
        {
            return square(percent / amplitude)
        }

        var free = 1.0 - amplitude * 0.56789
        var minimum = 0.56789
        var percentReal = percent - amplitude
        var left = this.numberBounce - 1

        while (percentReal.compare(amplitude) >= 0 && !amplitude.nul && !minimum.nul && !percentReal.nul && left > 0)
        {
            minimum *= 0.56789
            percentReal -= amplitude
            free -= amplitude
            amplitude = free * 0.56789
            left--
        }

        if (left == 0)
        {
            amplitude = free / 2.0
        }

        val squareRoot = sqrt(minimum)
        percentReal = (percentReal - amplitude / 2.0) * (squareRoot * 2.0 / amplitude)
        return min(square(percentReal) + 1.0 - minimum, 1.0)
    }
}
