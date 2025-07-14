package fr.khelp.zegaime.images.gif

import java.io.IOException
import java.io.InputStream

/**
 * Represents a block of information in a GIF file.
 *
 * @property type The type of the block.
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * 
 */
internal abstract class Block
{
    /**Block type*/
    var type = 0
        internal set

    /**
     * Reads the block specific data from an input stream.
     *
     * Note: The type and eventual sub-type are already read and set.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException If the stream does not contain valid data for the target block.
     */
    @Throws(IOException::class)
    internal abstract fun read(inputStream: InputStream)
}

/**
 * Represents an ignored block, which can appear in some malformed GIF files.
 * 
 */
internal object IgnoreBlock : Block()
{
    /**
     * Reads the block.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException Never happens.
     */
    @Throws(IOException::class)
    override fun read(inputStream: InputStream) = Unit
}

/**
 * Represents the end of a GIF stream.
 *
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 * 
 */
internal object EndBlock : Block()
{
    /**
     * Reads the block.
     *
     * @param inputStream The input stream to read from.
     * @throws IOException Never happens.
     */
    @Throws(IOException::class)
    override fun read(inputStream: InputStream) = Unit
}

/**
 * Reads the next block from an input stream.
 *
 * @param inputStream The input stream to read from.
 * @param colorResolution The color resolution.
 * @return The block read.
 * @throws IOException If the stream does not contain a valid block.
 * 
 */
@Throws(IOException::class)
internal fun readBlock(inputStream: InputStream, colorResolution: Int): Block
{
    val type = inputStream.read()
    val block =
            when (type)
            {
                BLOCK_IMAGE_DESCRIPTOR -> ImageDescriptorBlock(colorResolution)
                BLOCK_EXTENSION        -> readBlockExtension(inputStream)
                BLOCK_END_GIF          -> EndBlock
                0                      -> IgnoreBlock
                else                   -> throw IOException("Unknown block type : $type")
            }

    block.type = type
    block.read(inputStream)
    return block
}
