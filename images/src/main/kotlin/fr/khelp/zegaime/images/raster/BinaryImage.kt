package fr.khelp.zegaime.images.raster

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.utils.extensions.and
import fr.khelp.zegaime.utils.extensions.or
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents a binary image, which has 2 colors and 1 bit per pixel.
 *
 * **Creation example**
 * ```kotlin
 * val image = BinaryImage(100, 100)
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * image[10, 10] = true
 * val gameImage = image.toGameImage()
 * ```
 *
 * @property width The width of the image.
 * @property height The height of the image.
 * @property background The background color (for 0).
 * @property foreground The foreground color (for 1).
 * @constructor Creates a new binary image.
 */
class BinaryImage(private val width : Int, private val height : Int) : RasterImage
{
    /**
     * Background color (Color for 0)
     */
    var background : Int = BLACK.argb

    /**
     * Image data
     */
    private val data : ByteArray

    /**
     * Foreground color (Color for 1)
     */
    var foreground : Int = WHITE.argb

    init
    {
        if (width < 1 || height < 1)
        {
            throw IllegalArgumentException(
                "width and height MUST be >0, but specified dimension was ${this.width}x${this.height}")
        }

        val size = this.width * this.height
        var length = size shr 3

        if (size and 7 != 0)
        {
            length++
        }

        this.data = ByteArray(length)
    }

    /**
     * Checks if the given position is inside the image.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @throws IllegalArgumentException If (x, y) is outside the image.
     */
    private fun check(x : Int, y : Int)
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            throw IllegalArgumentException(
                "x must be in [0,  ${this.width}[ and y in [0,  ${this.height}[ but specified point ($x, $y)")
        }
    }

    /**
     * Clears the image with a transparent color.
     */
    override fun clear()
    {
        for (i in this.data.indices.reversed())
        {
            this.data[i] = 0.toByte()
        }
    }

    /**
     * Returns the type of the image.
     */
    override fun imageType() = RasterImageType.IMAGE_BINARY

    /**
     * Indicates if the pixel bit at the given coordinates is active or not.
     *
     * **Usage example**
     * ```kotlin
     * val isActive = image[10, 10]
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @return `true` if the pixel bit is active, `false` otherwise.
     */
    operator fun get(x : Int, y : Int) : Boolean
    {
        this.check(x, y)
        val p = x + (y * this.width)
        val pix = p shr 3
        val mask = 1 shl (p and 7)
        return (this.data[pix] and mask) != 0
    }

    /**
     * Parses a bitmap stream to the image data.
     *
     * @param inputStream The stream to parse.
     * @throws IOException On reading issue.
     */
    @Throws(IOException::class)
    fun parseBitmapStream(inputStream : InputStream)
    {
        this.clear()
        val buffer = ByteArray(4)
        var y = this.height - 1
        var index : Int
        var maskRead : Int
        var x : Int
        var line : Int
        var pix : Int

        while (y >= 0)
        {
            line = y * this.width
            x = 0

            while (x < this.width)
            {
                inputStream.readFully(buffer)
                index = 0
                maskRead = 1 shl 7

                while (index < 4 && x < this.width)
                {
                    if (buffer[index] and maskRead != 0)
                    {
                        pix = x + line
                        this.data[pix shr 3] = (this.data[pix shr 3] or (1 shl (pix and 7))).toByte()
                    }

                    x++
                    maskRead = maskRead shr 1

                    if (maskRead == 0)
                    {
                        maskRead = 1 shl 7
                        index++
                    }
                }
            }

            y--
        }
    }

    /**
     * Activates or deactivates a pixel.
     *
     * **Usage example**
     * ```kotlin
     * image[10, 10] = true
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @param on The new active status.
     */
    operator fun set(x : Int, y : Int, on : Boolean)
    {
        this.check(x, y)
        val p = x + (y * this.width)
        val pix = p shr 3
        val mask = 1 shl (p and 7)

        if (on)
        {
            this.data[pix] = (this.data[pix] or mask).toByte()
        }
        else
        {
            this.data[pix] = (this.data[pix] and mask.inv()).toByte()
        }
    }

    /**
     * Switches the activation status of a pixel.
     *
     * **Usage example**
     * ```kotlin
     * image.switchOnOff(10, 10)
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     */
    fun switchOnOff(x : Int, y : Int)
    {
        this.check(x, y)
        val p = x + (y * this.width)
        val pix = p shr 3
        val mask = 1 shl (p and 7)

        if ((this.data[pix] and mask) == 0)
        {
            this.data[pix] = (this.data[pix] or mask).toByte()
        }
        else
        {
            this.data[pix] = (this.data[pix] and mask.inv()).toByte()
        }
    }

    /**
     * Converts the image to a [GameImage].
     *
     * @return The converted image.
     */
    override fun toGameImage() : GameImage
    {
        val length = this.width * this.height
        val pixels = IntArray(length)
        var mask = 1 shl 7
        var pixData = 0
        var info = this.data[0]

        for (pix in 0 until length)
        {
            if (info and mask != 0)
            {
                pixels[pix] = this.foreground
            }
            else
            {
                pixels[pix] = this.background
            }

            mask = mask shr 1

            if (mask == 0)
            {
                mask = 1 shl 7
                pixData++

                if (pixData < this.data.size)
                {
                    info = this.data[pixData]
                }
            }
        }

        val gameImage = GameImage(this.width, this.height)
        gameImage.putPixels(0, 0, this.width, this.height, pixels)
        return gameImage
    }

    /**
     * Returns the height of the image.
     */
    override fun height() = this.height

    /**
     * Returns the width of the image.
     */
    override fun width() = this.width
}