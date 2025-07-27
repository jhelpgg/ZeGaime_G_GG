package fr.khelp.zegaime.animations.interpolation

/**
 * An interpolation with a hesitation effect.
 *
 * The animation starts, hesitates, and then finishes.
 */
data object InterpolationHesitate : Interpolation
{
    /**
     * Interpolates the value with a hesitation effect.
     *
     * The formula used is `0.5 * ((2 * percent - 1)^3 + 1)`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = 2.0 * percent - 1.0
        return 0.5 * (value * value * value + 1.0)
    }
}