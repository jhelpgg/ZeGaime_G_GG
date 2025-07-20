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

class Color4f(red : Float, green : Float = red, blue : Float = red, alpha : Float = 1f)
{
    companion object
    {
        private fun partEquals(part1 : Float, part2 : Float) =
            (part1 * 255f).toInt() == (part2 * 255f).toInt()
    }

    val red = red.coerceIn(0f, 1f)
    val green = green.coerceIn(0f, 1f)
    val blue = blue.coerceIn(0f, 1f)
    val alpha = alpha.coerceIn(0f, 1f)

    val color : Color get() = argb(this.alpha, this.red, this.green, this.blue)

    constructor(color : Int) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    constructor(color : Color) : this(color.alpha / 255f, color.red / 255f, color.green / 255f, color.blue / 255f)

    /**
     * Push the color in the float buffer
     *
     * @return Filled float buffer
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
     * Push the color in the float buffer
     *
     * @param percent Multiplier of percent of color
     * @return Filled float buffer
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

    internal fun glColor4f()
    {
        GL11.glColor4f(this.red, this.green, this.blue, this.alpha)
    }

    internal fun glColor4f(alpha : Float)
    {
        GL11.glColor4f(this.red, this.green, this.blue, alpha)
    }

    internal fun glColor4fBackground()
    {
        GL11.glClearColor(this.red, this.green, this.blue, 1f)
    }

    override fun hashCode() : Int = Objects.hash(this.alpha, this.red, this.green, this.blue)

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
