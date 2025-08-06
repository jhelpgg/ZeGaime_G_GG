package fr.khelp.zegaime.engine3d.gui.menu

import fr.khelp.zegaime.engine3d.gui.component.GUIComponentPanel
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import fr.khelp.zegaime.engine3d.gui.layout.horizontal.GUIHorizontalConstraint
import fr.khelp.zegaime.engine3d.gui.layout.horizontal.GUIHorizontalLayout
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShapeRectangle
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.VerticalAlignment
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.resources.ResourcesText
import kotlin.math.min

internal class GUIMenuBar(val resourcesText : ResourcesText,
                          val font : JHelpFont,
                          val textColor : Color,
                          val textBorderColor : Color,
                          val backgroundColor : Color)
{
    private val menus = ArrayList<GUIMenu>()
    private var previousPanel : GUIComponentPanel<*, *>? = null

    fun addMenu(keyText : String) : GUIMenu
    {
        val menu = GUIMenu(keyText, this.resourcesText, this.backgroundColor)
        this.menus.add(menu)
        return menu
    }

    fun panel(width : Int) : Pair<GUIComponentPanel<*, *>, Int>
    {
        val main = GUIAbsoluteLayout()

        val headerLayout = GUIHorizontalLayout()
        val panels = ArrayList<Pair<GUIComponentText, GUIComponentPanel<*, *>>>()

        for (menu in this.menus)
        {
            val panel = menu.panel(this.font, this.textColor, this.textBorderColor)
            panel.visible = false

            val text = GUIComponentText()
            text.keyText = menu.keyText
            text.resourcesText = menu.resourcesText
            text.textColorMain = this.textColor
            text.textColorBorder = this.textBorderColor
            text.font = this.font
            text.textAlignment = TextAlignment.CENTER
            text.verticalAlignment = VerticalAlignment.CENTER
            text.downAction = { this.openClose(panel) }

            panels.add(Pair(text, panel))

            headerLayout.add(text, GUIHorizontalConstraint.CENTER)
        }

        val header = GUIComponentPanel<GUIHorizontalConstraint, GUIHorizontalLayout>(headerLayout)
        header.background = StyleBackgroundColor(this.backgroundColor)
        header.borderColor = BLACK
        header.shape = StyleShapeRectangle
        val headerSize = header.preferredSize()
        headerLayout.layout(width, headerSize.height)
        val y = headerSize.height

        main.add(header, GUIAbsoluteConstraint(0, 0, width, headerSize.height))

        for ((text, panel) in panels)
        {
            var x = text.x
            val panelSize = panel.preferredSize()
            x = min(x, width - panelSize.width)
            main.add(panel, GUIAbsoluteConstraint(x, y, panelSize.width, panelSize.height))
        }

        return Pair(GUIComponentPanel<GUIAbsoluteConstraint, GUIAbsoluteLayout>(main), y)
    }

    private fun openClose(panel : GUIComponentPanel<*, *>)
    {
        if (panel.visible)
        {
            panel.visible = false
            this.previousPanel = null
            return
        }

        this.previousPanel?.visible = false
        panel.visible = true
        this.previousPanel = panel
    }
}
