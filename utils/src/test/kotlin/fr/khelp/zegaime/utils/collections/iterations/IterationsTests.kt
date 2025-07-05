package fr.khelp.zegaime.utils.collections.iterations

import fr.khelp.zegaime.utils.test.TestChild1
import fr.khelp.zegaime.utils.test.TestChild2
import fr.khelp.zegaime.utils.test.TestParent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterationsTests
{
    @Test
    fun `iterator select generic`()
    {
        val iterator = listOf(42, 73, 85, 44, 666, 999).listIterator()
        val iteratorSelect = iterator.select { value -> value % 2 == 0 }

        val iteratorSelected =
            Assertions.assertInstanceOf(IteratorSelected::class.java, iteratorSelect) as IteratorSelected<Int>
        Assertions.assertTrue(iteratorSelected.criteria(88))
        Assertions.assertTrue(iteratorSelected.criteria(22))
        Assertions.assertFalse(iteratorSelected.criteria(11))
        Assertions.assertFalse(iteratorSelected.criteria(99))
        Assertions.assertEquals(iterator, iteratorSelected.iterator)

        val collected = ArrayList<Int>()

        for (value in iteratorSelected)
        {
            collected.add(value)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals(42, collected[0])
        Assertions.assertEquals(44, collected[1])
        Assertions.assertEquals(666, collected[2])
    }

    @Test
    fun `iterator select with IteratorSelected`()
    {
        val iterator = listOf(42, 73, 85, 44, 666, 999).listIterator()
        val iteratorSelect = iterator.select { value -> value % 2 == 0 }
        val iteratorSelect2 = iteratorSelect.select { value -> value >= 50 }

        val iteratorSelected =
            Assertions.assertInstanceOf(IteratorSelected::class.java, iteratorSelect2) as IteratorSelected<Int>
        Assertions.assertTrue(iteratorSelected.criteria(88))
        Assertions.assertFalse(iteratorSelected.criteria(22))
        Assertions.assertFalse(iteratorSelected.criteria(11))
        Assertions.assertFalse(iteratorSelected.criteria(99))
        Assertions.assertEquals(iterator, iteratorSelected.iterator)

        val collected = ArrayList<Int>()

        for (value in iteratorSelected)
        {
            collected.add(value)
        }

        Assertions.assertEquals(1, collected.size)
        Assertions.assertEquals(666, collected[0])
    }

    @Test
    fun `iterator transform`()
    {
        val iterator = listOf(41, 72, 84, 43, 665, 998).listIterator()
        val collected = ArrayList<Int>()

        for (value in iterator.transform { value -> value + 1 })
        {
            collected.add(value)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals(42, collected[0])
        Assertions.assertEquals(44, collected[1])
        Assertions.assertEquals(666, collected[2])
    }

    @Test
    fun `iterator selectInstance`()
    {
        val iterator =
            listOf<TestParent>(TestChild1("Child 1.1"),
                               TestChild2("Child 2.1"),
                               TestChild1("Child 1.2"),
                               TestChild1("Child 1.3"),
                               TestChild2("Child 2.2"))
                .listIterator()

        val collected = ArrayList<TestChild1>()

        for (test in iterator.selectInstance<TestParent, TestChild1>())
        {
            collected.add(test)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals("Child 1.1", collected[0].name)
        Assertions.assertEquals("Child 1.2", collected[1].name)
        Assertions.assertEquals("Child 1.3", collected[2].name)
    }

    @Test
    fun `iterable select generic`()
    {
        val iterable = listOf(42, 73, 85, 44, 666, 999)
        val iterableSelect = iterable.select { value -> value % 2 == 0 }

        val iterableSelected =
            Assertions.assertInstanceOf(IterableSelected::class.java, iterableSelect) as IterableSelected<Int>
        Assertions.assertTrue(iterableSelected.criteria(88))
        Assertions.assertTrue(iterableSelected.criteria(22))
        Assertions.assertFalse(iterableSelected.criteria(11))
        Assertions.assertFalse(iterableSelected.criteria(99))
        Assertions.assertEquals(iterable, iterableSelected.iterable)

        val collected = ArrayList<Int>()

        for (value in iterableSelected)
        {
            collected.add(value)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals(42, collected[0])
        Assertions.assertEquals(44, collected[1])
        Assertions.assertEquals(666, collected[2])
    }

    @Test
    fun `iterable select with IterableSelected`()
    {
        val iterable = listOf(42, 73, 85, 44, 666, 999)
        val iterableSelect = iterable.select { value -> value % 2 == 0 }
        val iterableSelect2 = iterableSelect.select { value -> value >= 50 }

        val iterableSelected =
            Assertions.assertInstanceOf(IterableSelected::class.java, iterableSelect2) as IterableSelected<Int>
        Assertions.assertTrue(iterableSelected.criteria(88))
        Assertions.assertFalse(iterableSelected.criteria(22))
        Assertions.assertFalse(iterableSelected.criteria(11))
        Assertions.assertFalse(iterableSelected.criteria(99))
        Assertions.assertEquals(iterable, iterableSelected.iterable)

        val collected = ArrayList<Int>()

        for (value in iterableSelected)
        {
            collected.add(value)
        }

        Assertions.assertEquals(1, collected.size)
        Assertions.assertEquals(666, collected[0])
    }

    @Test
    fun `iterable transform`()
    {
        val iterable = listOf(41, 72, 84, 43, 665, 998)
        val collected = ArrayList<Int>()

        for (value in iterable.transform { value -> value + 1 })
        {
            collected.add(value)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals(42, collected[0])
        Assertions.assertEquals(44, collected[1])
        Assertions.assertEquals(666, collected[2])
    }

    @Test
    fun `iterable selectInstance`()
    {
        val iterable =
            listOf<TestParent>(TestChild1("Child 1.1"),
                               TestChild2("Child 2.1"),
                               TestChild1("Child 1.2"),
                               TestChild1("Child 1.3"),
                               TestChild2("Child 2.2"))

        val collected = ArrayList<TestChild1>()

        for (test in iterable.selectInstance<TestParent, TestChild1>())
        {
            collected.add(test)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals("Child 1.1", collected[0].name)
        Assertions.assertEquals("Child 1.2", collected[1].name)
        Assertions.assertEquals("Child 1.3", collected[2].name)
    }
}