package fr.khelp.zegaime.security.rsa

import fr.khelp.zegaime.security.des.TripleDES
import fr.khelp.zegaime.security.exception.LoginPasswordInvalidException
import fr.khelp.zegaime.utils.io.readBigInteger
import fr.khelp.zegaime.utils.io.readFully
import fr.khelp.zegaime.utils.io.writeBigInteger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.RSAPrivateKeySpec
import javax.crypto.Cipher

/**
 * Represents an RSA key pair.
 *
 * Since it contains the private key, be careful when sharing the instance.
 * Only save it in a trusted and secure place.
 *
 * **Creation example:**
 * ```kotlin
 * val keyPair = RSAKeyPair()
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val encryptedStream = RSAEncryptOutputStream(keyPair.publicKey, outputStream)
 * encryptedStream.write(data)
 * encryptedStream.close()
 *
 * val decryptedStream = RSADecryptInputStream(keyPair, inputStream)
 * val decryptedData = decryptedStream.readAllBytes()
 * ```
 *
 * @property publicKey The public key of the key pair.
 */
class RSAKeyPair
{
    private val privateKey: PrivateKey
    /**
     * The public key of the key pair.
     */
    val publicKey: RSAPublicKey
    private var tripleDES: TripleDES? = null

    /**
     * Creates a new RSA key pair.
     */
    constructor()
    {
        val generator = KeyPairGenerator.getInstance(ALGORITHM_RSA)
        generator.initialize(2048)

        val keyPair = generator.generateKeyPair()
        this.privateKey = keyPair.private
        this.publicKey = RSAPublicKey(keyPair.public)
    }

    /**
     * Creates an RSA key pair from an encrypted input stream.
     *
     * @param tripleDES The Triple DES crypter to use for decryption.
     * @param inputStream The input stream to read the key pair from.
     * @throws LoginPasswordInvalidException If the login or password is not valid.
     */
    constructor(tripleDES: TripleDES, inputStream: InputStream)
    {
        try
        {
            this.tripleDES = tripleDES
            val byteArrayOutputStream = ByteArrayOutputStream()
            tripleDES.decrypt(inputStream, byteArrayOutputStream)
            inputStream.close()
            val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
            val modulus = byteArrayInputStream.readBigInteger()
            val exponent = byteArrayInputStream.readBigInteger()
            val privateKeySpec = RSAPrivateKeySpec(modulus, exponent)
            this.privateKey = KeyFactory.getInstance(ALGORITHM_RSA)
                .generatePrivate(privateKeySpec)
            this.publicKey = RSAPublicKey(byteArrayInputStream)
        }
        catch (exception: Exception)
        {
            throw LoginPasswordInvalidException()
        }
    }

    /**
     * Saves the key pair to an encrypted output stream.
     *
     * @param tripleDES The Triple DES crypter to use for encryption.
     * @param outputStream The output stream to write the key pair to.
     * @throws LoginPasswordInvalidException If the login or password is not valid.
     */
    fun save(tripleDES: TripleDES, outputStream: OutputStream)
    {
        val actualTripleDES = this.tripleDES

        if (actualTripleDES == null)
        {
            this.tripleDES = tripleDES
        }
        else if (!actualTripleDES.sameKeys(tripleDES))
        {
            Thread.sleep((128L..1024L).random())
            throw LoginPasswordInvalidException()
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        val keyFactory = KeyFactory.getInstance(ALGORITHM_RSA)
        val privateKeySpec = keyFactory.getKeySpec(this.privateKey,
                                                   RSAPrivateKeySpec::class.java)
        byteArrayOutputStream.writeBigInteger(privateKeySpec.modulus)
        byteArrayOutputStream.writeBigInteger(privateKeySpec.privateExponent)
        this.publicKey.save(byteArrayOutputStream)
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        tripleDES.encrypt(byteArrayInputStream, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    /**
     * Returns a cipher for decryption.
     *
     * For internal use only.
     *
     * @return A cipher for decryption.
     */
    internal fun cypher(): Cipher
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey)
        return cipher
    }

    /**
     * Decrypts an input stream.
     *
     * @param inputStream The input stream to decrypt.
     * @param outputStream The output stream to write the decrypted data to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun decrypt(inputStream: InputStream, outputStream: OutputStream)
    {
        val cipher = Cipher.getInstance(RSA_CIPHER)
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey)
        val temp = ByteArray(256)
        var size = inputStream.read()

        if (size == 0)
        {
            size = 256
        }

        var read: Int = inputStream.readFully(temp, 0, size)

        while (read >= 0 && size >= 0)
        {
            outputStream.write(cipher.doFinal(temp, 0, read))
            size = inputStream.read()

            if (size >= 0)
            {
                if (size == 0)
                {
                    size = 256
                }

                read = inputStream.readFully(temp, 0, size)
            }
        }

        outputStream.flush()
    }

    /**
     * Signs a message.
     *
     * @param message The message to sign.
     * @param signature The output stream to write the signature to.
     * @throws IOException On IO error.
     */
    @Throws(IOException::class)
    fun sign(message: InputStream, signature: OutputStream)
    {
        val sign = Signature.getInstance(RSA_SIGNATURE)
        sign.initSign(this.privateKey)
        val temp = ByteArray(4096)
        var read = message.read(temp)

        while (read >= 0)
        {
            sign.update(temp, 0, read)
            read = message.read(temp)
        }

        signature.write(sign.sign())
        signature.flush()
    }
}