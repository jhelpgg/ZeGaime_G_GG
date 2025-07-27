package fr.khelp.zegaime.animations.interpolation

import kotlin.math.PI
import kotlin.math.cos

/**
 * An interpolation that follows a cosine function.
 *
 * The animation has a smooth start and a smooth end.
 */
data object InterpolationCosine : Interpolation
{
    /**
     * Interpolates the value using a cosine function.
     *
     * The formula used is `(1 + cos((percent + 1) * PI)) / 2`.
     *
     * @param percent The value to interpolate, between 0 and 1.
     * @return The interpolated value.
     */
    override operator fun invoke(percent : Double) =
        (1.0 + cos((percent + 1.0) * PI)) / 2.0
}