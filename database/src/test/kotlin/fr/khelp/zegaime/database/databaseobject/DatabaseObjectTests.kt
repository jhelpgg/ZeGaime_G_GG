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

        Assertions.assertTrue(result.hasNext())
        val retrieved = result.next()
        Assertions.assertFalse(result.hasNext())
        result.close()
        Assertions.assertEquals(testObject.name, retrieved.name)
        Assertions.assertEquals(testObject.age, retrieved.age)
    }

    @Test
    fun `linked objects`()
    {
        val addressJumpStreet = Address("Jump street", 21, this.database).waitCreated<Address>()
        val addressSpace = Address("Space", 7777777, database).waitCreated<Address>()
        val personArthur = Person("Arthur", 42, addressJumpStreet, this.database).waitCreated<Person>()
        val personJoe = Person("Joe", 1, addressJumpStreet, this.database).waitCreated<Person>()
        val personDandy = Person("Dandy", 33, addressSpace, this.database).waitCreated<Person>()

        var resultPersons = DatabaseObject.select<Person>(this.database) {
            condition = Person::age UPPER_EQUALS 18
        }

        Assertions.assertTrue(resultPersons.hasNext())
        var person = resultPersons.next()
        Assertions.assertEquals(personArthur.name, person.name)
        Assertions.assertEquals(personArthur.age, person.age)
        Assertions.assertEquals(addressJumpStreet.street, person.address.street)
        Assertions.assertEquals(addressJumpStreet.number, person.address.number)

        Assertions.assertTrue(resultPersons.hasNext())
        person = resultPersons.next()
        Assertions.assertEquals(personDandy.name, person.name)
        Assertions.assertEquals(personDandy.age, person.age)
        Assertions.assertEquals(addressSpace.street, person.address.street)
        Assertions.assertEquals(addressSpace.number, person.address.number)

        Assertions.assertFalse(resultPersons.hasNext())
        resultPersons.close()

        /*--*/

        resultPersons = DatabaseObject.select<Person>(this.database) {
            condition = Person::address EQUALS addressJumpStreet
        }
        Assertions.assertTrue(resultPersons.hasNext())
        Assertions.assertEquals(personArthur, resultPersons.next())
        Assertions.assertTrue(resultPersons.hasNext())
        Assertions.assertEquals(personJoe, resultPersons.next())
        Assertions.assertFalse(resultPersons.hasNext())
        resultPersons.close()
    }
}
