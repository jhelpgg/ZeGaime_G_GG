package fr.khelp.zegaime.security.des

import fr.khelp.zegaime.utils.extensions.same
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * A DES crypter.
 *
 * **Creation example:**
 * ```kotlin
 * val crypter = CrypterDES("myPassword")
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * crypter.encrypt(inputStream, outputStream)
 * crypter.decrypt(inputStream, outputStream)
 * ```
 */
class CrypterDES(password : String)
{
    companion object
    {
        private fun computeKey(string : String) : ByteArray
        {
            var hash : Long = 0
            string.toCharArray()
                .forEach { hash = 31L * hash + it.code.toLong() }
            val key = ByteArray(8)

            (0..7).forEach { index ->
                key[index] = (hash and 0xFF).toByte()
                hash = hash shr 8
            }

            return key
        }
    }

    private var lastPasswordCheckTime = 0L
    private var timeBetweenAttempt = 128L
    private val key = CrypterDES.computeKey(password)

    /**
     * Encrypts a stream.
     *
     * @param clearStream The stream to encrypt.
     * @param encryptedStream The stream to write the encrypted data to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun encrypt(clearStream : InputStream, encryptedStream : OutputStream)
    {
        this.desOperation(Cipher.ENCRYPT_MODE, clearStream, encryptedStream)
    }

    /**
     * Decrypts a stream.
     *
     * @param encryptedStream The stream to decrypt.
     * @param clearStream The stream to write the decrypted data to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun decrypt(encryptedStream : InputStream, clearStream : OutputStream)
    {
        this.desOperation(Cipher.DECRYPT_MODE, encryptedStream, clearStream)
    }

    /**
     * Checks if a password is valid.
     *
     * @param password The password to check.
     * @return `true` if the password is valid, `false` otherwise.
     */
    @Synchronized
    fun passwordValid(password : String) : Boolean
    {
        // To avoid multiple attempt in short time, wait more and more time between each attempt
        val waitBeforeTest = this.timeBetweenAttempt - (System.currentTimeMillis() - this.lastPasswordCheckTime)

        if (waitBeforeTest > 0L)
        {
            Thread.sleep(waitBeforeTest)
        }

        this.lastPasswordCheckTime = System.currentTimeMillis()
        this.timeBetweenAttempt *= 2L

        val key = CrypterDES.computeKey(password)
        val valid = this.key.same(key)
        // To avoid "time attack"
        Thread.sleep((1L..128L).random())
        return valid
    }

    /**
     * Checks if this crypter has the same key as another crypter.
     *
     * @param other The other crypter.
     * @return `true` if the keys are the same, `false` otherwise.
     * 
     */
    internal fun sameKey(other : CrypterDES) =
        this.key.same(other.key)

    /**
     * Encrypt or decrypt data
     * @param mode Encryption or decryption mode
     * @param inputStream Stream to encrypt/decrypt
     * @param outputStream Stream where write the result
     */
    @Throws(IOException::class)
    private fun desOperation(mode : Int, inputStream : InputStream, outputStream : OutputStream)
    {
        val desKeySpec = DESKeySpec(this.key)
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        val cipher = Cipher.getInstance("DES")
        cipher.init(mode, secretKey)
        val cipherInputStream = CipherInputStream(inputStream, cipher)
        val buffer = ByteArray(8)
        var read = cipherInputStream.read(buffer)

        while (read >= 0)
        {
            outputStream.write(buffer, 0, read)
            read = cipherInputStream.read(buffer)
        }

        outputStream.flush()
        outputStream.close()
        cipherInputStream.close()
    }
}
