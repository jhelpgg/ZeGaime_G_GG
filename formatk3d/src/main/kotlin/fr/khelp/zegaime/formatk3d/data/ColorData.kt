package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.Color4f
import kotlinx.serialization.Serializable

/**
 * Color data for save
 * @param alpha Alpha component
 * @param red Red component
 * @param green Green component
 * @param blue Blue component
 */
@Serializable
data class ColorData(val alpha : Float, val red : Float, val green : Float, val blue : Float)
{
    /**
     * Create a new instance of ColorData from a Color4f
     * @param color Color4f to copy
     */
    constructor(color : Color4f) : this(alpha = color.alpha, red = color.red, green = color.green, blue = color.blue)

    /**
     * The Color4f
     */
    val color : Color4f by lazy { Color4f(red = this.red, green = this.green, blue = this.blue, alpha = this.alpha) }
}


