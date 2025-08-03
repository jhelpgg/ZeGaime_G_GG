package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.Color4f
import kotlinx.serialization.Serializable

@Serializable
data class ColorData(val alpha : Float, val red : Float, val green : Float, val blue : Float)
{
    constructor(color : Color4f) : this(alpha = color.alpha, red = color.red, green = color.green, blue = color.blue)

    val color : Color4f by lazy { Color4f(red = this.red, green = this.green, blue = this.blue, alpha = this.alpha) }
}


