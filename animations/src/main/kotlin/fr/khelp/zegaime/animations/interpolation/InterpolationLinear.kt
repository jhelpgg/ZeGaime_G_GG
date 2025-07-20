package fr.khelp.zegaime.animations.interpolation

/**
 * Linear interpolation
 */
data object InterpolationLinear : Interpolation
{
    override fun invoke(percent : Double) : Double = percent
}