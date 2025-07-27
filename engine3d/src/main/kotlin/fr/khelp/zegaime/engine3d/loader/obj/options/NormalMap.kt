package fr.khelp.zegaime.engine3d.loader.obj.options

import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.utils.extensions.blue
import fr.khelp.zegaime.utils.extensions.green
import fr.khelp.zegaime.utils.extensions.red
import kotlin.math.floor

/**
 * Represents a normal map
 *
 * @property width Normal map width
 * @property height Normal map height
 * @property pixels Normal map pixels
 */
class NormalMap(val width : Int, val height : Int, val pixels : IntArray)
{
    /**
     * Gets a normal from `(u, v)` coordinates
     *
     * @param u U of `(u, v)` coordinates
     * @param v V of `(u, v)` coordinates
     *
     * @return The normal
     */
    operator fun get(u : Float, v : Float) : Point3D
    {
        val x = ((u - floor(u)) * this.width).toInt()
        val y = ((v - floor(v)) * this.height).toInt()
        val pixel = this.pixels[x + y * this.width]
        val red = pixel.red - 128
        val green = pixel.green - 128
        val blue = 128 - pixel.blue
        return Point3D(red.toFloat() / 127.0f, green.toFloat() / 127.0f, blue.toFloat() / 127.0f)
    }
}