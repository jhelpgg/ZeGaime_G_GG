package fr.khelp.zegaime.sounds.mp3

import fr.khelp.zegaime.sounds.SoundProgress
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * An input stream that can be controlled.
 *
 * This class is for internal use of the sound system.
 *
 * @property file The file to read.
 * @property pause Indicates if the stream reading is paused.
 * @property size The size of the file.
 * @property progressObservable An observable that emits the reading progress.
 * @property position The current reading position.
 * @constructor Creates a new control input stream.
 */
internal class ControlInputStream(private val file : File) : InputStream()
{
    /** Whether the stream reading is in pause */
    var pause = false

    /** File size */
    val size = file.length()

    /** Maximum value */
    private val maximum = this.size - 1L

    /** Last mark read */
    private var mark = 0L

    /** Access to file in whatever order */
    private var randomAccessFile = RandomAccessFile(this.file, "r")

    /** File chanel */
    private var fileChanel = this.randomAccessFile.channel

    /** Reading progress source */
    private val progressSource = ObservableSource<SoundProgress>(SoundProgress(0L, this.maximum))

    /** Reading progress */
    val progressObservable : Observable<SoundProgress> = this.progressSource.observable

    /** Current reading position */
    var position : Long
        get() = this.fileChanel.position()
        set(value)
        {
            val position = value.coerceIn(0L, this.maximum)
            this.fileChanel.position(position)
            this.progressSource.value = SoundProgress(position, this.maximum)
        }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an `int` in the range `0` to
     * `255`. If no byte is available because the end of the stream
     * has been reached, the value `-1` is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     *
     *  A subclass must provide an implementation of this method.
     *
     * @return     the next byte of data, or `-1` if the end of the
     * stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    override fun read() : Int
    {
        if (this.pause)
        {
            return -1
        }

        val read = this.randomAccessFile.read()
        this.progressSource.value = SoundProgress(this.fileChanel.position(), this.maximum)
        return read
    }

    /**
     * Read several bytes
     *
     * @param b Array to fill
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.read
     */
    override fun read(b : ByteArray) : Int
    {
        if (this.pause)
        {
            return -1
        }

        val read = this.randomAccessFile.read(b)
        this.progressSource.value = SoundProgress(this.fileChanel.position(), this.maximum)
        return read
    }

    /**
     * Read several bytes
     *
     * @param b   Array to fill
     * @param off Where start to fill
     * @param len Number of desired bytes
     * @return Number of bytes read
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.read
     */
    override fun read(b : ByteArray, off : Int, len : Int) : Int
    {
        if (this.pause)
        {
            return -1
        }

        val read = this.randomAccessFile.read(b, off, len)
        this.progressSource.value = SoundProgress(this.fileChanel.position(), this.maximum)
        return read
    }

    /**
     * Skip number of bytes
     *
     * @param n Number of bytes to skip
     * @return Number of bytes really skipped
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.skip
     */
    override fun skip(n : Long) : Long
    {
        val skipped = this.randomAccessFile.skipBytes(n.toInt()).toLong()
        this.progressSource.value = SoundProgress(this.fileChanel.position(), this.maximum)
        return skipped
    }

    /**
     * Returns the number of bytes that can be read
     *
     * @return Data size
     * @see InputStream.available
     */
    override fun available() : Int = (this.size - this.fileChanel.position()).toInt()

    /**
     * Mark the actual position
     *
     * @param readLimit Limit to keep
     * @see InputStream.mark
     */
    @Synchronized
    override fun mark(readLimit : Int)
    {
        this.mark = this.position
    }

    /**
     * Reset to last mark
     *
     * @throws IOException Not throw here (But keep to respect InputStream extends)
     * @see InputStream.reset
     */
    @Synchronized
    override fun reset()
    {
        this.position = this.mark
    }

    /**
     * Indicates that mark are supported
     *
     * @return `true`
     * @see InputStream.markSupported
     */
    override fun markSupported() = true

    /**
     * Reset the reading
     */
    fun resetAtZero()
    {
        this.pause = false
        this.randomAccessFile = RandomAccessFile(this.file, "r")
        this.fileChanel = this.randomAccessFile.channel
        this.position = 0
    }

    /**
     * Close the reading
     */
    override fun close()
    {
        this.fileChanel.close()
        this.randomAccessFile.close()
    }
}