package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.argb
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Image

/**
 * A graphics context that uses percentages for coordinates and dimensions.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is provided by the `GameImage.drawPercent` method.
 *
 * **Standard usage:**
 * ```kotlin
 * image.drawPercent { percentGraphics ->
 *     percentGraphics.color = Color.BLUE
 *     percentGraphics.fillRectangle(0.1, 0.1, 0.8, 0.8)
 * }
 * ```
 *
 * @property color The current color.
 * @property font The current font.
 */
class PercentGraphics internal constructor(private val graphics2D : Graphics2D,
                                           private val width : Int, private val height : Int)
{
    private val Double.width : Int get() = (this * this@PercentGraphics.width).toInt()
    private val Double.height : Int get() = (this * this@PercentGraphics.height).toInt()

    var color : Color
        get() = this.graphics2D.color.rgb.argb
        set(value)
        {
            this.graphics2D.color = java.awt.Color(value.argb, true)
        }

    var font : Font
        get() = this.graphics2D.font
        set(value)
        {
            this.graphics2D.font = value
        }

    /**
     * Draws a line between two points.
     *
     * @param xPercent1 The x coordinate of the first point.
     * @param yPercent1 The y coordinate of the first point.
     * @param xPercent2 The x coordinate of the second point.
     * @param yPercent2 The y coordinate of the second point.
     */
    fun drawLine(xPercent1 : Double, yPercent1 : Double, xPercent2 : Double, yPercent2 : Double)
    {
        this.graphics2D.drawLine(xPercent1.width, yPercent1.height, xPercent2.width, yPercent2.height)
    }

    /**
     * Draws a rectangle.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the rectangle.
     * @param heightPercent The height of the rectangle.
     */
    fun drawRectangle(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawRect(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    /**
     * Draws a rounded rectangle.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the rectangle.
     * @param heightPercent The height of the rectangle.
     * @param arcWidthPercent The width of the arc.
     * @param arcHeightPercent The height of the arc.
     */
    fun drawRoundRectangle(xPercent : Double, yPercent : Double,
                           widthPercent : Double, heightPercent : Double,
                           arcWidthPercent : Double, arcHeightPercent : Double)
    {
        this.graphics2D.drawRoundRect(xPercent.width, yPercent.height,
                                      widthPercent.width, heightPercent.height,
                                      arcWidthPercent.width, arcHeightPercent.height)
    }

    /**
     * Draws an oval.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the oval.
     * @param heightPercent The height of the oval.
     */
    fun drawOval(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawOval(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    /**
     * Draws a string.
     *
     * @param xPercent The x coordinate.
     * @param yPercent The y coordinate.
     * @param text The text to draw.
     * @param textAlignment The text alignment.
     */
    fun drawText(xPercent : Double, yPercent : Double, text : String,
                 textAlignment : TextAlignment = TextAlignment.LEFT)
    {
        this.graphics2D.drawText(xPercent.width, yPercent.height, text, textAlignment)
    }

    /**
     * Draws an image.
     *
     * @param xPercent The x coordinate.
     * @param yPercent The y coordinate.
     * @param image The image to draw.
     */
    fun drawImage(xPercent : Double, yPercent : Double, image : Image)
    {
        this.graphics2D.drawImage(image, xPercent.width, yPercent.height, null)
    }

    /**
     * Draws an image with the given dimensions.
     *
     * @param xPercent The x coordinate.
     * @param yPercent The y coordinate.
     * @param image The image to draw.
     * @param widthPercent The width of the image.
     * @param heightPercent The height of the image.
     */
    fun drawImageFit(xPercent : Double, yPercent : Double, image : Image,
                     widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.drawImage(image,
                                  xPercent.width, yPercent.height,
                                  widthPercent.width, heightPercent.height,
                                  null)
    }

    /**
     * Draws a part of an image.
     *
     * @param xImagePercent1 The x coordinate of the top-left corner of the source.
     * @param yImagePercent1 The y coordinate of the top-left corner of the source.
     * @param xImagePercent2 The x coordinate of the bottom-right corner of the source.
     * @param yImagePercent2 The y coordinate of the bottom-right corner of the source.
     * @param image The image to draw from.
     * @param xDrawPercent1 The x coordinate of the top-left corner of the destination.
     * @param yDrawPercent1 The y coordinate of the top-left corner of the destination.
     * @param xDrawPercent2 The x coordinate of the bottom-right corner of the destination.
     * @param yDrawPercent2 The y coordinate of the bottom-right corner of the destination.
     */
    fun drawImagePart(xImagePercent1 : Double, yImagePercent1 : Double,
                      xImagePercent2 : Double, yImagePercent2 : Double,
                      image : Image,
                      xDrawPercent1 : Double, yDrawPercent1 : Double,
                      xDrawPercent2 : Double, yDrawPercent2 : Double)
    {
        val width = image.getWidth(null)
        val height = image.getHeight(null)
        this.graphics2D.drawImage(image,
                                  xDrawPercent1.width, yDrawPercent1.height,
                                  xDrawPercent2.width, yDrawPercent2.height,
                                  (xImagePercent1 * width).toInt(), (yImagePercent1 * height).toInt(),
                                  (xImagePercent2 * width).toInt(), (yImagePercent2 * height).toInt(),
                                  null)
    }

    /**
     * Draws a part of an image.
     *
     * @param xImage1 The x coordinate of the top-left corner of the source.
     * @param yImage1 The y coordinate of the top-left corner of the source.
     * @param xImage2 The x coordinate of the bottom-right corner of the source.
     * @param yImage2 The y coordinate of the bottom-right corner of the source.
     * @param image The image to draw from.
     * @param xDrawPercent1 The x coordinate of the top-left corner of the destination.
     * @param yDrawPercent1 The y coordinate of the top-left corner of the destination.
     * @param xDrawPercent2 The x coordinate of the bottom-right corner of the destination.
     * @param yDrawPercent2 The y coordinate of the bottom-right corner of the destination.
     */
    fun drawImagePart(xImage1 : Int, yImage1 : Int, xImage2 : Int, yImage2 : Int,
                      image : Image,
                      xDrawPercent1 : Double, yDrawPercent1 : Double, xDrawPercent2 : Double, yDrawPercent2 : Double)
    {
        this.graphics2D.drawImage(image,
                                  xDrawPercent1.width, yDrawPercent1.height, xDrawPercent2.width, yDrawPercent2.height,
                                  xImage1, yImage1, xImage2, yImage2,
                                  null)
    }

    /**
     * Fills a rectangle.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the rectangle.
     * @param heightPercent The height of the rectangle.
     */
    fun fillRectangle(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.fillRect(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }

    /**
     * Fills a rounded rectangle.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the rectangle.
     * @param heightPercent The height of the rectangle.
     * @param arcWidthPercent The width of the arc.
     * @param arcHeightPercent The height of the arc.
     */
    fun fillRoundRectangle(xPercent : Double, yPercent : Double,
                           widthPercent : Double, heightPercent : Double,
                           arcWidthPercent : Double, arcHeightPercent : Double)
    {
        this.graphics2D.fillRoundRect(xPercent.width, yPercent.height,
                                      widthPercent.width, heightPercent.height,
                                      arcWidthPercent.width, arcHeightPercent.height)
    }

    /**
     * Fills an oval.
     *
     * @param xPercent The x coordinate of the top-left corner.
     * @param yPercent The y coordinate of the top-left corner.
     * @param widthPercent The width of the oval.
     * @param heightPercent The height of the oval.
     */
    fun fillOval(xPercent : Double, yPercent : Double, widthPercent : Double, heightPercent : Double)
    {
        this.graphics2D.fillOval(xPercent.width, yPercent.height, widthPercent.width, heightPercent.height)
    }
}
