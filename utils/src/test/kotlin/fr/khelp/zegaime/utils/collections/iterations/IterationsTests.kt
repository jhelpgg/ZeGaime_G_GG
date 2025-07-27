package fr.khelp.zegaime.utils.collections.iterations

import fr.khelp.zegaime.utils.test.TestChild1
import fr.khelp.zegaime.utils.test.TestChild2
import fr.khelp.zegaime.utils.test.TestParent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterationsTests
{
    @Test
    fun `iterator select`()
    {
        val source = listOf(42, 73, 85, 44, 666, 999)
        val iterator = source.iterator().select { it % 2 == 0 }
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(42, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(44, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(666, iterator.next())
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `iterator transform`()
    {
        val source = listOf(1, 2, 3)
        val iterator = source.iterator().transform { "N:$it" }
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("N:1", iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("N:2", iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("N:3", iterator.next())
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `iterator selectInstance`()
    {
        val source =
            listOf(TestChild1("c1"), TestChild2("2"), TestParent("p"), TestChild1("c2"))
        val iterator = source.iterator().selectInstance<TestParent, TestChild1>()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("c1", iterator.next().name)
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals("c2", iterator.next().name)
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `iterable select`()
    {
        val source = listOf(42, 73, 85, 44, 666, 999)
        val iterable = source.select { it % 2 == 0 }
        val result = iterable.toList()
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(42, result[0])
        Assertions.assertEquals(44, result[1])
        Assertions.assertEquals(666, result[2])
    }

    @Test
    fun `iterable transform`()
    {
        val source = listOf(1, 2, 3)
        val iterable = source.transform { "N:$it" }
        val result = iterable.toList()
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals("N:1", result[0])
        Assertions.assertEquals("N:2", result[1])
        Assertions.assertEquals("N:3", result[2])
    }

    @Test
    fun `iterable selectInstance`()
    {
        val source =
            listOf(TestChild1("c1"), TestChild2("2"), TestParent("p"), TestChild1("c2"))
        val iterable = source.selectInstance<TestParent, TestChild1>()
        val result = iterable.toList()
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("c1", result[0].name)
        Assertions.assertEquals("c2", result[1].name)
    }

    @Test
    fun `empty iterator select`()
    {
        val source = emptyList<Int>()
        val iterator = source.iterator().select { it % 2 == 0 }
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `empty iterator transform`()
    {
        val source = emptyList<Int>()
        val iterator = source.iterator().transform { "N:$it" }
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `empty iterator selectInstance`()
    {
        val source = emptyList<TestParent>()
        val iterator = source.iterator().selectInstance<TestParent, TestChild1>()
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `empty iterable select`()
    {
        val source = emptyList<Int>()
        val iterable = source.select { it % 2 == 0 }
        Assertions.assertTrue(iterable.toList().isEmpty())
    }

    @Test
    fun `empty iterable transform`()
    {
        val source = emptyList<Int>()
        val iterable = source.transform { "N:$it" }
        Assertions.assertTrue(iterable.toList().isEmpty())
    }

    @Test
    fun `empty iterable selectInstance`()
    {
        val source = emptyList<TestParent>()
        val iterable = source.selectInstance<TestParent, TestChild1>()
        Assertions.assertTrue(iterable.toList().isEmpty())
    }

    @Test
    fun `no match iterator select`()
    {
        val source = listOf(1, 3, 5)
        val iterator = source.iterator().select { it % 2 == 0 }
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `no match iterable select`()
    {
        val source = listOf(1, 3, 5)
        val iterable = source.select { it % 2 == 0 }
        Assertions.assertTrue(iterable.toList().isEmpty())
    }

    @Test
    fun `combined select`()
    {
        val source = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val iterable = source.select { it > 3 }.select { it < 8 }.select { it % 2 == 0 }
        val result = iterable.toList()
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(4, result[0])
        Assertions.assertEquals(6, result[1])
    }
}
