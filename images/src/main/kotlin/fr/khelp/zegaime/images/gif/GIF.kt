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
 * Represents a GIF image.
 *
 * This class can be used to load and play animated GIFs.
 *
 * **Creation example:**
 * ```kotlin
 * val gif = GIF(inputStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * gif.startAnimation()
 * val currentImage = gif.imageFromStartAnimation()
 * ```
 *
 * @property width The width of the GIF.
 * @property height The height of the GIF.
 * @property totalTime The total duration of the GIF animation in milliseconds.
 * @property numberOfImage The number of images in the GIF.
 * @constructor Creates a new GIF from an input stream.
 */
class GIF(inputStream : InputStream)
{
    companion object
    {
        /**
         * Computes the size of a GIF image.
         *
         * If the given file is not a GIF image file, `null` is returned.
         *
         * **Usage example**
         * ```kotlin
         * val size = GIF.computeGifSize(file)
         * ```
         *
         * @param file The GIF image file.
         * @return The size of the GIF image, or `null` if the file is not a valid GIF image.
         */
        fun computeGifSize(file : File) = fr.khelp.zegaime.images.gif.computeGifSize(file)

        /**
         * Indicates if a file is a GIF image file.
         *
         * **Usage example**
         * ```kotlin
         * val isGif = GIF.isGIF(file)
         * ```
         *
         * @param file The file to test.
         * @return `true` if the file is a GIF image file, `false` otherwise.
         */
        fun isGIF(file : File) = fr.khelp.zegaime.images.gif.isGIF(file)
    }

    /**
     * Visitor to collect images.
     *
     * @param delays The array to store the delays of the images.
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
         * Transforms the collected list into an array and empties the list.
         *
         * @return An array of the images.
         */
        fun getArray() : Array<GameImage>
        {
            val array = this.list.toTypedArray()
            this.list.clear()
            return array
        }

        /**
         * Called when a collection of images is finished.
         */
        override fun endCollecting() = Unit

        /**
         * Called when the next image is computed.
         *
         * @param duration The duration of the image in milliseconds.
         * @param image The image.
         */
        override fun nextImage(duration : Long, image : GameImage)
        {
            this.delays.add(duration.toInt())
            this.list.add(image)
        }

        /**
         * Called when the image collection starts.
         *
         * @param width The width of the image.
         * @param height The height of the image.
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
     * Computes the MD5 hash of the GIF.
     *
     * **Usage example**
     * ```kotlin
     * val md5 = gif.md5()
     * ```
     *
     * @return The MD5 hash of the GIF.
     * @throws NoSuchAlgorithmException If the MD5 algorithm is not available.
     * @throws IOException On computation problem.
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
     * Returns the delay of an image.
     *
     * **Usage example**
     * ```kotlin
     * val delay = gif.delay(0)
     * ```
     *
     * @param index The index of the image.
     * @return The delay in milliseconds.
     */
    fun delay(index : Int) = this.delays[index]

    /**
     * Returns an image.
     *
     * **Usage example**
     * ```kotlin
     * val image = gif.image(0)
     * ```
     *
     * @param index The index of the image.
     * @return The desired image.
     */
    fun image(index : Int) = this.images[index]

    /**
     * Returns the image index suggested between the last time [GIF.startAnimation] was called and the time this method is called,
     * based on the images delays.
     *
     * **Usage example**
     * ```kotlin
     * val index = gif.imageIndexFromStartAnimation()
     * ```
     *
     * @return The image index since the last time [GIF.startAnimation] was called.
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
     * Returns the image suggested between the last time [GIF.startAnimation] was called and the time this method is called,
     * based on the images delays.
     *
     * **Usage example**
     * ```kotlin
     * val image = gif.imageFromStartAnimation()
     * ```
     *
     * @return The image since the last time [GIF.startAnimation] was called.
     */
    fun imageFromStartAnimation() = this.images[this.imageIndexFromStartAnimation()]

    /**
     * The number of images.
     */
    val numberOfImage : Int
        get() = this.images.size

    /**
     * Starts or restarts the animation from the beginning.
     *
     * To follow the evolution, use [imageFromStartAnimation] to get the current image of the animation.
     *
     * **Usage example**
     * ```kotlin
     * gif.startAnimation()
     * ```
     */
    fun startAnimation()
    {
        this.startTime = System.currentTimeMillis()
    }
}