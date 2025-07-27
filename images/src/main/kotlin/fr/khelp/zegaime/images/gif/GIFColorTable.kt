package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.images.color.base.COLOR_BLACK
import fr.khelp.zegaime.images.color.base.COLOR_WHITE
import fr.khelp.zegaime.utils.extensions.shl
import fr.khelp.zegaime.utils.extensions.toUnsignedInt
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents a color table in a GIF file.
 *
 * This class is for internal use of the image system.
 *
 * @property colorResolution The color resolution of the table.
 * @property ordered Indicates if the table is ordered.
 * @property size The size of the table.
 * @constructor Creates a new GIF color table.
 */
class GIFColorTable(val colorResolution : Int, val ordered : Boolean, size : Int)
{
    /**Table size*/
    val size = Math.max(2, size)

    /**Colors table*/
    private val colors = IntArray(this.size)

    /**
     * Returns the color at the given index.
     *
     * @param index The index of the color.
     * @return The color.
     */
    fun color(index : Int) = this.colors[index % this.size]

    /**
     * Initializes the color table to default values.
     */
    fun initializeDefault()
    {
        this.colors[0] = COLOR_BLACK
        this.colors[1] = COLOR_WHITE

        val step = 256 / (1 shl this.colorResolution)
        var red = 0
        var green = 0
        var blue = step

        for (i in 2 until this.size)
        {
            this.colors[i] = COLOR_BLACK or (red shl 16) or (green shl 8) or blue
            blue += step

            if (blue > 255)
            {
                blue = 0
                green += step

                if (green > 255)
                {
                    green = 0
                    red = red + step and 0xFF
                }
            }
        }
    }

    /**
     * Reads the color table from an input stream.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the stream does not contain a valid color table.
     */
    @Throws(IOException::class)
    fun read(inputStream : InputStream)
    {
        val total = this.size * 3
        val data = ByteArray(total)
        val read = inputStream.readFully(data)

        if (read < total)
        {
            throw IOException("No enough data to read the color table")
        }

        var pix = 0
        var part = 0

        while (pix < this.size)
        {
            this.colors[pix] = COLOR_BLACK or
                    (data[part] shl 16) or
                    (data[part + 1] shl 8) or
                    data[part + 2].toUnsignedInt()
            pix++
            part += 3
        }
    }
}