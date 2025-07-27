package fr.khelp.zegaime.images.gif

import java.io.IOException
import java.io.InputStream

/**
 * Represents a graphic control extension block in a GIF file.
 *
 * This class is for internal use of the image system.
 *
 * @property disposalMethod The disposal method.
 * @property transparencyIndex The transparency index.
 * @constructor Creates a new graphic control block.
 */
internal class GraphicControlBlock : BlockExtension()
{
    /**
     * Disposal method
     */
    var disposalMethod: Int = 0
        private set
    /**
     * Image duration time in milliseconds
     */
    private var time: Long = 0
    /**
     * Transparency index
     */
    var transparencyIndex: Int = 0
        private set

    /**
     * Reads the stream to extract the block information.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the stream does not contain valid data for a graphic control block extension.
     */
    @Throws(IOException::class)
    override fun read(inputStream: InputStream)
    {
        val size = inputStream.read()

        if (size != 4)
        {
            throw IOException("Size of graphic control MUST be 4, not $size")
        }

        val flags = inputStream.read()

        if (flags < 0)
        {
            throw IOException("No enough data for read flags of graphic control block")
        }

        this.disposalMethod = flags and MASK_DISPOSAL_METHOD shr SHIFT_DISPOSAL_METHOD
        val transparencyIndexGiven = flags and MASK_TRANSPARENCY_GIVEN != 0

        this.time = 10L * read2ByteInt(inputStream)
        this.transparencyIndex = inputStream.read()

        if (!transparencyIndexGiven)
        {
            this.transparencyIndex = -1
        }

        // Consume the block terminator

        inputStream.read()
    }

    /**
     * Returns the image time.
     *
     * @param defaultTime The default time to return if the time is undefined.
     * @return The image time in milliseconds.
     */
    fun time(defaultTime: Long) = if (this.time == 0L) defaultTime else this.time
}