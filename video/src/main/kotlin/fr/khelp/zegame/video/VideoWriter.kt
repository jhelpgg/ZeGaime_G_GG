package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.ImageFormat
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.io.writeByteArray
import fr.khelp.zegaime.utils.io.writeInt
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class VideoWriter(val width : Int, val height : Int, val fps : Int, val outputStream : OutputStream)
{
    constructor(width : Int, height : Int, outputStream : OutputStream) : this(width, height, 25, outputStream)

    init
    {
        argumentCheck(this.width > 0 && this.height >= 0) { "Width and height must be > 0, not ${this.width}c${this.height}" }
        argumentCheck(this.fps in 1..100) { "FPS must be in [1, 100], not ${this.fps}" }
        this.writeHeader()
    }

    fun appendImage(image : GameImage)
    {
        val imageResized = image.resize(this.width, this.height)
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageResized.save(byteArrayOutputStream, ImageFormat.JPEG)
        this.outputStream.writeByteArray(byteArrayOutputStream.toByteArray())
    }

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