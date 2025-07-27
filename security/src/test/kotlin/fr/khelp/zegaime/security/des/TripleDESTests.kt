package fr.khelp.zegaime.security.des

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TripleDESTests
{
    @Test
    fun `encrypt and decrypt`()
    {
        val tripleDES = TripleDES("my_login", "my_password")
        val clearText = "This is a secret message"
        val clearStream = ByteArrayInputStream(clearText.toByteArray())
        val encryptedStream = ByteArrayOutputStream()
        tripleDES.encrypt(clearStream, encryptedStream)
        val encryptedData = encryptedStream.toByteArray()
        val encryptedInputStream = ByteArrayInputStream(encryptedData)
        val decryptedStream = ByteArrayOutputStream()
        tripleDES.decrypt(encryptedInputStream, decryptedStream)
        val decryptedText = decryptedStream.toString()
        Assertions.assertEquals(clearText, decryptedText)
    }

    @Test
    fun valid()
    {
        val tripleDES = TripleDES("my_login", "my_password")
        Assertions.assertTrue(tripleDES.valid("my_login", "my_password"))
        Assertions.assertFalse(tripleDES.valid("wrong_login", "my_password"))
        Assertions.assertFalse(tripleDES.valid("my_login", "wrong_password"))
    }
}
