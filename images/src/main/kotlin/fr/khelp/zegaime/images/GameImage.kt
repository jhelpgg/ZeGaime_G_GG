package fr.khelp.zegaime.images

import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.DARK_GRAY
import fr.khelp.zegaime.images.color.LIGHT_GRAY
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.images.color.ayuv
import fr.khelp.zegaime.images.color.toArgb
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

class GameImage(val width : Int, val height : Int) : Icon
{
    companion object
    {
        val DUMMY = GameImage(1, 1)
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
        val DARK_LIGHT_PAINT : Paint by lazy {
            TexturePaint(GameImage.DARK_LIGHT.image,
                         Rectangle2D.Double(0.0, 0.0,
                                            GameImage.DARK_LIGHT.width.toDouble(),
                                            GameImage.DARK_LIGHT.height.toDouble()))
        }

        fun load(inputStream : InputStream) =
            this.load(inputStream, -1, -1)

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
     * Save the image in desired image format
     *
     * @param outputStream Stream where write the image
     * @param imageFormat Image format destination
     */
    fun save(outputStream : OutputStream, imageFormat : ImageFormat)
    {
        ImageIO.write(this.image, imageFormat.formatName, outputStream)
    }

    /**
     * Image for draw in a swing component
     */
    internal val image : BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    private val refreshCount = AtomicInteger(0)
    private val refreshFlowData = ObservableSource<Int>(this.refreshCount.getAndIncrement())
    val refreshFlow : Observable<Int> = this.refreshFlowData.observable

    fun clear(color : Color)
    {
        val col = color.argb
        val length = this.width * this.height
        val pixels = IntArray(length) { col }
        this.image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
    }

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
    }

    fun drawPercent(drawer : (PercentGraphics) -> Unit)
    {
        this.draw { graphics -> drawer(PercentGraphics(graphics, this.width, this.height)) }
    }

    fun drawOn(graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    fun drawOn(graphics : Graphics, x : Int, y : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image, x, y, width, height, null)
    }

    fun drawOnPart(graphics : Graphics, x : Int, y : Int, imageX : Int, imageY : Int, width : Int, height : Int)
    {
        graphics.drawImage(this.image,
                           x, y, x + width, y + height,
                           imageX, imageY, imageX + width, imageY + height,
                           null)
    }

    override fun paintIcon(ignored : Component?, graphics : Graphics, x : Int, y : Int)
    {
        graphics.drawImage(this.image, x, y, null)
    }

    override fun getIconWidth() : Int = this.width

    override fun getIconHeight() : Int = this.height

    fun grabPixels(x : Int = 0, y : Int = 0, width : Int = this.width - x, height : Int = this.height - y) : IntArray
    {
        if (x < 0 || width <= 0 || y < 0 || height <= 0 || x + width > this.width || y + height > this.height)
        {
            throw IllegalArgumentException(
                "The image is ${this.width}x${this.height} but its required : ($x, $y) ${width}x$height that is out of bounds")
        }

        val pixels = IntArray(width * height)
        this.image.getRGB(x, y, width, height, pixels, 0, width)
        return pixels
    }

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
    }

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

    fun copy() : GameImage
    {
        val copy = GameImage(this.width, this.height)
        copy.putPixels(0, 0, this.width, this.height, this.grabPixels())
        return copy
    }

    fun copy(image : GameImage)
    {
        argumentCheck(this.width == image.width && this.height == image.height)
        { "Image given have different dimension that this image" }

        this.putPixels(0, 0, this.width, this.height, image.grabPixels())
    }

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
     * Compute the image rotated from 180 degree
     *
     * @return Rotated image
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
     * Compute the image rotated from 270 degree
     *
     * @return Rotated image
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
     * Compute the image rotated from 90 degree
     *
     * @return Rotated image
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
     * Flip the image horizontally and vertically in same time.
     *
     * Visually its same result as :
     *
     * ```kotlin
     *     image.flipHorizontal()
     *     image.flipVertical()
     * ```
     *
     * But it's done faster
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
     * Flip the image horizontally
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
     * Flip the image vertically
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

    fun toBufferedImage() : BufferedImage
    {
        val image = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
        val pixels = this.grabPixels()
        image.setRGB(0, 0, this.width, this.height, pixels, 0, this.width)
        return image
    }

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