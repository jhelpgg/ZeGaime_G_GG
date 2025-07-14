package fr.khelp.zegaime.security.rsa

import fr.khelp.zegaime.utils.io.readBigInteger
import fr.khelp.zegaime.utils.io.readFully
import fr.khelp.zegaime.utils.io.writeBigInteger
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher

/**
 * Represents an RSA public key, which can be shared with users.
 *
 * It is used to encrypt data for the owner of the corresponding [RSAKeyPair].
 *
 * **Creation example:**
 * ```kotlin
 * val publicKey = RSAPublicKey(inputStream)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val encryptedStream = RSAEncryptOutputStream(publicKey, outputStream)
 * encryptedStream.write(data)
 * encryptedStream.close()
 * ```
 */
class RSAPublicKey
{
    private val publicKey : PublicKey

    /**
     * 
     */
    internal constructor(publicKey : PublicKey)
    {
        this.publicKey = publicKey
    }

    /**
     * Creates an RSA public key from an input stream.
     *
     * @param inputStream The input stream to read the key from.
     */
    constructor(inputStream : InputStream)
    {
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)

        val modulus = inputStream.readBigInteger()
        val exponent = inputStream.readBigInteger()

        val publicKeySpec = RSAPublicKeySpec(modulus, exponent)
        this.publicKey = keyFactory.generatePublic(publicKeySpec)
    }

    /**
     * Saves the public key to an output stream.
     *
     * @param outputStream The output stream to write the key to.
     */
    fun save(outputStream : OutputStream)
    {
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)

        val publicKeySpec = keyFactory.getKeySpec(this.publicKey,
                                                  RSAPublicKeySpec::class.java)

        outputStream.writeBigInteger(publicKeySpec.modulus)
        outputStream.writeBigInteger(publicKeySpec.publicExponent)
        outputStream.flush()
    }

    /**
     * Returns a cipher for encryption.
     *
     * @return A cipher for encryption.
     * 
     */
    internal fun cipher() : Cipher
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher
    }

    /**
     * Encrypts an input stream.
     *
     * @param clearStream The input stream to encrypt.
     * @param encryptedStream The output stream to write the encrypted data to.
     */
    fun encrypt(clearStream : InputStream, encryptedStream : OutputStream)
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val temp = ByteArray(245)
        var crypted : ByteArray

        var read : Int = clearStream.readFully(temp)

        while (read >= 0)
        {
            crypted = cipher.doFinal(cipher.update(temp, 0, read))
            encryptedStream.write(crypted.size % 256)
            encryptedStream.write(crypted)
            read = clearStream.readFully(temp)
        }

        encryptedStream.flush()
    }

    /**
     * Verifies a signature.
     *
     * @param message The message that was signed.
     * @param signature The signature to verify.
     * @return `true` if the signature is valid, `false` otherwise.
     */
    fun validSignature(message : InputStream, signature : InputStream) : Boolean
    {
        val sign = Signature.getInstance(RSA_SIGNATURE)
        sign.initVerify(this.publicKey)
        var temp = ByteArray(4096)
        var read = message.read(temp)

        while (read >= 0)
        {
            sign.update(temp, 0, read)

            read = message.read(temp)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        read = signature.read(temp)

        while (read >= 0)
        {
            byteArrayOutputStream.write(temp, 0, read)

            read = signature.read(temp)
        }

        byteArrayOutputStream.flush()
        temp = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()

        return sign.verify(temp)
    }

    override fun toString() : String =
        this.publicKey.toString()
}
