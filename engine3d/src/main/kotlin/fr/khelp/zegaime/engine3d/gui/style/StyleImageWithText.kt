package fr.khelp.zegaime.engine3d.gui.style

open class StyleImageWithText : StyleText()
{
    var imageTextRelativePosition : ImageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }
}