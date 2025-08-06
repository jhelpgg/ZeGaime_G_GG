package fr.khelp.zegaime.engine3d.gui.component

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.gui.GUIMargin
import fr.khelp.zegaime.engine3d.gui.layout.GUIConstraints
import fr.khelp.zegaime.engine3d.gui.layout.GUILayout
import java.awt.Dimension
import java.awt.Graphics2D

class GUIComponentPanel<C : GUIConstraints, L : GUILayout<C>>(private val layout : L) : GUIComponent()
{
    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val transform = graphics2D.transform
        val clip = graphics2D.clip
        val parentWidth = this.width - margin.width
        val parentHeight = this.height - margin.height
        graphics2D.clipRect(margin.left, margin.top, parentWidth, parentHeight)
        graphics2D.translate(margin.left, margin.top)
        this.layout.layout(parentWidth, parentHeight)

        for (component in this.layout.components())
        {
            if (component.visible)
            {
                val clip2 = graphics2D.clip
                val transform2 = graphics2D.transform
                graphics2D.clipRect(component.x, component.y, component.width, component.height)
                graphics2D.translate(component.x, component.y)
                component.draw(graphics2D)
                graphics2D.transform = transform2
                graphics2D.clip = clip2
            }
        }

        graphics2D.transform = transform
        graphics2D.clip = clip
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val preferred = this.layout.preferredSize()
        return Dimension(preferred.width + margin.width, preferred.height + margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean =
        this.layout.mouseState(mouseState)

    internal fun relayoutWithPreferred()
    {
        val size = this.preferredSize()
        this.layout.layout(size.width, size.height)
    }
}
