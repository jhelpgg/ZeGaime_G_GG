package fr.khelp.zegaime.security.rsa

import fr.khelp.zegaime.security.des.TripleDES
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RSAKeyPairTests
{
    @Test
    fun `encrypt and decrypt`()
    {
        val keyPair = RSAKeyPair()
        val publicKey = keyPair.publicKey
        val clearText = "This is a secret message"
        val clearStream = ByteArrayInputStream(clearText.toByteArray())
        val encryptedStream = ByteArrayOutputStream()
        publicKey.encrypt(clearStream, encryptedStream)
        val encryptedData = encryptedStream.toByteArray()
        val encryptedInputStream = ByteArrayInputStream(encryptedData)
        val decryptedStream = ByteArrayOutputStream()
        keyPair.decrypt(encryptedInputStream, decryptedStream)
        val decryptedText = decryptedStream.toString()
        Assertions.assertEquals(clearText, decryptedText)
    }

    @Test
    fun `sign and verify`()
    {
        val keyPair = RSAKeyPair()
        val publicKey = keyPair.publicKey
        val message = "This is a message to sign"
        val messageStream = ByteArrayInputStream(message.toByteArray())
        val signatureStream = ByteArrayOutputStream()
        keyPair.sign(messageStream, signatureStream)
        val signature = signatureStream.toByteArray()
        val messageInputStream = ByteArrayInputStream(message.toByteArray())
        val signatureInputStream = ByteArrayInputStream(signature)
        Assertions.assertTrue(publicKey.validSignature(messageInputStream, signatureInputStream))
    }

    @Test
    fun `save and load`()
    {
        val tripleDES = TripleDES("my_login", "my_password")
        val keyPair = RSAKeyPair()
        val outputStream = ByteArrayOutputStream()
        keyPair.save(tripleDES, outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val loadedKeyPair = RSAKeyPair(tripleDES, inputStream)
        Assertions.assertEquals(keyPair.publicKey.toString(), loadedKeyPair.publicKey.toString())
    }
}
