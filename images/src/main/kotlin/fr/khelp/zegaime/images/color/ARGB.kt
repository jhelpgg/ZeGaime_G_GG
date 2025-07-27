package fr.khelp.zegaime.images.color

/**
 * Color in ARGB (Alpha, Red, Green, Blue).
 *
 * @property alpha The alpha component (0-255).
 * @property red The red component (0-255).
 * @property green The green component (0-255).
 * @property blue The blue component (0-255).
 * @constructor Creates a new ARGB color.
 */
class ARGB(alpha : Int, red : Int, green : Int, blue : Int) : Color
{
    /**
     * The alpha component (0-255).
     */
    val alpha : Int = alpha.coerceIn(0, 255)

    /**
     * The red component (0-255).
     */
    val red : Int = red.coerceIn(0, 255)

    /**
     * The green component (0-255).
     */
    val green : Int = green.coerceIn(0, 255)

    /**
     * The blue component (0-255).
     */
    val blue : Int = blue.coerceIn(0, 255)

    /**
     * Returns the alpha component.
     */
    operator fun component1() : Int = this.alpha

    /**
     * Returns the red component.
     */
    operator fun component2() : Int = this.red

    /**
     * Returns the green component.
     */
    operator fun component3() : Int = this.green

    /**
     * Returns the blue component.
     */
    operator fun component4() : Int = this.blue
}
