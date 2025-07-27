package fr.khelp.zegaime.animations.interpolation

import fr.khelp.zegaime.utils.extensions.compare
import fr.khelp.zegaime.utils.extensions.nul
import fr.khelp.zegaime.utils.math.square
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * An interpolation that creates a bouncing effect.
 *
 * The animation simulates an object bouncing a specified number of times.
 *
 * @property numberBounce The number of bounces.
 * @constructor Creates a new bouncing interpolation.
 */
class InterpolationBouncing(numberBounce: Int = 2) : Interpolation
{
    /** The number of bounces, ensured to be non-negative. */
    private val numberBounce = max(0, numberBounce)

    /**
     * Interpolates the value with a bouncing effect.
     *
     * The interpolation is calculated based on the number of bounces.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double): Double
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