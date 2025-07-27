package fr.khelp.zegaime.animations.interpolation

/**
 * A linear interpolation.
 *
 * The interpolation is a direct mapping of the input value.
 * It is the default interpolation.
 */
data object InterpolationLinear : Interpolation
{
    /**
     * Returns the given value without any transformation.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The same value as the input.
     */
    override fun invoke(percent: Double): Double = percent
}
