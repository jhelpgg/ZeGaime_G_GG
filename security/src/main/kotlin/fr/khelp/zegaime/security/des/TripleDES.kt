package fr.khelp.zegaime.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * A Triple DES crypter.
 *
 * **Creation example:**
 * ```kotlin
 * val crypter = TripleDES("myLogin", "myPassword")
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * crypter.encrypt(inputStream, outputStream)
 * crypter.decrypt(inputStream, outputStream)
 * ```
 */
class TripleDES(login: String, password: String)
{
    private val first = CrypterDES(login)
    private val second = CrypterDES("${login}_${password}")
    private val third = CrypterDES(password)

    /**
     * Encrypts a stream.
     *
     * @param clearStream The stream to encrypt.
     * @param encryptedStream The stream to write the encrypted data to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun encrypt(clearStream: InputStream, encryptedStream: OutputStream)
    {
        var byteArrayOutputStream = ByteArrayOutputStream()
        this.first.encrypt(clearStream, byteArrayOutputStream)
        var byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream = ByteArrayOutputStream()
        this.second.encrypt(byteArrayInputStream, byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        this.third.encrypt(byteArrayInputStream, encryptedStream)
    }

    /**
     * Decrypts a stream.
     *
     * @param encryptedStream The stream to decrypt.
     * @param clearStream The stream to write the decrypted data to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun decrypt(encryptedStream: InputStream, clearStream: OutputStream)
    {
        var byteArrayOutputStream = ByteArrayOutputStream()
        this.third.decrypt(encryptedStream, byteArrayOutputStream)
        var byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        byteArrayOutputStream = ByteArrayOutputStream()
        this.second.decrypt(byteArrayInputStream, byteArrayOutputStream)
        byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        this.first.decrypt(byteArrayInputStream, clearStream)
    }

    /**
     * Checks if a login and password are valid.
     *
     * @param login The login to check.
     * @param password The password to check.
     * @return `true` if the login and password are valid, `false` otherwise.
     */
    fun valid(login: String, password: String) =
        this.first.passwordValid(login) && this.third.passwordValid(password)

    /**
     * Checks if this crypter has the same keys as another crypter.
     *
     * @param other The other crypter.
     * @return `true` if the keys are the same, `false` otherwise.
     * 
     */
    internal fun sameKeys(other: TripleDES) =
        this.first.sameKey(other.first) && this.second.sameKey(other.second) && this.third.sameKey(other.third)
}
