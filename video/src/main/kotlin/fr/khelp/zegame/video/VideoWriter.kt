package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.ImageFormat
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.io.writeByteArray
import fr.khelp.zegaime.utils.io.writeInt
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * A writer for creating a video.
 *
 * **Creation example:**
 * ```kotlin
 * val videoWriter = VideoWriter(640, 480, 25, outputStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * videoWriter.appendImage(image1)
 * videoWriter.appendImage(image2)
 * videoWriter.close()
 * ```
 *
 * @property width The width of the video.
 * @property height The height of the video.
 * @property fps The frames per second of the video.
 * @property outputStream The output stream to write the video to.
 */
class VideoWriter(val width : Int, val height : Int, val fps : Int, val outputStream : OutputStream)
{
    /**
     * Creates a new video writer with a default FPS of 25.
     *
     * @param width The width of the video.
     * @param height The height of the video.
     * @param outputStream The output stream to write the video to.
     */
    constructor(width : Int, height : Int, outputStream : OutputStream) : this(width, height, 25, outputStream)

    init
    {
        argumentCheck(this.width > 0 && this.height >= 0) { "Width and height must be > 0, not ${this.width}c${this.height}" }
        argumentCheck(this.fps in 1..100) { "FPS must be in [1, 100], not ${this.fps}" }
        this.writeHeader()
    }

    /**
     * Appends an image to the video.
     *
     * @param image The image to append.
     */
    fun appendImage(image : GameImage)
    {
        val imageResized = image.resize(this.width, this.height)
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageResized.save(byteArrayOutputStream, ImageFormat.JPEG)
        this.outputStream.writeByteArray(byteArrayOutputStream.toByteArray())
    }

    /**
     * Closes the video writer.
     */
    fun close()
    {
        this.outputStream.writeByteArray(ByteArray(0))

        try
        {
            this.outputStream.flush()
        }
        catch (_ : Exception)
        {
        }

        try
        {
            this.outputStream.close()
        }
        catch (_ : Exception)
        {
        }
    }

    private fun writeHeader()
    {
        this.outputStream.writeInt(this.width)
        this.outputStream.writeInt(this.height)
        this.outputStream.writeInt(this.fps)
    }
}
