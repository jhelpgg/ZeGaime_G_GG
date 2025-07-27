package fr.khelp.zegaime.animations.interpolation

/**
 * An interpolation that follows a square progression.
 *
 * The animation has a slow start and a fast end (acceleration).
 */
data object InterpolationSquare : Interpolation
{
    /**
     * Interpolates the value using a square function.
     *
     * The formula used is `percent * percent`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent: Double) = percent * percent
}