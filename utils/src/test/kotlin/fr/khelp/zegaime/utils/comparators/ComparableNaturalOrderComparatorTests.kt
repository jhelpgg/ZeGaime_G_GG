package fr.khelp.zegaime.utils.comparators

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComparableNaturalOrderComparatorTests
{
    @Test
    fun `compare integers`()
    {
        val comparator = ComparableNaturalOrderComparator<Int>()
        Assertions.assertTrue(comparator.compare(1, 2) < 0)
        Assertions.assertTrue(comparator.compare(2, 1) > 0)
        Assertions.assertEquals(0, comparator.compare(1, 1))
    }

    @Test
    fun `compare strings`()
    {
        val comparator = ComparableNaturalOrderComparator<String>()
        Assertions.assertTrue(comparator.compare("a", "b") < 0)
        Assertions.assertTrue(comparator.compare("b", "a") > 0)
        Assertions.assertEquals(0, comparator.compare("a", "a"))
    }

    @Test
    fun `compare custom objects`()
    {
        data class Person(val name : String, val age : Int) : Comparable<Person>
        {
            override fun compareTo(other : Person) : Int =
                this.age.compareTo(other.age)
        }

        val comparator = ComparableNaturalOrderComparator<Person>()
        val person1 = Person("Alice", 25)
        val person2 = Person("Bob", 30)
        val person3 = Person("Charlie", 25)

        Assertions.assertTrue(comparator.compare(person1, person2) < 0)
        Assertions.assertTrue(comparator.compare(person2, person1) > 0)
        Assertions.assertEquals(0, comparator.compare(person1, person3))
    }
}
