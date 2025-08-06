package fr.khelp.zegaime.engine3d.gui.style

import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.images.font.TEXT_FONT

open class StyleText : Style()
{
    var textColor : Color = BLACK
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var font : JHelpFont = TEXT_FONT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var textAlignment : TextAlignment = TextAlignment.LEFT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }
}