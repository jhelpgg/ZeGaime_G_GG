package fr.khelp.zegaime.security.rsa

import fr.khelp.zegaime.utils.collections.lists.CycleByteArray
import fr.khelp.zegaime.utils.io.readFully
import fr.khelp.zegaime.utils.io.readSomeBytes
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.Objects

/**
 * An input stream that decrypts data using RSA.
 *
 * **Creation example:**
 * ```kotlin
 * val decryptedStream = RSADecryptInputStream(keyPair, encryptedStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val decryptedData = decryptedStream.readAllBytes()
 * ```
 *
 * @constructor Creates a new RSA decrypt input stream.
 * @param keyPair The RSA key pair to use for decryption.
 * @param encryptedStream The input stream to decrypt.
 */
class RSADecryptInputStream(keyPair: RSAKeyPair, encryptedStream: InputStream) : InputStream()
{
    private val cipher = keyPair.cypher()
    private val bufferedInputStream = BufferedInputStream(encryptedStream)
    private val cycleByteArray = CycleByteArray()
    private var finished = false

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     */
    override fun close()
    {
        this.bufferedInputStream.close()
    }

    /**
     * Reads the next byte of data from the input stream.
     *
     * @return The next byte of data, or -1 if the end of the stream is reached.
     */
    override fun read(): Int
    {
        this.assureHave(1)

        if (this.cycleByteArray.empty)
        {
            return -1
        }

        return this.cycleByteArray.read()
            .toInt() and 0xFF
    }

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array `b`.
     *
     * @param b The buffer into which the data is read.
     * @param off The start offset in array `b` at which the data is written.
     * @param len The maximum number of bytes to read.
     * @return The total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached.
     */
    override fun read(b: ByteArray, off: Int, len: Int): Int
    {
        Objects.checkFromIndexSize(off, len, b.size)

        if (len == 0)
        {
            return 0
        }

        this.assureHave(len)

        if (this.cycleByteArray.empty)
        {
            return -1
        }

        return this.cycleByteArray.read(b, off, len)
    }

    /**
     * Reads up to a specified number of bytes from the input stream.
     *
     * @param len The number of bytes to read.
     * @return The bytes read.
     */
    override fun readNBytes(len: Int): ByteArray =
        this.readSomeBytes(len)

    /**
     * Reads the specified number of bytes from the input stream.
     *
     * @param b The buffer into which the data is read.
     * @param off The start offset in array `b` at which the data is written.
     * @param len The number of bytes to read.
     * @return The number of bytes read.
     */
    override fun readNBytes(b: ByteArray, off: Int, len: Int): Int =
        this.readFully(b, off, len)

    private fun assureHave(number: Int)
    {
        if (this.finished || this.cycleByteArray.size >= number)
        {
            return
        }

        while (this.cycleByteArray.size < number)
        {
            var size = this.bufferedInputStream.read()

            if (size < 0)
            {
                this.finished = true
                return
            }

            if (size == 0)
            {
                size = 256
            }

            val temp = this.bufferedInputStream.readSomeBytes(size)

            if (temp.isEmpty())
            {
                this.finished = true
                return
            }

            this.cycleByteArray.write(this.cipher.doFinal(temp))
        }
    }
}