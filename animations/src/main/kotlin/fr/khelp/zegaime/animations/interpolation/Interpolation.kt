package fr.khelp.zegaime.animations.interpolation

/**
 * An interpolation is a function that transforms a linear progress (from 0 to 1) to another progress.
 *
 * The constraint is that the interpolation of 0 must be 0 and the interpolation of 1 must be 1.
 * The values between 0 and 1 can be transformed to create different animation effects like acceleration, deceleration, bounce, etc.
 *
 * For example, a linear interpolation will return the same value as the input:
 *
 * ```kotlin
 * object InterpolationLinear : Interpolation
 * {
 *     override fun invoke(percent: Double): Double = percent
 * }
 * ```
 *
 * An acceleration interpolation will return a value that grows faster at the end:
 *
 * ```kotlin
 * object InterpolationAcceleration : Interpolation
 * {
 *     override fun invoke(percent: Double): Double = percent * percent
 * }
 * ```
 */
sealed interface Interpolation
{
    /**
     * Interpolates a value in the range [0, 1].
     *
     * The implementation must respect the constraint that `invoke(0.0)` returns `0.0` and `invoke(1.0)` returns `1.0`.
     *
     * @param percent The linear value to transform, between 0 and 1.
     * @return The transformed value.
     */
    operator fun invoke(percent : Double) : Double
}
