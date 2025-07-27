package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Represents a sub-block in a GIF file.
 *
 * This class is for internal use of the image system.
 *
 * @property size The size of the sub-block data.
 * @property data The data of the sub-block.
 * @constructor Creates a new sub-block.
 */
internal data class SubBlock(val size : Int, val data : ByteArray)

/**
 * An empty sub-block.
 *
 * This object is for internal use of the image system.
 */
internal val EMPTY = SubBlock(0, ByteArray(0))

/**
 * Reads the next sub-block from an input stream.
 *
 * This function is for internal use of the image system.
 *
 * @param inputStream The input stream to read from.
 * @return The sub-block read.
 * @throws IOException If the data does not correspond to a valid sub-block.
 */
@Throws(IOException::class)
internal fun readSubBlock(inputStream : InputStream) : SubBlock
{
    val size = inputStream.read()

    if (size < 0)
    {
        throw IOException("Not enough data to read a SubBlock (No size !)")
    }

    if (size == 0)
    {
        return EMPTY
    }

    val data = ByteArray(size)
    val read = inputStream.readFully(data)

    if (read < size)
    {
        throw IOException("Not enough data to read a SubBlock (read=$read, size=$size)")
    }

    return SubBlock(size, data)
}