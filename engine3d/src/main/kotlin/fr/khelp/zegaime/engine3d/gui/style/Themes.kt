package fr.khelp.zegaime.engine3d.gui.style

import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShapeSausage
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.base.Grey
import fr.khelp.zegaime.images.font.BUTTON_FONT
import fr.khelp.zegaime.images.font.TITLE_FONT

fun title() : StyleImageWithTextClickable
{
    val style = StyleImageWithTextClickable()
    style.font = TITLE_FONT
    style.textAlignment = TextAlignment.CENTER
    style.borderColor = TRANSPARENT
    return style
}

fun button() : StyleImageWithTextClickable
{
    val style = StyleImageWithTextClickable()
    style.font = BUTTON_FONT
    style.textAlignment = TextAlignment.CENTER
    style.shape = StyleShapeSausage
    style.overBackground = StyleBackgroundColor(Green.GREEN_0300.color)
    style.outOfBackground = StyleBackgroundColor(Green.GREEN_0500.color)
    style.clickBackground = StyleBackgroundColor(Green.GREEN_0700.color)
    style.borderColor = Grey.BLACK.color
    return style
}
