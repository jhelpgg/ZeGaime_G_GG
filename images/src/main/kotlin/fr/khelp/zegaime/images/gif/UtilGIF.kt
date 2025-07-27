package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.utils.extensions.toUnsignedInt
import fr.khelp.zegaime.utils.io.readFully
import java.io.IOException
import java.io.InputStream

/**
 * Appends ASCII bytes to a string builder.
 *
 * This function is for internal use of the image system.
 *
 * @param stringBuilder The string builder to append to.
 * @param data The ASCII bytes to append.
 */
fun appendAsciiBytes(stringBuilder : StringBuilder, data : ByteArray)
{
    for (b in data)
    {
        stringBuilder.append(b.toUnsignedInt()
                                 .toChar())
    }
}

/**
 * Reads a 2-byte integer from an input stream.
 *
 * This function is for internal use of the image system.
 *
 * @param inputStream The input stream to read from.
 * @return The integer read.
 * @throws IOException If the stream closes or reaches the end before the 2 bytes are read.
 */
@Throws(IOException::class)
fun read2ByteInt(inputStream : InputStream) : Int
{
    val data = ByteArray(2)
    val read = inputStream.readFully(data)

    if (read < 2)
    {
        throw IOException("No enough data to read a 2 bytes int")
    }

    return data[0].toUnsignedInt() or (data[1].toUnsignedInt() shl 8)
}

/**
 * Reads an ASCII string from an input stream.
 *
 * This function is for internal use of the image system.
 *
 * @param size The size of the string.
 * @param inputStream The input stream to read from.
 * @return The string read.
 * @throws IOException If the stream closes or ends before reading the entire string.
 */
@Throws(IOException::class)
fun readString(size : Int, inputStream : InputStream) : String
{
    val data = ByteArray(size)
    val read = inputStream.readFully(data)

    if (read < size)
    {
        throw IOException("Not enough data to read a String size $size")
    }

    val chars = CharArray(size)

    for (i in 0 until size)
    {
        chars[i] = data[i].toUnsignedInt()
            .toChar()
    }

    return String(chars)
}