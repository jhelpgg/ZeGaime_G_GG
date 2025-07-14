package fr.khelp.zegaime.images.gif

import java.io.IOException
import java.io.InputStream

/**
 * Represents a comment block in a GIF file.
 *
 * @property comment The comment text.
 * 
 */
internal class CommentBlock : BlockExtension()
{
    /**Comment read*/
    var comment = ""
        private set

    /**
     * Reads the block information from an input stream.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the stream contains invalid comment block extension data.
     */
    @Throws(IOException::class)
    override fun read(inputStream: InputStream)
    {
        val stringBuilder = StringBuilder()
        var subBlock = readSubBlock(inputStream)

        while (subBlock !== EMPTY)
        {
            appendAsciiBytes(stringBuilder, subBlock.data)
            subBlock = readSubBlock(inputStream)
        }

        this.comment = stringBuilder.toString()
    }
}
