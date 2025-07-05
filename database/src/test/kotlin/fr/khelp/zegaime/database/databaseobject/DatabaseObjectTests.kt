package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Database
import fr.khelp.zegaime.utils.io.deleteFull
import java.io.File
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DatabaseObjectTests
{
    private lateinit var database : Database

    @BeforeEach
    fun setup()
    {
        database = Database.database("user", "password", "DatabaseObjectTests/test")
    }

    @AfterEach
    fun tearDown()
    {
        database.close()
        File("DatabaseObjectTests").deleteFull(tryOnExitIfFail = true)
    }

    @Test
    fun `save and retrieve`()
    {
        val testObject = TestObject(name = "John", age = 42, database = this.database).waitCreated<TestObject>()
        val result = DatabaseObject.select<TestObject>(this.database) {
            "ID" EQUALS_ID testObject.databaseID
        }

        Assertions.assertTrue(result.hasNext)
        val retrieved = result.next()
        Assertions.assertFalse(result.hasNext)
        result.close()
        Assertions.assertEquals(testObject.name, retrieved.name)
        Assertions.assertEquals(testObject.age, retrieved.age)
    }
}
