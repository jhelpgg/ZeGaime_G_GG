package fr.khelp.zegaime.images.color

/**
 * Color in A-YUV format (Alpha, Y:Luminance, U:Chromatic 1, V:Chromatic 2).
 *
 * @property alpha The alpha component (0-255).
 * @property y The luminance component (0-255).
 * @property u The first chrominance component (0-255).
 * @property v The second chrominance component (0-255).
 * @constructor Creates a new AYUV color.
 */
class AYUV(alpha: Int, y: Int, u: Int, v: Int) : Color
{
    /**
     * The alpha component (0-255).
     */
    val alpha = alpha.coerceIn(0, 255)
    /**
     * The luminance component (0-255).
     */
    val y = y.coerceIn(0, 255)
    /**
     * The first chrominance component (0-255).
     */
    val u = u.coerceIn(0, 255)
    /**
     * The second chrominance component (0-255).
     */
    val v = v.coerceIn(0, 255)

    /**
     * Returns the alpha component.
     */
    operator fun component1(): Int = this.alpha
    /**
     * Returns the luminance component.
     */
    operator fun component2(): Int = this.y
    /**
     * Returns the first chrominance component.
     */
    operator fun component3(): Int = this.u
    /**
     * Returns the second chrominance component.
     */
    operator fun component4(): Int = this.v
}
