package fr.khelp.zegaime.security.des

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class CrypterDESTests
{
    @Test
    fun `encrypt and decrypt`()
    {
        val crypter = CrypterDES("my_password")
        val clearText = "This is a secret message"
        val clearStream = ByteArrayInputStream(clearText.toByteArray())
        val encryptedStream = ByteArrayOutputStream()
        crypter.encrypt(clearStream, encryptedStream)
        val encryptedData = encryptedStream.toByteArray()
        val encryptedInputStream = ByteArrayInputStream(encryptedData)
        val decryptedStream = ByteArrayOutputStream()
        crypter.decrypt(encryptedInputStream, decryptedStream)
        val decryptedText = decryptedStream.toString()
        Assertions.assertEquals(clearText, decryptedText)
    }

    @Test
    fun `password valid`()
    {
        val crypter = CrypterDES("my_password")
        Assertions.assertTrue(crypter.passwordValid("my_password"))
        Assertions.assertFalse(crypter.passwordValid("wrong_password"))
    }
}
