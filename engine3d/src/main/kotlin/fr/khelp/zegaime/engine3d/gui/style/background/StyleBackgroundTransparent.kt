package fr.khelp.zegaime.engine3d.gui.style.background

import java.awt.Graphics2D
import java.awt.Shape

object StyleBackgroundTransparent : StyleBackground
{
    override fun applyOnShape(graphics2D : Graphics2D, shape : Shape) = Unit
}