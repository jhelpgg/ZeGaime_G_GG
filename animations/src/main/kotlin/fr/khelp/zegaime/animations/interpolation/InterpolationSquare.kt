package fr.khelp.zegaime.animations.interpolation

/**
 * Interpolation follow square progression
 */
data object InterpolationSquare : Interpolation
{
    /**
     * Interpolate value with following equation:
     *
     *    t²
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent : Double) = percent * percent
}
