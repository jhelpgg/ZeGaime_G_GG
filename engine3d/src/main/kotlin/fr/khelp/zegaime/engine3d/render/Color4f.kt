package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegaime.engine3d.utils.TEMPORARY_FLOAT_BUFFER
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.alpha
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.images.color.blue
import fr.khelp.zegaime.images.color.green
import fr.khelp.zegaime.images.color.red
import fr.khelp.zegaime.utils.extensions.alpha
import fr.khelp.zegaime.utils.extensions.blue
import fr.khelp.zegaime.utils.extensions.green
import fr.khelp.zegaime.utils.extensions.red
import java.nio.FloatBuffer
import java.util.Objects
import org.lwjgl.opengl.GL11

/**
 * Represents a color with four float components (red, green, blue, alpha).
 *
 * **Creation example:**
 * ```kotlin
 * val red = Color4f(1f, 0f, 0f)
 * val green = Color4f(0f, 1f, 0f, 0.5f)
 * val fromInt = Color4f(0xFFFF0000.toInt())
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val color = Color4f(1f, 0f, 0f)
 * GL11.glColor4f(color.red, color.green, color.blue, color.alpha)
 * ```
 *
 * @property red The red component of the color (0.0 to 1.0).
 * @property green The green component of the color (0.0 to 1.0).
 * @property blue The blue component of the color (0.0 to 1.0).
 * @property alpha The alpha component of the color (0.0 to 1.0).
 * @constructor Creates a new color.
 */
class Color4f(red : Float, green : Float = red, blue : Float = red, alpha : Float = 1f)
{
    companion object
    {
        private fun partEquals(part1 : Float, part2 : Float) =
            (part1 * 255f).toInt() == (part2 * 255f).toInt()
    }

    /**
     * The red component of the color (0.0 to 1.0).
     */
    val red = red.coerceIn(0f, 1f)

    /**
     * The green component of the color (0.0 to 1.0).
     */
    val green = green.coerceIn(0f, 1f)

    /**
     * The blue component of the color (0.0 to 1.0).
     */
    val blue = blue.coerceIn(0f, 1f)

    /**
     * The alpha component of the color (0.0 to 1.0).
     */
    val alpha = alpha.coerceIn(0f, 1f)

    /**
     * The color as a `fr.khelp.zegaime.images.color.Color` instance.
     */
    val color : Color get() = argb(this.alpha, this.red, this.green, this.blue)

    /**
     * Creates a new color from an ARGB integer.
     *
     * @param color The ARGB integer.
     */
    constructor(color : Int) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

    /**
     * Creates a new color from a `fr.khelp.zegaime.images.color.Color` instance.
     *
     * @param color The color instance.
     */
    constructor(color : Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

    /**
     * Push the color in the float buffer.
     *
     * For internal use only.
     *
     * @return Filled float buffer.
     */
    internal fun putInFloatBuffer() : FloatBuffer
    {
        TEMPORARY_FLOAT_BUFFER.rewind()
        TEMPORARY_FLOAT_BUFFER.put(this.red)
        TEMPORARY_FLOAT_BUFFER.put(this.green)
        TEMPORARY_FLOAT_BUFFER.put(this.blue)
        TEMPORARY_FLOAT_BUFFER.put(this.alpha)
        TEMPORARY_FLOAT_BUFFER.rewind()

        return TEMPORARY_FLOAT_BUFFER
    }

    /**
     * Push the color in the float buffer.
     *
     * For internal use only.
     *
     * @param percent Multiplier of percent of color.
     * @return Filled float buffer.
     */
    internal fun putInFloatBuffer(percent : Float) : FloatBuffer
    {
        TEMPORARY_FLOAT_BUFFER.rewind()
        TEMPORARY_FLOAT_BUFFER.put(this.red * percent)
        TEMPORARY_FLOAT_BUFFER.put(this.green * percent)
        TEMPORARY_FLOAT_BUFFER.put(this.blue * percent)
        TEMPORARY_FLOAT_BUFFER.put(this.alpha)
        TEMPORARY_FLOAT_BUFFER.rewind()

        return TEMPORARY_FLOAT_BUFFER
    }

    /**
     * Sets the current color for OpenGL.
     *
     * For internal use only.
     */
    internal fun glColor4f()
    {
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha)
    }

    /**
     * Sets the current color for OpenGL with a specific alpha.
     *
     * For internal use only.
     *
     * @param alpha The alpha value.
     */
    internal fun glColor4f(alpha : Float)
    {
        GL11.glColor4f(this.red, this.green, this.blue, alpha)
    }

    /**
     * Sets the clear color for OpenGL.
     *
     * For internal use only.
     */
    internal fun glColor4fBackground()
    {
        GL11.glClearColor(this.red, this.green, this.blue, 1f)
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    override fun hashCode() : Int = Objects.hash(this.alpha, this.red, this.green, this.blue)

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other The reference object with which to compare.
     * @return `true` if this object is the same as the obj argument; `false` otherwise.
     */
    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Color4f)
        {
            return false
        }

        return Color4f.partEquals(this.alpha, other.alpha)
               && Color4f.partEquals(this.red, other.red)
               && Color4f.partEquals(this.green, other.green)
               && Color4f.partEquals(this.blue, other.blue)
    }
}