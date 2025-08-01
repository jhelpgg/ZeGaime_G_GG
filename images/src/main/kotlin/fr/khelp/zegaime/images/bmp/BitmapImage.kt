package fr.khelp.zegaime.images.bmp

import fr.khelp.zegaime.images.raster.RasterImage
import fr.khelp.zegaime.utils.extensions.ifElse
import fr.khelp.zegaime.utils.io.treatInputStream
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Optional

/**
 * Bitmap image.
 *
 * This class is for internal use of the image system.
 *
 * @param bitmapHeader Bitmap header.
 * @param rasterImage Raster image.
 */
class BitmapImage internal constructor(val bitmapHeader : BitmapHeader, val rasterImage : RasterImage)
{
    /**Image width*/
    val width = this.rasterImage.width()

    /**Image height*/
    val height = this.rasterImage.height()

    /**Image can be drawn*/
    fun toJHelpImage() = this.rasterImage.toGameImage()
}

/**
 * Read bitmap header from stream.
 *
 * @param inputStream Stream to parse.
 * @param jumpHeader Indicates to jump (or not) bitmap header specific part.
 * @return Bitmap header read.
 * @throws IOException If the stream not contains a valid bitmap header.
 */
@Throws(IOException::class)
fun readBitmapHeader(inputStream : InputStream, jumpHeader : Boolean = false) = BitmapHeader(inputStream, jumpHeader)

/**
 * Read bitmap header from a file.
 *
 * @param file File to read.
 * @return Optional that contains bitmap header if the file is a valid bitmap.
 */
fun obtainBitmapHeader(file : File) : Optional<BitmapHeader>
{
    if (!file.exists() || file.isDirectory || !file.canRead())
    {
        return Optional.empty()
    }

    var bitmapHeader : Optional<BitmapHeader> = Optional.empty()

    treatInputStream({ FileInputStream(file) },
                     { bitmapHeader = Optional.of(readBitmapHeader(it)) })

    return bitmapHeader
}

/**
 * Obtain bitmap size from a file.
 *
 * @param file File to read.
 * @return Optional that contains bitmap dimension if the file is a valid bitmap.
 */
fun computeBitmapDimension(file : File) =
    obtainBitmapHeader(file).ifElse({ Optional.of(Dimension(it.width, it.height)) },
                                    { Optional.empty() })

/**
 * Indicates if a file is a valid bitmap.
 *
 * @param file File to test.
 * @return **`true`** if the file is a valid bitmap.
 */
fun bitmap(file : File) = obtainBitmapHeader(file).isPresent

/**
 * Parse stream to bitmap image.
 *
 * @param inputStream Stream to parse.
 * @return Bitmap read.
 * @throws IOException If the stream not contains a valid bitmap image.
 */
@Throws(IOException::class)
fun parseBitmap(inputStream : InputStream) : BitmapImage
{
    val bitmapHeader = readBitmapHeader(inputStream)
    val rasterImage = bitmapHeader.readRasterImage(inputStream)
    return BitmapImage(bitmapHeader, rasterImage)
}

/**
 * Load bitmap image from a file.
 *
 * @param file Bitmap image file.
 * @return An optional that contains the bitmap image if the file is a valid bitmap image.
 */
fun loadBitmap(file : File) : Optional<BitmapImage>
{
    if (!file.exists() || file.isDirectory || !file.canRead())
    {
        return Optional.empty()
    }

    var bitmapImage : Optional<BitmapImage> = Optional.empty()

    treatInputStream({ FileInputStream(file) },
                     { bitmapImage = Optional.of(parseBitmap(it)) },
                     {})

    return bitmapImage
}
