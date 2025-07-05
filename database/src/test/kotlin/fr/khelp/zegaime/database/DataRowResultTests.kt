package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.io.deleteFull
import java.io.File
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DataRowResultTests
{
    private lateinit var database : Database
    private lateinit var table : Table

    @BeforeEach
    fun setup()
    {
        this.database = Database.database("user", "password", "DataRowResultTests/test")
        this.table = this.database.table("test_table") {
            "name" AS DataType.STRING
            "age" AS DataType.INTEGER
        }
    }

    @AfterEach
    fun tearDown()
    {
        this.database.close()
        File("DataRowResultTests").deleteFull(tryOnExitIfFail = true)
    }

    @Test
    fun `get values`()
    {
        this.table.insert {
            "name" IS "John"
            "age" IS 42
        }

        val result = this.table.select {
            +"name"
            +"age"
        }

        Assertions.assertTrue(result.hasNext)
        result.next {
            Assertions.assertEquals("John", getString(columnRange("name")))
            Assertions.assertEquals(42, getInt(this.table.getColumn("age")))
        }
        Assertions.assertFalse(result.hasNext)
        result.close()
    }
}
