package fr.khelp.zegaime.engine3d.gui.style

import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackground
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundTransparent
import fr.khelp.zegaime.images.color.base.Cyan

class StyleImageWithTextClickable : StyleImageWithText()
{
    var outOfBackground : StyleBackground = StyleBackgroundTransparent
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var overBackground : StyleBackground = StyleBackgroundColor(Cyan.CYAN_0500)
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var clickBackground : StyleBackground = StyleBackgroundColor(Cyan.CYAN_0500)
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }
}