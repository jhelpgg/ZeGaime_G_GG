package fr.khelp.zegaime.security.rsa

import fr.khelp.zegaime.utils.collections.lists.CycleByteArray
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.util.Objects

/**
 * An output stream that encrypts data using RSA.
 *
 * **Creation example:**
 * ```kotlin
 * val encryptedStream = RSAEncryptOutputStream(publicKey, outputStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * encryptedStream.write(data)
 * encryptedStream.close()
 * ```
 *
 * @constructor Creates a new RSA encrypt output stream.
 * @param publicKey The RSA public key to use for encryption.
 * @param encryptedStream The output stream to write the encrypted data to.
 */
class RSAEncryptOutputStream(publicKey: RSAPublicKey, val encryptedStream: OutputStream) : OutputStream()
{
    private val cipher = publicKey.cipher()
    private val bufferedOutputStream = BufferedOutputStream(this.encryptedStream)
    private val cycleByteArray = CycleByteArray()

    /**
     * Closes this output stream and releases any system resources associated with this stream.
     */
    override fun close()
    {
        this.flush()
        this.bufferedOutputStream.close()
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out.
     */
    override fun flush()
    {
        this.doTransfer(true)
        this.bufferedOutputStream.flush()
    }

    /**
     * Writes the specified byte to this output stream.
     *
     * @param b The byte to be written.
     */
    override fun write(b: Int)
    {
        this.write(byteArrayOf(b.toByte()), 0, 1)
    }

    /**
     * Writes `len` bytes from the specified byte array starting at offset `off` to this output stream.
     *
     * @param b The data.
     * @param off The start offset in the data.
     * @param len The number of bytes to write.
     */
    override fun write(b: ByteArray, off: Int, len: Int)
    {
        Objects.checkFromIndexSize(off, len, b.size)

        if (len == 0)
        {
            return
        }

        this.cycleByteArray.write(b, off, len)
        this.doTransfer(false)
    }

    private fun doTransfer(flush: Boolean)
    {
        while (this.cycleByteArray.notEmpty && (flush || this.cycleByteArray.size >= 245))
        {
            val temp = ByteArray(245)
            val read = this.cycleByteArray.read(temp, 0, 245)
            val crypted = this.cipher.doFinal(cipher.update(temp, 0, read))
            this.bufferedOutputStream.write(crypted.size % 256)
            this.bufferedOutputStream.write(crypted)
        }
    }
}