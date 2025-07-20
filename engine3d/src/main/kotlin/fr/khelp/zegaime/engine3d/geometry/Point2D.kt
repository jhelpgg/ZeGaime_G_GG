package fr.khelp.zegaime.engine3d.geometry

import java.util.Objects
import kotlin.math.sqrt
import org.lwjgl.opengl.GL11

class Point2D(x : Float = 0f, y : Float = 0f)
{
    companion object
    {
        fun provider() : Point2D = Point2D()
    }

    var x : Float = x
        private set
    var y : Float = y
        private set
    var length : Float = sqrt(this.x * this.x + this.y * this.y)
        private set

    operator fun plus(point2D : Point2D) : Point2D =
        Point2D(this.x + point2D.x, this.y + point2D.y)

    operator fun minus(point2D : Point2D) : Point2D =
        Point2D(this.x - point2D.x, this.y - point2D.y)

    operator fun times(factor : Float) : Point2D =
        Point2D(this.x * factor, this.y * factor)

    infix fun dot(point2D : Point2D) : Float =
        this.x * point2D.x + this.y * point2D.y

    infix fun cross(point2D : Point2D) : Float =
        this.y * point2D.x - this.x * point2D.y

    override fun hashCode() : Int = Objects.hash(this.x, this.y)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Point2D)
        {
            return false
        }

        return fr.khelp.zegaime.utils.math.equals(this.x, other.x) && fr.khelp.zegaime.utils.math.equals(this.y,
                                                                                                         other.y)
    }

    fun copy() : Point2D = Point2D(this.x, this.y)

    fun copy(point2D : Point2D)
    {
        this.x = point2D.x
        this.y = point2D.y
    }

    fun middle(point2D : Point2D) : Point2D = Point2D((this.x + point2D.x) / 2f,
                                                      (this.y + point2D.y) / 2f)

    /**
     * Apply like UV in OpenGL
     */
    internal fun glTexCoord2f()
    {
        GL11.glTexCoord2f(this.x, this.y)
    }
}