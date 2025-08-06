package fr.khelp.zegaime.engine3d.gui.style.background

import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.base.BaseColor
import fr.khelp.zegaime.images.setColor
import java.awt.Graphics2D
import java.awt.Shape
import java.util.Objects

class StyleBackgroundColor(private val color : Color) : StyleBackground
{
    constructor(baseColor : BaseColor<*>) : this(baseColor.color)

    override fun applyOnShape(graphics2D : Graphics2D, shape : Shape)
    {
        val color = graphics2D.color
        graphics2D.setColor(this.color)
        graphics2D.fill(shape)
        graphics2D.color = color
    }

    override fun hashCode() : Int = Objects.hash(this.color)

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is StyleBackgroundColor)
        {
            return false
        }

        return this.color == other.color
    }
}
