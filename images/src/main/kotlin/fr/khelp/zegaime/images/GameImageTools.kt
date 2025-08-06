package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.Color
import javax.swing.Icon

fun GameImage.withGray() : GameImage
{
    val copy = this.copy()
    copy.gray()
    return copy
}

fun GameImage.withContrast(contrast:Double) : GameImage
{
    val copy = this.copy()
    copy.contrast(contrast)
    return copy
}

fun GameImage.withTint(color: Color) : GameImage
{
    val copy = this.copy()
    copy.tint(color)
    return copy
}

fun Icon.toGameImage() : GameImage
{
    if (this is GameImage)
    {
        return this
    }

    val width = this.iconWidth
    val height = this.iconHeight

    if (width <= 0 || height <= 0)
    {
        return GameImage.DUMMY
    }

    val gameImage = GameImage(width, height)
    gameImage.draw { graphics2D -> this.paintIcon(null, graphics2D, 0, 0) }
    return gameImage
}
