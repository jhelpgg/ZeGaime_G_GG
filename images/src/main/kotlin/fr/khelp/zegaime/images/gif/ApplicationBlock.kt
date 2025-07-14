package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.utils.io.ByteArrayStream
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents an application extension block in a GIF file.
 *
 * @property applicationCode The application code.
 * @property applicationData The application data.
 * @property applicationIdentifier The application identifier.
 * 
 */
internal class ApplicationBlock : BlockExtension()
{
    /**Application code*/
    val applicationCode = ByteArray(3)

    /**Application data*/
    val applicationData = ByteArrayStream()

    /**Application identifier*/
    var applicationIdentifier = ""
        private set

    /**
     * Reads the block from an input stream.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the stream contains invalid data for an application extension block.
     */
    @Throws(IOException::class)
    override fun read(inputStream : InputStream)
    {
        val size = inputStream.read()

        if (size != 11)
        {
            throw IOException("Size of application block MUST be 11, not $size")
        }

        this.applicationIdentifier = readString(8, inputStream)
        inputStream.readFully(this.applicationCode)
        var subBlock = readSubBlock(inputStream)

        while (subBlock !== EMPTY)
        {
            this.applicationData.write(subBlock.data)
            subBlock = readSubBlock(inputStream)
        }
    }
}
