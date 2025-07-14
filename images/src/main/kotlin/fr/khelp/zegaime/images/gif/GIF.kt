package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.collections.lists.ArrayInt
import fr.khelp.zegaime.utils.io.IntegerArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * GIF image
 * @param inputStream Stream to read image
 */
class GIF(inputStream : InputStream)
{
    companion object
    {
        /**
         * Compute size of an GIF image.
         *
         * If the given file is not a GIF image file, `null` is return
         *
         * @param file Image GIF file
         * @return GIF image size OR `null` if given file not a valid GIF image file
         */
        fun computeGifSize(file : File) = fr.khelp.zegaime.images.gif.computeGifSize(file)

        /**
         * Indicates if a file is a GIF image file
         *
         * @param file Tested file
         * @return `true` if the file is a GIF image file
         */
        fun isGIF(file : File) = fr.khelp.zegaime.images.gif.isGIF(file)
    }

    /**
     * Visitor to collect images
     * @param delays Images delays to fill
     */
    private class InternalVisitor(val delays : ArrayInt) : DataGIFVisitor
    {
        /**Images collected*/
        private val list = ArrayList<GameImage>()

        /**Image width*/
        var width = 0

        /**Image height*/
        var height = 0

        /**
         * Transform collected list in array, and empty the list
         *
         * @return Array of images
         */
        fun getArray() : Array<GameImage>
        {
            val array = this.list.toTypedArray()
            this.list.clear()
            return array
        }

        /**
         * Called when a collection of images is finished *
         */
        override fun endCollecting() = Unit

        /**
         * Called when the next image is computed
         * @param duration Image duration in millisecond
         * @param image    Image
         */
        override fun nextImage(duration : Long, image : GameImage)
        {
            this.delays.add(duration.toInt())
            this.list.add(image)
        }

        /**
         * Called when collect image start
         * @param width  Image width
         * @param height Image height
         */
        override fun startCollecting(width : Int, height : Int)
        {
            this.width = width
            this.height = height
        }
    }

    /**Images delays*/
    private val delays = ArrayInt()

    /**Image width*/
    val width : Int

    /**Image height*/
    val height : Int

    /**Images of animated GIF*/
    private val images : Array<GameImage>

    /**Previous image index*/
    private var previousIndex = 0

    /**Start animation time*/
    private var startTime = 0L

    /**Total GIF animation time*/
    val totalTime : Int

    /** Number of images/delays */
    val number : Int get() = this.images.size

    init
    {
        var totalTime = 0
        val dataGIF = DataGIF()
        dataGIF.read(inputStream)
        val internalVisitor = InternalVisitor(this.delays)
        dataGIF.collectImages(internalVisitor)
        this.width = internalVisitor.width
        this.height = internalVisitor.height
        this.images = internalVisitor.getArray()
        val size = this.delays.size

        for (i in 0 until size)
        {
            totalTime += this.delays[i]
        }

        this.totalTime = totalTime

        if (this.images.isEmpty())
        {
            throw IOException("Failed to load GIF, no extracted image")
        }
    }

    /**
     * Compute GIF MD5
     *
     * @return GIF MD5
     * @throws NoSuchAlgorithmException If MD5 unknown
     * @throws IOException              On computing problem
     */
    @Throws(NoSuchAlgorithmException::class, IOException::class)
    fun md5() : String
    {
        val md5 = MessageDigest.getInstance("MD5")
        val numberOfImages = this.images.size
        var temp = ByteArray(4096)
        temp[0] = (numberOfImages shr 24 and 0xFF).toByte()
        temp[1] = (numberOfImages shr 16 and 0xFF).toByte()
        temp[2] = (numberOfImages shr 8 and 0xFF).toByte()
        temp[3] = (numberOfImages and 0xFF).toByte()

        md5.update(temp, 0, 4)

        var inputStream : IntegerArrayInputStream
        var bufferedImage : GameImage
        var pixels : IntArray
        var read : Int
        var width : Int
        var height : Int

        for (image1 in this.images)
        {
            bufferedImage = image1

            width = bufferedImage.width
            height = bufferedImage.height

            pixels = bufferedImage.grabPixels(0, 0, width, height, 2)
            pixels[0] = width
            pixels[1] = height

            inputStream = IntegerArrayInputStream(pixels)
            read = inputStream.read(temp)

            while (read >= 0)
            {
                md5.update(temp, 0, read)

                read = inputStream.read(temp)
            }

            inputStream.close()
        }

        temp = md5.digest()
        val stringBuffer = StringBuilder()

        for (b in temp)
        {
            read = b.toInt() and 0xFF
            stringBuffer.append(Integer.toHexString(read shr 4 and 0xF))
            stringBuffer.append(Integer.toHexString(read and 0xF))
        }

        return stringBuffer.toString()
    }

    /**
     * Obtain an image delay
     *
     * @param index Image index
     * @return Delay in millisecond
     */
    fun delay(index : Int) = this.delays[index]

    /**
     * Get a image
     *
     * @param index Image index
     * @return Desired image
     */
    fun image(index : Int) = this.images[index]

    /**
     * Get the image index suggest between last time [GIF.startAnimation] was called and time this method is called based on
     * images delays
     *
     * @return Image index since last time [GIF.startAnimation] was called
     */
    fun imageIndexFromStartAnimation() : Int
    {
        val time = System.currentTimeMillis() - this.startTime
        val max = this.images.size - 1
        val relativeTime = (time % this.totalTime).toInt()
        var index = 0
        var actualTime = 0
        var delay : Int

        while (index < max)
        {
            delay = this.delays[index]
            actualTime += delay

            if (actualTime >= relativeTime)
            {
                break
            }

            index++
        }

        val nextIndex = (this.previousIndex + 1) % this.images.size

        if (index != this.previousIndex)
        {
            index = nextIndex
        }

        this.previousIndex = index
        return index
    }

    /**
     * Get the image suggest between last time [GIF.startAnimation] was called and time this method is called based on
     * images delays
     *
     * @return Image since last time [GIF.startAnimation] was called
     */
    fun imageFromStartAnimation() = this.images[this.imageIndexFromStartAnimation()]

    /**
     * Number of images
     *
     * @return Number of images
     */
    val numberOfImage : Int
        get() = this.images.size

    /**
     * Start/restart animation from beginning, to follow evolution, use [imageFromStartAnimation] to have current
     * image of the animation
     */
    fun startAnimation()
    {
        this.startTime = System.currentTimeMillis()
    }
}
