package fr.khelp.zegaime.images.pcx

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.utils.extensions.and
import fr.khelp.zegaime.utils.extensions.toUnsignedInt
import fr.khelp.zegaime.utils.io.readFully
import java.io.EOFException
import java.io.IOException
import java.io.InputStream

/**
 * Represents a PCX image.
 *
 * It loads a PCX image file in memory and stores its information and uncompressed image data.
 *
 * An [GameImage] can be created from this loader with the [createImage] method.
 *
 * The loader here is a combination of:
 * * [PCX header](http://www.fileformat.info/format/pcx/corion.htm)
 * * [PCX image data information](http://en.wikipedia.org/wiki/PCX)
 * * tests
 *
 * **Creation example:**
 * ```kotlin
 * val pcx = PCX(inputStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val image = pcx.createImage()
 * ```
 *
 * @property height The height of the image.
 * @property manufacturer The manufacturer code.
 * @property manufacturerName The name of the manufacturer.
 * @property version The PCX version code.
 * @property versionName The name of the version.
 * @property width The width of the image.
 */
class PCX internal constructor()
{
    companion object
    {
        /**
         * Reads one word from an array.
         *
         * @param array The array to read from.
         * @param offset The offset where to start reading the word.
         * @return The word read.
         */
        private fun word(array : ByteArray, offset : Int) : Int
        {
            return array[offset].toUnsignedInt() or (array[offset + 1].toUnsignedInt() shl 8)
        }
    }

    /**
     * Indicates if a 256 palette is defined
     */
    private var has256Palette : Boolean = false

    /**
     * Image height
     */
    var height : Int = 0
        private set

    /**
     * DPI in horizontal
     */
    private var horizontalDPI : Int = 0

    /**
     * Manufacturer code
     */
    var manufacturer : Byte = 0
        private set

    /**
     * The name of the manufacturer.
     */
    val manufacturerName : String get() = manufacturerToString(this.manufacturer)

    /**
     * Number of byte per scanline
     */
    private var numberBitsPerScanline : Int = 0

    /**
     * Number of byte per pixel
     */
    private var numberBytePerPixel : Int = 0

    /**
     * Number of color plane
     */
    private var numberOfColorPlane : Int = 0

    /**
     * The 16 colors palette
     */
    private lateinit var palette16 : IntArray

    /**
     * The 256 colors palette
     */
    private lateinit var palette256 : IntArray

    /**
     * Scanline size
     */
    private var scanLineSize : Int = 0

    /**
     * Screen height
     */
    private var screenHeight : Int = 0

    /**
     * Screen width
     */
    private var screenWidth : Int = 0

    /**
     * Uncompressed image data
     */
    private lateinit var uncompressed : IntArray

    /**
     * PCX version code
     */
    var version : Byte = 0
        private set

    /**
     * The name of the version.
     */
    val versionName : String get() = versionToString(this.version)

    /**
     * Vertical DPI
     */
    private var verticalDPI : Int = 0

    /**
     * Image width
     */
    var width : Int = 0
        private set

    /**
     * Creates a new PCX image from an input stream.
     *
     * @param inputStream The input stream to read from.
     */
    constructor(inputStream : InputStream) : this()
    {
        this.readHeader(inputStream)
        this.readImageData(inputStream)
        this.read256Palette(inputStream)
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 1 color plane
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_1_BytePerPixel_1_ColorPlane(pixels : IntArray)
    {
        // Each bit represents a pixel, 1 => white, 0 => black
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        var shift : Int
        var read : Int
        var index : Int
        val white = WHITE.argb
        val black = BLACK.argb

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)
            x = 0
            shift = 7
            index = 0
            read = scanLine[0]

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    pixels[pix++] = white
                }
                else
                {
                    pixels[pix++] = black
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++

                    if (index >= this.scanLineSize)
                    {
                        break
                    }

                    read = scanLine[index]
                }

                x++
            }

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 3 color planes
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_1_BytePerPixel_3_ColorPlane(pixels : IntArray)
    {
        // The palette 16 index is dispatch like that, lower bits first, upper bits last (only first 8 colors (0-7) of
        // the palette are used)
        val scanLine = IntArray(this.scanLineSize)
        val codes = IntArray(this.width)
        var lineData = 0
        var pix = 0
        var x : Int
        var shift : Int
        var read : Int
        var index : Int

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            x = 0
            shift = 7
            index = 0
            read = scanLine[0]

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = 0x1
                }
                else
                {
                    codes[x] = 0
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++
                    read = scanLine[index]
                }

                x++
            }

            x = 0

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = codes[x] or 0x2
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++
                    read = scanLine[index]
                }

                x++
            }

            x = 0

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = codes[x] or 0x4
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++

                    if (index >= this.scanLineSize)
                    {
                        break
                    }

                    read = scanLine[index]
                }

                x++
            }

            x = 0
            while (x < this.width)
            {
                pixels[pix++] = this.palette16[codes[x]]
                x++
            }

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 1 byte per pixel and 4 color planes
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_1_BytePerPixel_4_ColorPlane(pixels : IntArray)
    {
        // The palette 16 index is dispatch like that, lower bits first, upper bits last
        val scanLine = IntArray(this.scanLineSize)
        val codes = IntArray(this.width)
        var lineData = 0
        var pix = 0
        var x : Int
        var shift : Int
        var read : Int
        var index : Int

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            x = 0
            shift = 7
            index = 0
            read = scanLine[0]

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = 0x1
                }
                else
                {
                    codes[x] = 0
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++
                    read = scanLine[index]
                }

                x++
            }

            x = 0

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = codes[x] or 0x2
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++
                    read = scanLine[index]
                }

                x++
            }

            x = 0

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = codes[x] or 0x4
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++
                    read = scanLine[index]
                }

                x++
            }

            x = 0

            while (x < this.width)
            {
                if (read shr shift and 1 == 1)
                {
                    codes[x] = codes[x] or 0x8
                }

                shift--

                if (shift < 0)
                {
                    shift = 7
                    index++

                    if (index >= this.scanLineSize)
                    {
                        break
                    }

                    read = scanLine[index]
                }

                x++
            }

            x = 0
            while (x < this.width)
            {
                pixels[pix++] = this.palette16[codes[x]]
                x++
            }

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 4 bytes per pixel and 1 color plane
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_4_BytePerPixel_1_ColorPlane(pixels : IntArray)
    {
        // Each byte contains 2 palette 16 indexes
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        var read : Int
        var index : Int

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            x = 0
            index = 0

            while (x < this.width)
            {
                read = scanLine[index]

                pixels[pix++] = this.palette16[(read shr 4) and 0xF]
                x++

                if (x < this.width)
                {
                    pixels[pix++] = this.palette16[read and 0xF]
                    x++
                }

                index++
            }

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 4 bytes per pixel and 4 color planes
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_4_BytePerPixel_4_ColorPlane(pixels : IntArray)
    {
        // RGBA all codes in 0-16, so have to multiply values per 16
        // 2 parts per byte
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        var read : Int
        var index : Int
        var start : Int
        var write : Int

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            start = 0
            index = 0
            x = 0
            write = pix

            while (x < this.width)
            {
                read = scanLine[index]

                pixels[write++] = ((read shr 4) and 0xF) shl 20
                x++

                if (x < this.width)
                {
                    pixels[write++] = (read and 0xF) shl 20
                    x++
                }
            }

            start += this.numberBitsPerScanline
            index = start
            x = 0
            write = pix

            while (x < this.width)
            {
                read = scanLine[index]

                pixels[write] = pixels[write] or (((read shr 4) and 0xF) shl 12)
                write++
                x++

                if (x < this.width)
                {
                    pixels[write] = pixels[write] or ((read and 0xF) shl 12)
                    write++
                    x++
                }
            }

            start += this.numberBitsPerScanline
            index = start
            x = 0
            write = pix

            while (x < this.width)
            {
                read = scanLine[index]

                pixels[write] = pixels[write] or (((read shr 4) and 0xF) shl 4)
                write++
                x++

                if (x < this.width)
                {
                    pixels[write] = pixels[write] or ((read and 0xF) shl 4)
                    write++
                    x++
                }
            }

            start += this.numberBitsPerScanline
            index = start
            x = 0
            write = pix

            while (x < this.width)
            {
                read = scanLine[index]

                pixels[write] = pixels[write] or (((read shr 4) and 0xF) shl 28)
                write++
                x++

                if (x < this.width)
                {
                    pixels[write] = pixels[write] or ((read and 0xF) shl 28)
                    write++
                    x++
                }
            }

            pix += this.width

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 1 color plane
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_8_BytePerPixel_1_ColorPlane(pixels : IntArray)
    {
        // Each byte is 1 palette 256 index
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            x = 0
            while (x < this.width)
            {
                pixels[pix++] = this.palette256[scanLine[x]]
                x++
            }

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 3 color planes
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_8_BytePerPixel_3_ColorPlane(pixels : IntArray)
    {
        // RGB color : ex for (5x3)
        // RRRRRGGGGGBBBBB
        // RRRRRGGGGGBBBBB
        // RRRRRGGGGGBBBBB
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        var index : Int
        var start : Int
        var write : Int
        val black = BLACK.argb

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            start = 0
            index = 0
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write++] = scanLine[index++] shl 16
                x++
            }

            start += this.numberBitsPerScanline
            index = start
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or (scanLine[index] shl 8)
                write++
                index++
                x++
            }

            start += this.numberBitsPerScanline
            index = start
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or scanLine[index]
                write++
                index++
                x++
            }

            start += this.numberBitsPerScanline
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or black
                write++
                x++
            }

            pix += this.width

            lineData += this.scanLineSize
        }
    }

    /**
     * Fill image pixels for the case : 8 bytes per pixel and 4 color planes
     *
     * @param pixels Pixels image to fill
     */
    private fun fillPixels_8_BytePerPixel_4_ColorPlane(pixels : IntArray)
    {
        // RGBA color : ex for (5x3)
        // RRRRRGGGGGBBBBBAAAAA
        // RRRRRGGGGGBBBBBAAAAA
        // RRRRRGGGGGBBBBBAAAAA
        val scanLine = IntArray(this.scanLineSize)
        var lineData = 0
        var pix = 0
        var x : Int
        var index : Int
        var start : Int
        var write : Int

        for (y in 0 until this.height)
        {
            System.arraycopy(this.uncompressed, lineData, scanLine, 0, this.scanLineSize)

            start = 0
            index = 0
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write++] = scanLine[index++] shl 16
                x++
            }

            start += this.numberBitsPerScanline
            index = start
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or (scanLine[index++] shl 8)
                write++
                x++
            }

            start += this.numberBitsPerScanline
            index = start
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or scanLine[index++]
                write++
                x++
            }

            start += this.numberBitsPerScanline
            index = start
            write = pix

            x = 0
            while (x < this.width)
            {
                pixels[write] = pixels[write] or (scanLine[index++] shl 24)
                write++
                x++
            }

            pix += this.width

            lineData += this.scanLineSize
        }
    }

    /**
     * Reads the 256 color palette.
     *
     * @param inputStream The stream to read from.
     * @throws IOException On reading issue.
     */
    @Throws(IOException::class)
    private fun read256Palette(inputStream : InputStream)
    {
        var read = inputStream.read()
        this.has256Palette = read == 0x0C
        val black = BLACK.argb

        if (this.has256Palette)
        {
            val data = ByteArray(768)
            read = inputStream.readFully(data)

            if (read < 768)
            {
                throw IOException("Not enough data for the 256 palette")
            }

            var index : Int
            this.palette256 = IntArray(256)

            index = 0
            read = 0
            while (index < 256)
            {
                this.palette256[index] = black or
                        (data[read].toUnsignedInt() shl 16) or
                        (data[read + 1].toUnsignedInt() shl 8) or
                        data[read + 2].toUnsignedInt()
                index++
                read += 3
            }
        }
        else if (this.numberBytePerPixel == 8 && this.numberOfColorPlane == 1)
        {
            // In that case we have to use 256 gray shade, to treat it has normal 256 palette, we generate a gray shade one
            this.has256Palette = true
            this.palette256 = IntArray(256)

            for (index in 0..255)
            {
                this.palette256[index] = black or (index shl 16) or (index shl 8) or index
            }
        }
    }

    /**
     * Reads the PCX header.
     *
     * @param inputStream The stream to read from.
     * @throws IOException On reading issue.
     */
    @Throws(IOException::class)
    internal fun readHeader(inputStream : InputStream)
    {
        // Header has 128 bytes fixed size
        val header = ByteArray(128)
        var read = inputStream.readFully(header)

        if (read < 128)
        {
            throw IOException("Not enough data for read the header")
        }

        this.manufacturer = header[0x00]
        this.version = header[0x01]
        val encoding = header[0x02]

        if (encoding != 1.toByte())
        {
            throw IOException("Unknown encoding=$encoding, only supported is 1 (RLE)")
        }

        this.numberBytePerPixel = header[0x03] and 0xFF
        val left = PCX.word(header, 0x04)
        val up = PCX.word(header, 0x06)
        val right = PCX.word(header, 0x08)
        val bottom = PCX.word(header, 0x0A)

        if (left > right || up > bottom)
        {
            throw IOException("Invalid PCX size !")
        }

        this.horizontalDPI = PCX.word(header, 0x0C)
        this.verticalDPI = PCX.word(header, 0x0E)

        var index : Int
        this.palette16 = IntArray(16)
        val black = BLACK.argb

        read = 0x10
        index = 0
        while (index < 16)
        {
            this.palette16[index] = black or
                    (header[read].toUnsignedInt() shl 16) or
                    (header[read + 1].toUnsignedInt() shl 8) or
                    header[read + 2].toUnsignedInt()
            read += 3
            index++
        }

        this.numberOfColorPlane = header[0x41].toUnsignedInt()
        this.numberBitsPerScanline = PCX.word(header, 0x42)
        // UNUSED : int paletteInformation = PCX.word(header, 0x44);
        this.screenWidth = PCX.word(header, 0x46)
        this.screenHeight = PCX.word(header, 0x48)

        if (this.numberOfColorPlane < 1 || this.numberOfColorPlane > 4 || this.numberBytePerPixel < 1 ||
            this.numberBytePerPixel > 8)
        {
            throw IOException("Invalid PCX header !")
        }

        this.width = right - left + 1
        this.height = bottom - up + 1
        this.scanLineSize = this.numberOfColorPlane * this.numberBitsPerScanline
    }

    /**
     * Reads the image data and uncompresses them.
     *
     * @param inputStream The stream to read from.
     * @throws IOException On reading issue.
     */
    @Throws(IOException::class)
    private fun readImageData(inputStream : InputStream)
    {
        val total = this.height * this.scanLineSize
        this.uncompressed = IntArray(total)
        var index = 0
        var read : Int
        var count : Int
        var i : Int

        while (index < total)
        {
            read = inputStream.read()

            if (read < 0)
            {
                throw EOFException("Unexpected end of stream !")
            }

            if (read < 0xC0)
            {
                // If 2 first bits aren't 1 together, then it is an isolated value
                this.uncompressed[index++] = read
            }
            else
            {
                // If 2 first bits are 1, then the byte represents the number of time to repeat the following byte
                // Have to remove 2 first bits to have the repetition number
                count = read and 0x3F
                read = inputStream.read()

                if (read < 0)
                {
                    throw EOFException("Unexpected end of stream !")
                }

                i = 0
                while (i < count)
                {
                    this.uncompressed[index++] = read
                    i++
                }
            }
        }
    }

    /**
     * Creates a new image from the PCX information.
     *
     * **Usage example**
     * ```kotlin
     * val image = pcx.createImage()
     * ```
     *
     * @return The created image.
     * @throws IllegalStateException If how to create the image for this specific PCX information is unknown.
     */
    fun createImage() : GameImage
    {
        val pixels = IntArray(this.width * this.height)

        when (this.numberBytePerPixel)
        {
            1    -> when (this.numberOfColorPlane)
            {
                1    -> this.fillPixels_1_BytePerPixel_1_ColorPlane(pixels)
                3    -> this.fillPixels_1_BytePerPixel_3_ColorPlane(pixels)
                4    -> this.fillPixels_1_BytePerPixel_4_ColorPlane(pixels)
                else -> throw IllegalStateException(
                    "Don't know how to convert numberBytePerPixel=${this.numberBytePerPixel} numberOfColorPlane=${this.numberOfColorPlane}")
            }

            4    -> when (this.numberOfColorPlane)
            {
                1    -> this.fillPixels_4_BytePerPixel_1_ColorPlane(pixels)
                4    -> this.fillPixels_4_BytePerPixel_4_ColorPlane(pixels)
                else -> throw IllegalStateException(
                    "Don't know how to convert numberBytePerPixel=${this.numberBytePerPixel} numberOfColorPlane=${this.numberOfColorPlane}")
            }

            8    -> when (this.numberOfColorPlane)
            {
                1    -> this.fillPixels_8_BytePerPixel_1_ColorPlane(pixels)
                3    -> this.fillPixels_8_BytePerPixel_3_ColorPlane(pixels)
                4    -> this.fillPixels_8_BytePerPixel_4_ColorPlane(pixels)
                else -> throw IllegalStateException(
                    "Don't know how to convert numberBytePerPixel=${this.numberBytePerPixel} numberOfColorPlane=${this.numberOfColorPlane}")
            }

            else -> throw IllegalStateException(
                "Don't know how to convert numberBytePerPixel=${this.numberBytePerPixel} numberOfColorPlane=${this.numberOfColorPlane}")
        }

        val gameImage = GameImage(this.width, this.height)
        gameImage.putPixels(0, 0, this.width, this.height, pixels)
        return gameImage
    }

    /**
     * Returns a string representation of the PCX image.
     *
     * @return A string representation of the PCX image.
     */
    override fun toString() : String =
        "PCX ${this.width}x${this.height} by ${manufacturerToString(this.manufacturer)} version ${versionToString(this.version)}"
}