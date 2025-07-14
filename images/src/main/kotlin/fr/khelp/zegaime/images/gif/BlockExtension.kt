package fr.khelp.zegaime.images.gif

import java.io.IOException
import java.io.InputStream

/**
 * Represents an extension block in a GIF file.
 *
 * @property subType The sub-type of the extension block.
 * 
 */
internal abstract class BlockExtension : Block()
{
    /**Extention sub-type*/
    var subType = 0
        internal set
}

/**
 * Reads an extension block from an input stream.
 *
 * Note: The type is already read and set.
 *
 * @param inputStream The input stream to read from.
 * @return The block read.
 * @throws IOException If the stream does not contain an available sub-type.
 * 
 */
@Throws(IOException::class)
internal fun readBlockExtension(inputStream: InputStream): BlockExtension
{
    val subType = inputStream.read()
    val blockExtension =
            when (subType)
            {
                BLOCK_EXTENSION_GRAPHIC_CONTROL -> GraphicControlBlock()
                BLOCK_EXTENSION_COMMENT         -> CommentBlock()
                BLOCK_EXTENSION_PLAIN_TEXT      -> PlainTextBlock()
                BLOCK_EXTENSION_APPLICATION     -> ApplicationBlock()
                else                            -> throw IOException("Invalid block extension sub type $subType")
            }

    blockExtension.subType = subType
    return blockExtension
}
