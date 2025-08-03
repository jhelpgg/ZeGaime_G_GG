package fr.khelp.zegaime.utils.io

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CodeTests
{
    @Test
    fun cipher()
    {
        Assertions.assertEquals("5308D", Code.cipher("sword"))
        Assertions.assertEquals("74E 5308D 5412E !", Code.cipher("The sword shine !"))
    }

    @Test
    fun decipher()
    {
        val collected = ArrayList<String>()
        Code.decipher("5308D") { word -> collected.add(word) }

        Assertions.assertEquals(12, collected.size, "Content = $collected")

        Assertions.assertEquals("SMOKD", collected[0])
        Assertions.assertEquals("SMORD", collected[1])
        Assertions.assertEquals("SMOXD", collected[2])

        Assertions.assertEquals("SMQKD", collected[3])
        Assertions.assertEquals("SMQRD", collected[4])
        Assertions.assertEquals("SMQXD", collected[5])

        Assertions.assertEquals("SWOKD", collected[6])
        Assertions.assertEquals("SWORD", collected[7])
        Assertions.assertEquals("SWOXD", collected[8])

        Assertions.assertEquals("SWQKD", collected[9])
        Assertions.assertEquals("SWQRD", collected[10])
        Assertions.assertEquals("SWQXD", collected[11])

        Code.decipher("74E 5308D 5412E !") { word -> println(word) }
    }
}
