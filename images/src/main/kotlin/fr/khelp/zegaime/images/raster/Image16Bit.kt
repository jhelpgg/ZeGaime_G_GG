package fr.khelp.zegaime.images.raster

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.base.COLOR_BLACK
import fr.khelp.zegaime.utils.extensions.and
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents a 16-bit image.
 *
 * **Creation example:**
 * ```kotlin
 * val image = Image16Bit(100, 100)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * image[10, 10] = Color.RED.argb
 * val gameImage = image.toGameImage()
 * ```
 *
 * @property width The width of the image.
 * @property height The height of the image.
 * @constructor Creates a new 16-bit image.
 */
class Image16Bit(val width: Int, val height: Int) : RasterImage
{
    /**Image data*/
    private val data = IntArray(this.width * this.height)

    init
    {
        if (this.width < 1 || this.height < 1)
        {
            throw IllegalArgumentException(
                "width and height MUST be >0, but specified dimension was ${this.width}x${this.height}")
        }
    }

    /**
     * Clears the image with a transparent color.
     */
    override fun clear()
    {
        for (i in this.data.indices.reversed())
        {
            this.data[i] = 0
        }
    }

    /**
     * Returns the width of the image.
     */
    override fun width() = this.width

    /**
     * Returns the height of the image.
     */
    override fun height() = this.height

    /**
     * Returns the type of the image.
     */
    override fun imageType() = RasterImageType.IMAGE_16_BITS

    /**
     * Converts the image to a [GameImage].
     *
     * @return The converted image.
     */
    override fun toGameImage(): GameImage
    {
        val gameImage = GameImage(this.width, this.height)
        gameImage.putPixels(0, 0, this.width, this.height, this.data)
        return gameImage
    }

    /**
     * Returns the color of the pixel at the given coordinates.
     *
     * **Usage example:**
     * ```kotlin
     * val color = image[10, 10]
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @return The color of the pixel.
     */
    operator fun get(x: Int, y: Int): Int
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            throw IllegalArgumentException(
                "x must be in [0, ${this.width}[ and y in [0, ${this.height}[ but specified point ($x, $y)")
        }

        return this.data[x + (y * this.width)]
    }

    /**
     * Parses a bitmap stream to the image data.
     *
     * @param inputStream The stream to parse.
     * @throws IOException On reading issue.
     */
    @Throws(IOException::class)
    fun parseBitmapStream(inputStream: InputStream)
    {
        this.clear()
        val buffer = ByteArray(4)
        var y = this.height - 1
        var red: Int
        var green: Int
        var blue: Int
        var x: Int
        var line: Int
        var index: Int

        while (y >= 0)
        {
            line = y * this.width
            x = 0
            while (x < this.width)
            {
                inputStream.readFully(buffer)
                index = 0

                while (index < 4 && x < this.width)
                {
                    red = (buffer[index] and 0xF0) shr 4
                    green = buffer[index] and 0xF
                    index++
                    blue = (buffer[index] and 0xF0) shr 4
                    index++
                    this.data[x + line] = COLOR_BLACK or (red shl 20) or (green shl 12) or (blue shl 4)
                    x++
                }
            }

            y--
        }
    }

    /**
     * Sets the color of the pixel at the given coordinates.
     *
     * **Usage example:**
     * ```kotlin
     * image[10, 10] = Color.RED.argb
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @param color The new color of the pixel.
     */
    operator fun set(x: Int, y: Int, color: Int)
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            throw IllegalArgumentException(
                "x must be in [0, ${this.width}[ and y in [0, ${this.height}[ but specified point ($x, $y)")
        }

        this.data[x + (y * this.width)] = color
    }
}