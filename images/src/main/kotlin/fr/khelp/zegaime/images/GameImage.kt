package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.DARK_GRAY
import fr.khelp.zegaime.images.color.LIGHT_GRAY
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.images.color.ayuv
import fr.khelp.zegaime.images.color.toArgb
import fr.khelp.zegaime.images.raster.RasterImage
import fr.khelp.zegaime.images.raster.RasterImageType
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.ui.FONT_RENDER_CONTEXT
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Paint
import java.awt.RenderingHints
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import javax.swing.Icon
import kotlin.math.max
import kotlin.math.min

/**
 * Represents an image that can be manipulated and drawn on the screen.
 *
 * **Creation example:**
 * ```kotlin
 * val image = GameImage(100, 100)
 * image.clear(Color.RED)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val image = GameImage.load(inputStream)
 * image.draw { graphics ->
 *     graphics.color = Color.BLUE
 *     graphics.fillRect(10, 10, 80, 80)
 * }
 * ```
 *
 * @property width The width of the image.
 * @property height The height of the image.
 * @property refreshFlow An observable that emits a value when the image is refreshed.
 */
class GameImage(val width : Int, val height : Int) : RasterImage,
                                                     Icon
{
    companion object
    {
        /** A dummy 1x1 image. */
        val DUMMY = GameImage(1, 1)
        /** A 32x32 image with a dark and light gray checkerboard pattern. */
        val DARK_LIGHT : GameImage by lazy {
            val gameImage = GameImage(32, 32)
            gameImage.clear(LIGHT_GRAY)
            gameImage.drawPercent { percentGraphics ->
                percentGraphics.color = DARK_GRAY
                percentGraphics.fillRectangle(0.0, 0.0, 0.5, 0.5)
                percentGraphics.fillRectangle(0.5, 0.5, 0.5, 0.5)
            }
            gameImage
        }
        /** A paint that uses the [DARK_LIGHT] image as a texture. */
        val DARK_LIGHT_PAINT : Paint by lazy {
            TexturePaint(GameImage.DARK_LIGHT.image,
                         Rectangle2D.Double(0.0, 0.0,
                                            GameImage.DARK_LIGHT.width.toDouble(),
                                            GameImage.DARK_LIGHT.height.toDouble()))
        }

        /**
         * Loads an image from an input stream.
         *
         * **Usage example:**
         * ```kotlin
         * val image = GameImage.load(inputStream)
         * ```
         *
         * @param inputStream The input stream to load the image from.
         * @return The loaded image.
         */
        fun load(inputStream : InputStream) =
            this.load(inputStream, -1, -1)

        /**
         * Loads a thumbnail of an image from an input stream.
         *
         * **Usage example:**
         * ```kotlin
         * val thumbnail = GameImage.loadThumbnail(inputStream, 50, 50)
         * ```
         *
         * @param inputStream The input stream to load the image from.
         * @param imageWidth The width of the thumbnail.
         * @param imageHeight The height of the thumbnail.
         * @return The loaded thumbnail.
         */
        fun loadThumbnail(inputStream : InputStream, imageWidth : Int, imageHeight : Int) =
            this.load(inputStream, imageWidth, imageHeight)

        private fun load(inputStream : InputStream, imageWidth : Int, imageHeight : Int) : GameImage =
            try
            {
                val image = ImageIO.read(inputStream)
                val width = image.width
                val height = image.height
                val targetWidth = if (imageWidth > 0) imageWidth else width
                val targetHeight = if (imageHeight > 0) imageHeight else height

                if (width <= 0 || height <= 0)
                {
                    GameImage.DUMMY
                }
                else
                {
                    val gameImage = GameImage(targetWidth, targetHeight)
                    gameImage.clear(TRANSPARENT)
                    gameImage.draw { graphics2D ->
                        graphics2D.drawImage(image,
                                             0, 0, targetWidth, targetHeight,
                                             0, 0, width, height,
                                             null)
                    }
                    gameImage.refresh()
                    gameImage
                }
            }
            catch (exception : Exception)
            {
                GameImage.DUMMY
            }
    }

    /**
     * Saves the image to an output stream in the specified format.
     *
     * **Usage example:**
     * ```kotlin
     * image.save(outputStream, ImageFormat.PNG)
     * ```
     *
     * @param outputStream The output stream to save the image to.
     * @param imageFormat The format to save the image in.
     */
    fun save(outputStream : OutputStream, imageFormat : ImageFormat)
    {
        ImageIO.write(this.image, imageFormat.formatName, outputStream)
    }

    /**
     * The underlying buffered image.
     * 
     */
    internal val image : BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    private val refreshCount = AtomicInteger(0)
    private val refreshFlowData = ObservableSource<Int>(this.refreshCount.getAndIncrement())
    val refreshFlow : Observable<Int> = this.refreshFlowData.observable

    /**
     * Clears the image with the given color.
     *
     * **Usage example:**
     * ```kotlin
     * image.clear(Color.RED)
     * ```
     *
     * @param color The color to clear the image with.
     */
    fun clear(color : Color)
    {
        val col = color.argb
        val length = this.width * this.height
        val pixels = IntArray(length) { col }
        this.image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
        this.refresh()
    }

    /**
     * Draws on the image using a [Graphics2D] context.
     *
     * **Usage example:**
     * ```kotlin
     * image.draw { graphics ->
     *     graphics.color = Color.BLUE
     *     graphics.fillRect(10, 10, 80, 80)
     * }
     * ```
     *
     * @param drawer A lambda function that takes a [Graphics2D] context and draws on it.
     */
    fun draw(drawer : (Graphics2D) -> Unit)
    {
        val graphics = this.image.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                  RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)

        if (FONT_RENDER_CONTEXT.isAntiAliased)
        {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        }
        else
        {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        }

        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        if (FONT_RENDER_CONTEXT.usesFractionalMetrics())
        {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                      RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        }
        else
        {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                      RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        }

        drawer(graphics)
        graphics.dispose()
        this.refresh()
    }

    /**
     * Draws on the image using a [PercentGraphics] context.
     *
     * **Usage example:**
     * ```kotlin
     * image.drawPercent { percentGraphics ->
     *     percentGraphics.color = Color.BLUE
     *     percentGraphics.fillRectangle(0.1, 0.1, 0.8, 0.8)
     * }
     * ```
     *
     * @param drawer A lambda function that takes a [PercentGraphics] context and draws on it.
     */
    fun drawPercent(drawer : (PercentGraphics) -> Unit)
    {
        this.draw { graphics -> drawer(PercentGraphics(graphics, this.width, this.height)) }
    }

    /**
     * Draws the image on a [Graphics] context at the specified coordinates.
     *
     * **Usage example:**
     * ```kotlin
     * image.drawOn(graphics, 10, 10)
     * ```
     *
     * @param graphics The graphics context to draw on.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    fun drawOn(graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    /**
     * Draws the image on a [Graphics] context at the specified coordinates and with the specified dimensions.
     *
     * **Usage example:**
     * ```kotlin
     * image.drawOn(graphics, 10, 10, 50, 50)
     * ```
     *
     * @param graphics The graphics context to draw on.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param width The width.
     * @param height The height.
     */
    fun drawOn(graphics : Graphics, x : Int, y : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image, x, y, width, height, null)
    }

    /**
     * Draws a part of the image on a [Graphics] context.
     *
     * **Usage example:**
     * ```kotlin
     * image.drawOnPart(graphics, 10, 10, 20, 20, 30, 30)
     * ```
     *
     * @param graphics The graphics context to draw on.
     * @param x The x coordinate of the destination.
     * @param y The y coordinate of the destination.
     * @param imageX The x coordinate of the source.
     * @param imageY The y coordinate of the source.
     * @param width The width of the part to draw.
     * @param height The height of the part to draw.
     */
    fun drawOnPart(graphics : Graphics, x : Int, y : Int, imageX : Int, imageY : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image,
                           x, y, x + width, y + height,
                           imageX, imageY, imageX + width, imageY + height,
                           null)
    }

    /**
     * Paints the icon at the specified location.
     *
     * @param ignored The component to paint on.
     * @param graphics The graphics context.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    override fun paintIcon(ignored : Component?, graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    /**
     * Returns the icon's width.
     */
    override fun getIconWidth() : Int = this.width

    /**
     * Returns the icon's height.
     */
    override fun getIconHeight() : Int = this.height

    /**
     * Grabs the pixels of a part of the image.
     *
     * **Usage example:**
     * ```kotlin
     * val pixels = image.grabPixels(10, 10, 20, 20)
     * ```
     *
     * @param x The x coordinate of the top-left corner of the part to grab.
     * @param y The y coordinate of the top-left corner of the part to grab.
     * @param width The width of the part to grab.
     * @param height The height of the part to grab.
     * @param offset The offset in the returned array.
     * @return An array of the pixels.
     */
    fun grabPixels(x : Int = 0,
                   y : Int = 0,
                   width : Int = this.width - x,
                   height : Int = this.height - y,
                   offset : Int = 0) : IntArray
    {
        if (x < 0 || width <= 0 || y < 0 || height <= 0 || x + width > this.width || y + height > this.height)
        {
            throw IllegalArgumentException(
                "The image is ${this.width}x${this.height} but its required : ($x, $y) ${width}x$height that is out of bounds")
        }

        val pixels = IntArray(width * height)
        this.image.getRGB(x, y, width, height, pixels, offset, width)
        return pixels
    }

    /**
     * Puts pixels on the image.
     *
     * **Usage example:**
     * ```kotlin
     * image.putPixels(10, 10, 20, 20, pixels)
     * ```
     *
     * @param x The x coordinate of the top-left corner.
     * @param y The y coordinate of the top-left corner.
     * @param width The width of the area to put the pixels on.
     * @param height The height of the area to put the pixels on.
     * @param pixels The pixels to put.
     */
    fun putPixels(x : Int, y : Int, width : Int, height : Int, pixels : IntArray)
    {
        if (x < 0 || width <= 0 || y < 0 || height <= 0 || x + width > this.width || y + height > this.height)
        {
            throw IllegalArgumentException(
                "The image is ${this.width}x${this.height} but its required : ($x, $y) ${width}x$height that is out of bounds")
        }

        if (pixels.size != width * height)
        {
            throw IllegalArgumentException(
                "Pixels array must have size $width*$height=${width * height}, but it is ${pixels.size}")
        }

        this.image.setRGB(x, y, width, height, pixels, 0, width)
        this.refresh()
    }

    /**
     * Resizes the image.
     *
     * **Usage example:**
     * ```kotlin
     * val resizedImage = image.resize(50, 50)
     * ```
     *
     * @param targetWidth The new width.
     * @param targetHeight The new height.
     * @return The resized image.
     */
    fun resize(targetWidth : Int, targetHeight : Int) : GameImage
    {
        val width = max(1, targetWidth)
        val height = max(1, targetHeight)

        if (this.width == width && this.height == height)
        {
            return this
        }

        val resized = GameImage(width, height)
        resized.clear(TRANSPARENT)
        resized.drawPercent { percentGraphics -> percentGraphics.drawImageFit(0.0, 0.0, this.image, 1.0, 1.0) }
        return resized
    }

    /**
     * Creates a copy of the image.
     *
     * **Usage example:**
     * ```kotlin
     * val copy = image.copy()
     * ```
     *
     * @return A copy of the image.
     */
    fun copy() : GameImage
    {
        val copy = GameImage(this.width, this.height)
        copy.putPixels(0, 0, this.width, this.height, this.grabPixels())
        return copy
    }

    /**
     * Copies the given image to this image.
     *
     * **Usage example:**
     * ```kotlin
     * image.copy(otherImage)
     * ```
     *
     * @param image The image to copy.
     */
    fun copy(image : GameImage)
    {
        argumentCheck(this.width == image.width && this.height == image.height)
        { "Image given have different dimension that this image" }

        this.putPixels(0, 0, this.width, this.height, image.grabPixels())
    }

    /**
     * Converts the image to grayscale.
     *
     * **Usage example:**
     * ```kotlin
     * image.gray()
     * ```
     */
    fun gray()
    {
        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val (alpha, y, _, _) = pixels[index].ayuv
                pixels[index] = (alpha shl 24) or (y shl 16) or (y shl 8) or y
            }
        }
    }

    /**
     * Tints the image with the given color.
     *
     * **Usage example:**
     * ```kotlin
     * image.tint(Color.RED)
     * ```
     *
     * @param color The color to tint the image with.
     */
    fun tint(color : Color)
    {
        val (_, red, green, blue) = color.toArgb

        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val (alpha, y, _, _) = pixels[index].ayuv
                pixels[index] =
                    (alpha shl 24) or (((red * y) shr 8) shl 16) or (((green * y) shr 8) shl 8) or ((blue * y) shr 8)
            }
        }
    }

    /**
     * Colorizes the image with the given color.
     *
     * **Usage example:**
     * ```kotlin
     * image.colorize(Color.RED)
     * ```
     *
     * @param color The color to colorize the image with.
     */
    fun colorize(color : Color)
    {
        val colorPart = color.argb and 0x00_FF_FF_FF

        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val col = pixels[index]
                pixels[index] =
                    (col and 0xFF000000.toInt()) or colorPart
            }
        }
    }

    /**
     * Changes the contrast of the image.
     *
     * **Usage example:**
     * ```kotlin
     * image.contrast(1.5)
     * ```
     *
     * @param contrast The contrast factor.
     */
    fun contrast(contrast : Double)
    {
        this.manipulatePixels { pixels ->
            for (index in pixels.indices)
            {
                val (alpha, y, u, v) = pixels[index].ayuv
                pixels[index] = ayuv(alpha, (y * contrast + 0.5).toInt(), u, v).argb
            }
        }
    }

    /**
     * Rotates the image by 180 degrees.
     *
     * **Usage example:**
     * ```kotlin
     * val rotatedImage = image.rotate180()
     * ```
     *
     * @return The rotated image.
     */
    fun rotate180() : GameImage
    {
        val source = this.grabPixels()
        val width = this.width
        val height = this.height
        val length = width * height
        val rotated = IntArray(length)

        var pixelThis = 0
        var pixelRotated = length - 1

        while (pixelRotated >= 0)
        {
            rotated[pixelRotated] = source[pixelThis]
            pixelThis++
            pixelRotated--
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Rotates the image by 270 degrees.
     *
     * **Usage example:**
     * ```kotlin
     * val rotatedImage = image.rotate270()
     * ```
     *
     * @return The rotated image.
     */
    fun rotate270() : GameImage
    {
        val source = this.grabPixels()
        val width = this.height
        val height = this.width
        val rotated = IntArray(width * height)

        var xRotated = width - 1
        var pixelRotated = xRotated

        var pixelThis = 0

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                rotated[pixelRotated] = source[pixelThis]

                pixelThis++
                pixelRotated += width
            }

            xRotated--
            pixelRotated = xRotated
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Rotates the image by 90 degrees.
     *
     * **Usage example:**
     * ```kotlin
     * val rotatedImage = image.rotate90()
     * ```
     *
     * @return The rotated image.
     */
    fun rotate90() : GameImage
    {
        val source = this.grabPixels()
        val width = this.height
        val height = this.width
        val rotated = IntArray(width * height)

        var xRotated = 0
        val stepRotated = -width
        val startRotated = (height - 1) * width
        var pixelRotated = startRotated

        var pixelThis = 0

        for (y in 0 until this.height)
        {
            for (x in 0 until this.width)
            {
                rotated[pixelRotated] = source[pixelThis]

                pixelThis++
                pixelRotated += stepRotated
            }

            xRotated++
            pixelRotated = startRotated + xRotated
        }

        val result = GameImage(width, height)
        result.putPixels(0, 0, width, height, rotated)
        return result
    }

    /**
     * Flips the image horizontally and vertically at the same time.
     *
     * Visually, it's the same result as:
     *
     * ```kotlin
     * image.flipHorizontal()
     * image.flipVertical()
     * ```
     *
     * But it's done faster.
     *
     * **Usage example:**
     * ```kotlin
     * image.flipBoth()
     * ```
     */
    fun flipBoth()
    {
        this.manipulatePixels { pixels ->
            val length = pixels.size
            val middlePixel = length shr 1
            var color : Int

            var pixelStart = 0
            var pixelEnd = length - 1

            while (pixelStart < middlePixel)
            {
                color = pixels[pixelStart]
                pixels[pixelStart] = pixels[pixelEnd]
                pixels[pixelEnd] = color
                pixelStart++
                pixelEnd--
            }
        }
    }

    /**
     * Flips the image horizontally.
     *
     * **Usage example:**
     * ```kotlin
     * image.flipHorizontal()
     * ```
     */
    fun flipHorizontal()
    {
        this.manipulatePixels { pixels ->
            val middleX = this.width shr 1
            var line = 0
            var pixelLeft : Int
            var pixelRight : Int
            var color : Int

            for (y in 0 until this.height)
            {
                pixelLeft = line
                pixelRight = line + this.width - 1

                for (x in 0 until middleX)
                {
                    color = pixels[pixelLeft]
                    pixels[pixelLeft] = pixels[pixelRight]
                    pixels[pixelRight] = color

                    pixelLeft++
                    pixelRight--
                }

                line += this.width
            }
        }
    }

    /**
     * Flips the image vertically.
     *
     * **Usage example:**
     * ```kotlin
     * image.flipVertical()
     * ```
     */
    fun flipVertical()
    {
        this.manipulatePixels { pixels ->
            val middleY = this.height shr 1
            var lineTop = 0
            var lineBottom = (this.height - 1) * this.width
            val line = IntArray(this.width)

            for (y in 0 until middleY)
            {
                System.arraycopy(pixels, lineTop, line, 0, this.width)
                System.arraycopy(pixels, lineBottom, pixels, lineTop, this.width)
                System.arraycopy(line, 0, pixels, lineBottom, this.width)

                lineTop += this.width
                lineBottom -= this.width
            }
        }
    }

    /**
     * Converts the image to a [BufferedImage].
     *
     * **Usage example:**
     * ```kotlin
     * val bufferedImage = image.toBufferedImage()
     * ```
     *
     * @return The converted image.
     */
    fun toBufferedImage() : BufferedImage
    {
        val image = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
        val pixels = this.grabPixels()
        image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
        return image
    }

    /**
     * Clears a rectangle in the image with the given color.
     *
     * **Usage example:**
     * ```kotlin
     * image.clearRectangle(10, 10, 20, 20, Color.RED.argb)
     * ```
     *
     * @param imageX The x coordinate of the top-left corner of the rectangle.
     * @param imageY The y coordinate of the top-left corner of the rectangle.
     * @param imageWidth The width of the rectangle.
     * @param imageHeight The height of the rectangle.
     * @param color The color to clear the rectangle with.
     */
    fun clearRectangle(imageX : Int, imageY : Int, imageWidth : Int, imageHeight : Int, color : Int)
    {
        val minX = imageX.coerceIn(0 until this.width)
        val minY = imageY.coerceIn(0 until this.height)
        val realWidth = min(imageWidth, this.width - minX)
        val realHeight = min(imageHeight, this.height - minY)

        if (realWidth <= 0 || realHeight <= 0)
        {
            return
        }

        this.manipulatePixels { pixels ->
            var line = minX + minY * this.width

            for (y in 0 until realHeight)
            {
                var pixel = line

                for (x in 0 until realWidth)
                {
                    pixels[pixel] = color
                    pixel++
                }

                line += this.width
            }
        }
    }

    /**
     * Clears the image with a transparent color.
     */
    override fun clear()
    {
        this.clear(TRANSPARENT)
    }

    /**
     * Returns the width of the image.
     */
    override fun width() : Int = this.width

    /**
     * Returns the height of the image.
     */
    override fun height() : Int = this.height

    /**
     * Returns the type of the image.
     */
    override fun imageType() : RasterImageType = RasterImageType.GAME_IMAGE

    /**
     * Returns the image itself.
     */
    override fun toGameImage() : GameImage = this

    private fun manipulatePixels(pixelsModifier : (IntArray) -> Unit)
    {
        val pixels = this.grabPixels()
        pixelsModifier(pixels)
        this.putPixels(0, 0, this.width, this.height, pixels)
    }

    internal fun refresh()
    {
        this.image.flush()
        this.refreshFlowData.value = this.refreshCount.getAndIncrement()
    }
}
