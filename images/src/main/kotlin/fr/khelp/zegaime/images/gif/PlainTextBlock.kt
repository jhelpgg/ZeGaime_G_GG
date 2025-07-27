package fr.khelp.zegaime.images.gif

import java.io.IOException
import java.io.InputStream

/**
 * Represents a plain text block in a GIF file.
 *
 * This class is for internal use of the image system.
 *
 * @property backgroundIndex The background color index.
 * @property cellHeight The cell height.
 * @property cellWidth The cell width.
 * @property foregroundIndex The foreground color index.
 * @property gridHeight The grid height.
 * @property gridWidth The grid width.
 * @property gridX The grid x coordinate.
 * @property gridY The grid y coordinate.
 * @property text The text to print.
 * @constructor Creates a new plain text block.
 */
internal class PlainTextBlock : BlockExtension()
{
    /**
     * Background color index
     */
    var backgroundIndex = 0
        private set
    /**
     * Cell height
     */
    var cellHeight = 0
        private set
    /**
     * Cell width
     */
    var cellWidth = 0
        private set
    /**
     * Foreground color index
     */
    var foregroundIndex = 0
        private set
    /**
     * Grid height
     */
    var gridHeight = 0
        private set
    /**
     * Grid width
     */
    var gridWidth = 0
        private set
    /**
     * Grid X
     */
    var gridX = 0
        private set
    /**
     * Grid Y
     */
    var gridY = 0
        private set
    /**
     * Text to print
     */
    var text = ""
        private set

    /**
     * Reads the plain text extension block from an input stream.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the data is not a valid plain text extension block.
     */
    @Throws(IOException::class)
    override fun read(inputStream: InputStream)
    {
        val size = inputStream.read()

        if (size != 12)
        {
            throw IOException("Size of plain text MUST be 12, not $size")
        }

        this.gridX = read2ByteInt(inputStream)
        this.gridY = read2ByteInt(inputStream)
        this.gridWidth = read2ByteInt(inputStream)
        this.gridHeight = read2ByteInt(inputStream)
        this.cellWidth = inputStream.read()
        this.cellHeight = inputStream.read()
        this.foregroundIndex = inputStream.read()
        this.backgroundIndex = inputStream.read()

        val stringBuilder = StringBuilder()
        var subBlock = readSubBlock(inputStream)

        while (subBlock !== EMPTY)
        {
            appendAsciiBytes(stringBuilder, subBlock.data)
            subBlock = readSubBlock(inputStream)
        }

        this.text = stringBuilder.toString()
    }
}