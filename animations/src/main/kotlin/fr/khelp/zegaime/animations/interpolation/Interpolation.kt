package fr.khelp.zegaime.animations.interpolation

/**
 * An interpolation
 */
sealed interface Interpolation
{
    /**
     * Interpolates a value in [[0, 1]]
     *
     * The constraint is that 0 gives 0 and 1 1, other value in ]0,1[ depends
     *
     * @param percent Linear value to transform
     *
     * @return Transformed value
     */
    operator fun invoke(percent : Double) : Double
}