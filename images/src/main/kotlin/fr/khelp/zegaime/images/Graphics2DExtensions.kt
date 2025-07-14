package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.images.color.base.BaseColor
import fr.khelp.zegaime.images.font.JHelpFont
import java.awt.Graphics2D
import kotlin.math.max

/**
 * Sets the font of the graphics context to the given [JHelpFont].
 *
 * @param font The font to set.
 */
fun Graphics2D.jhelpFont(font : JHelpFont)
{
    this.font = font.font
}

/**
 * Draws a string with the given text alignment.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 * @param text The text to draw.
 * @param textAlignment The text alignment.
 */
fun Graphics2D.drawText(x : Int, y : Int, text : String, textAlignment : TextAlignment = TextAlignment.LEFT)
{
    val fontMetrics = this.getFontMetrics(this.font)
    val accent = fontMetrics.ascent
    val height = fontMetrics.height
    val lines = text.split('\n')
    val widths = IntArray(lines.size) { index -> fontMetrics.stringWidth(lines[index]) }
    val maxWidth = widths.maxOrNull()!!
    var xx : Int
    var yy = y + accent

    for ((index, line) in lines.withIndex())
    {
        val width = widths[index]

        xx = when (textAlignment)
        {
            TextAlignment.LEFT   -> x
            TextAlignment.CENTER -> x + (maxWidth - width) / 2
            TextAlignment.RIGHT  -> x + maxWidth - width
        }

        this.drawString(line, xx, yy)
        yy += height
    }
}

/**
 * Fills a string with the given text alignment.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 * @param text The text to fill.
 * @param textAlignment The text alignment.
 * @param limitHorizontal The horizontal limit.
 */
fun Graphics2D.fillText(x : Int, y : Int, text : String,
                        textAlignment : TextAlignment = TextAlignment.LEFT,
                        limitHorizontal : Int = Int.MAX_VALUE) =
    this.fillText(x, y, text, JHelpFont(this.font), textAlignment, limitHorizontal)

/**
 * Fills a string with the given font and text alignment.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 * @param text The text to fill.
 * @param font The font to use.
 * @param textAlignment The text alignment.
 * @param limitHorizontal The horizontal limit.
 */
fun Graphics2D.fillText(x : Int, y : Int, text : String,
                        font : JHelpFont,
                        textAlignment : TextAlignment = TextAlignment.LEFT,
                        limitHorizontal : Int = Int.MAX_VALUE)
{
    val height = font.fontHeight
    val lines = ArrayList<Pair<String, Int>>()
    var maxWidth = 0

    for (line in text.trim().replace("\t", "   ").split('\n'))
    {
        var lineToAdd = line
        var width = font.stringWidth(lineToAdd)
        var indexSpace = lineToAdd.lastIndexOf(' ')

        while (width > limitHorizontal && indexSpace > 0)
        {
            val firstPart = line.substring(0, indexSpace).trim()
            width = font.stringWidth(firstPart)

            if (width <= limitHorizontal)
            {
                maxWidth = max(maxWidth, width)
                lines.add(Pair(firstPart, width))
                lineToAdd = lineToAdd.substring(indexSpace + 1).trim()
                width = font.stringWidth(lineToAdd)
                indexSpace = lineToAdd.lastIndexOf(' ')
            }
            else
            {
                indexSpace = lineToAdd.lastIndexOf(' ', indexSpace - 1)
            }
        }

        maxWidth = max(maxWidth, width)
        lines.add(Pair(lineToAdd, width))
    }

    var xx : Int
    var yy = y
    val space = font.stringWidth(" ")

    for ((line, width) in lines)
    {
        xx =
            when (textAlignment)
            {
                TextAlignment.LEFT   -> x
                TextAlignment.CENTER -> x + (maxWidth - width) / 2
                TextAlignment.RIGHT  -> x + maxWidth - width
            }

        for (character in line)
        {
            if (character > ' ')
            {
                val shape = font.shape(character.toString(), xx, yy)
                this.fill(shape)
                xx += shape.bounds.width
            }
            else
            {
                xx += space
            }
        }

        yy += height
    }
}

/**
 * Draws a [GameImage] at the given coordinates.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 * @param image The image to draw.
 */
fun Graphics2D.drawImage(x : Int, y : Int, image : GameImage)
{
    image.drawOn(this, x, y)
}

/**
 * Draws a [GameImage] at the given coordinates with the given dimensions.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 * @param width The width.
 * @param height The height.
 * @param image The image to draw.
 */
fun Graphics2D.drawImage(x : Int, y : Int, width : Int, height : Int, image : GameImage)
{
    image.drawOn(this, x, y, width, height)
}

/**
 * Draws a part of a [GameImage] at the given coordinates.
 *
 * @param x The x coordinate of the destination.
 * @param y The y coordinate of the destination.
 * @param imageX The x coordinate of the source.
 * @param imageY The y coordinate of the source.
 * @param width The width of the part to draw.
 * @param height The height of the part to draw.
 * @param image The image to draw from.
 */
fun Graphics2D.drawPart(x : Int, y : Int, imageX : Int, imageY : Int, width : Int, height : Int, image : GameImage)
{
    image.drawOnPart(this, x, y, imageX, imageY, width, height)
}

/**
 * Draws a [GameImage] centered at the given coordinates.
 *
 * @param x The x coordinate of the center.
 * @param y The y coordinate of the center.
 * @param image The image to draw.
 */
fun Graphics2D.drawImageCenter(x : Int, y : Int, image : GameImage)
{
    image.drawOn(this, x - image.width / 2, y - image.height - 2)
}

/**
 * Sets the color of the graphics context to the given [BaseColor].
 *
 * @param baseColor The color to set.
 */
fun Graphics2D.baseColor(baseColor : BaseColor<*>)
{
    this.color = java.awt.Color(baseColor.color.argb, true)
}

/**
 * Sets the color of the graphics context to the given [Color].
 *
 * @param color The color to set.
 */
fun Graphics2D.setColor(color : Color)
{
    this.color = java.awt.Color(color.argb, true)
}
