package fr.khelp.zegaime.engine3d.gui.menu

import fr.khelp.zegaime.engine3d.events.GenericAction
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentPanel
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentTextImage
import fr.khelp.zegaime.engine3d.gui.dsl.panelVertical
import fr.khelp.zegaime.engine3d.gui.style.ImageTextRelativePosition
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShapeRectangle
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.VerticalAlignment
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.resources.ResourcesText
import java.awt.event.ActionEvent

internal class GUIMenu(val keyText : String, val resourcesText : ResourcesText, val backgroundColor : Color)
{
    companion object
    {
        private val ACTION_EVENT = ActionEvent(Unit, 0, "")
    }

    private val actions = ArrayList<GenericAction>()
    private var panel : GUIComponentPanel<*, *>? = null

    fun add(action : GenericAction)
    {
        this.actions.add(action)
    }

    fun panel(font : JHelpFont,
              textColor : Color, textBorderColor : Color) : GUIComponentPanel<*, *>
    {
        val panel = panelVertical {
            for (action in this@GUIMenu.actions)
            {
                val image = action.largeImage() ?: action.smallImage()

                val component =
                    if (image == null)
                    {
                        val text = GUIComponentText()
                        text.keyText = action.keyName()
                        text.resourcesText = this@GUIMenu.resourcesText
                        text.font = font
                        text.textColorMain = textColor
                        text.textColorBorder = textBorderColor
                        text.textAlignment = TextAlignment.LEFT
                        text.verticalAlignment = VerticalAlignment.CENTER
                        text.margin = text.margin.copy(left=16)
                        text
                    }
                    else
                    {
                        val textImage = GUIComponentTextImage()
                        textImage.keyText = action.keyName()
                        textImage.resourcesText = this@GUIMenu.resourcesText
                        textImage.font = font
                        textImage.textColorMain = textColor
                        textImage.textColorBorder = textBorderColor
                        textImage.textAlignment = TextAlignment.LEFT
                        textImage.image = image
                        textImage.imageSize = 32
                        textImage.imageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT
                        textImage.margin = textImage.margin.copy(left=16)
                        textImage
                    }

                component.downAction = {
                    this@GUIMenu.panel?.visible = false
                    action.actionPerformed(ACTION_EVENT)
                }

                component.left
            }
        }

        panel.background = StyleBackgroundColor(this.backgroundColor)
        panel.borderColor = BLACK
        panel.shape = StyleShapeRectangle
        panel.relayoutWithPreferred()
        this.panel = panel
        return panel
    }
}

