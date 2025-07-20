package fr.khelp.zegaime.animations.interpolation

/**
 * Interpolation with hesitation effect
 */
data object InterpolationHesitate : Interpolation
{
    /**
     * Interpolate value with hesitation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) : Double
    {
        val value = 2.0 * percent - 1.0
        return 0.5 * (value * value * value + 1.0)
    }
}
