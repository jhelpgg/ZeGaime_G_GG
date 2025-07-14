package fr.khelp.zegaime.images.raster

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.base.COLOR_BLACK
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.extensions.toUnsignedInt
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents an 8-bit image.
 *
 * **Creation example:**
 * ```kotlin
 * val image = Image8Bit(100, 100)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * image.colorIndex(10, 10, 128)
 * val gameImage = image.toGameImage()
 * ```
 *
 * @property width The width of the image.
 * @property height The height of the image.
 */
class Image8Bit(val width : Int, val height : Int) : RasterImage
{
    companion object
    {
        /**
         * The size of the color table.
         */
        const val COLOR_TABLE_SIZE = 256
    }

    /**Image data*/
    private val data = ByteArray(this.width * this.height)

    /**Color table*/
    private val colorTable = IntArray(Image8Bit.COLOR_TABLE_SIZE)

    init
    {
        if (this.width < 1 || this.height < 1)
        {
            throw IllegalArgumentException(
                "width and height MUST be >0, but specified dimension was ${this.width}x${this.height}")
        }

        this.toGrayColorTable()
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
    override fun imageType() = RasterImageType.IMAGE_8_BITS

    /**
     * Returns the color at the given index in the color table.
     *
     * **Usage example:**
     * ```kotlin
     * val color = image[128]
     * ```
     *
     * @param colorIndex The index of the color in the color table.
     * @return The color.
     */
    operator fun get(colorIndex : Int) = this.colorTable[colorIndex]

    /**
     * Returns the color index of the pixel at the given coordinates.
     *
     * **Usage example:**
     * ```kotlin
     * val index = image.colorIndex(10, 10)
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @return The color index of the pixel.
     */
    fun colorIndex(x : Int, y : Int) : Int
    {
        argumentCheck(x < 0 || x >= this.width || y < 0 || y >= this.height) {"x must be in [0, ${this.width}[ and y in [0, ${this.height}[ but specified point ($x, $y)"}
        return this.data[x + (y * this.width)].toUnsignedInt()
    }

    /**
     * Parses a bitmap stream to the image data.
     *
     * @param inputStream The stream to parse.
     * @throws IOException On reading issue.
     * 
     */
    @Throws(IOException::class)
    fun parseBitmapStream(inputStream : InputStream)
    {
        this.clear()
        val buffer = ByteArray(4)
        var y = this.height - 1
        var x : Int
        var line : Int
        var index : Int

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
                    this.data[x + line] = buffer[index]
                    index++
                    x++
                }
            }

            y--
        }
    }

    /**
     * Parses a compressed bitmap stream to the image data.
     *
     * @param inputStream The stream to parse.
     * @throws IOException On reading issue.
     * 
     */
    @Throws(IOException::class)
    fun parseBitmapStreamCompressed(inputStream : InputStream)
    {
        this.clear()
        val buffer = ByteArray(4)
        var y = this.height - 1
        var x : Int
        var line : Int
        var index : Int
        var count : Int
        var info : Int
        var left : Int
        var up : Int
        var length : Int
        var internBuffer : ByteArray

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
                    count = buffer[index++].toUnsignedInt()
                    info = buffer[index++].toUnsignedInt()

                    if (count > 0)
                    {
                        for (i in 0 until count)
                        {
                            this.data[x + line] = info.toByte()
                            x++

                            if (x >= this.width && i + 1 < count)
                            {
                                x = 0
                                y--
                                line = y * this.width

                                if (y < 0)
                                {
                                    return
                                }
                            }
                        }
                    }
                    else
                    {
                        when (info)
                        {
                            0    -> x = this.width
                            1    -> return
                            2    ->
                            {
                                if (index == 2)
                                {
                                    left = buffer[2].toUnsignedInt()
                                    up = buffer[3].toUnsignedInt()
                                    index = 4
                                }
                                else
                                {
                                    inputStream.readFully(buffer)
                                    left = buffer[0].toUnsignedInt()
                                    up = buffer[1].toUnsignedInt()
                                    index = 2
                                }

                                x += left
                                y -= up

                                if (y < 0)
                                {
                                    return
                                }
                            }

                            else ->
                            {
                                length = info

                                if (length and 1 == 1)
                                {
                                    length++
                                }

                                internBuffer = ByteArray(length)

                                if (index == 2)
                                {
                                    internBuffer[0] = buffer[2]
                                    internBuffer[1] = buffer[3]
                                    inputStream.readNBytes(internBuffer, 2, internBuffer.size - 2)
                                    index = 4
                                }
                                else
                                {
                                    inputStream.readFully(internBuffer)
                                }

                                for (i in 0 until info)
                                {
                                    this.data[x + line] = internBuffer[i]
                                    x++

                                    if (x >= this.width && i + 1 < info)
                                    {
                                        x = 0
                                        y--
                                        line = y * this.width

                                        if (y < 0)
                                        {
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            y--
        }
    }

    /**
     * Sets the color at the given index in the color table.
     *
     * **Usage example:**
     * ```kotlin
     * image[128] = Color.RED.argb
     * ```
     *
     * @param colorIndex The index of the color in the color table.
     * @param color The new color.
     */
    operator fun set(colorIndex : Int, color : Int)
    {
        this.colorTable[colorIndex] = color
    }

    /**
     * Sets the color index of the pixel at the given coordinates.
     *
     * **Usage example:**
     * ```kotlin
     * image.colorIndex(10, 10, 128)
     * ```
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @param colorIndex The new color index of the pixel.
     */
    fun colorIndex(x : Int, y : Int, colorIndex : Int)
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height)
        {
            throw IllegalArgumentException(
                "x must be in [0, " + this.width + "[ and y in [0, " + this.height + "[ but specified point (" + x +
                ", " + y + ")")
        }

        if ((colorIndex < 0) || (colorIndex >= 256))
        {
            throw IllegalArgumentException("colorIndex MUST be in [0, 255], not $colorIndex")
        }

        this.data[x + (y * this.width)] = colorIndex.toByte()
    }

    /**
     * Sets several colors in the color table.
     *
     * **Usage example:**
     * ```kotlin
     * image.colors(0, Color.RED.argb, Color.GREEN.argb, Color.BLUE.argb)
     * ```
     *
     * @param colorIndexStart The index in the color table where to start writing.
     * @param colors The colors to write.
     */
    fun colors(colorIndexStart : Int, vararg colors : Int)
    {
        val limit = Math.min(256 - colorIndexStart, colors.size)
        System.arraycopy(colors, 0, this.colorTable, colorIndexStart, limit)
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

        for (pix in length - 1 downTo 0)
        {
            pixels[pix] = this.colorTable[this.data[pix].toUnsignedInt()]
        }

        val gameImage = GameImage(this.width, this.height)
        gameImage.putPixels(0, 0, this.width, this.height, pixels)
        return gameImage
    }

    /**
     * Converts the color table to a gray scale table.
     */
    fun toGrayColorTable()
    {
        for (i in 0..255)
        {
            this.colorTable[i] = COLOR_BLACK or (i shl 16) or (i shl 8) or i
        }
    }
}
