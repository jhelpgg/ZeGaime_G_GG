package fr.khelp.zegaime.utils.collections.lists

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListsTests
{
    @Test
    fun `forEachReversed`()
    {
        val source = listOf(1, 2, 3)
        val result = mutableListOf<Int>()
        source.forEachReversed { result.add(it) }
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(3, result[0])
        Assertions.assertEquals(2, result[1])
        Assertions.assertEquals(1, result[2])
    }

    @Test
    fun `forEachReversedWithIndex`()
    {
        val source = listOf(10, 20, 30)
        val resultValues = mutableListOf<Int>()
        val resultIndices = mutableListOf<Int>()
        source.forEachReversedWithIndex { index, value ->
            resultIndices.add(index)
            resultValues.add(value)
        }
        Assertions.assertEquals(3, resultValues.size)
        Assertions.assertEquals(30, resultValues[0])
        Assertions.assertEquals(20, resultValues[1])
        Assertions.assertEquals(10, resultValues[2])
        Assertions.assertEquals(3, resultIndices.size)
        Assertions.assertEquals(2, resultIndices[0])
        Assertions.assertEquals(1, resultIndices[1])
        Assertions.assertEquals(0, resultIndices[2])
    }

    @Test
    fun `inverted`()
    {
        val source = listOf(1, 2, 3)
        val iterable = source.inverted()
        val result = iterable.toList()
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(3, result[0])
        Assertions.assertEquals(2, result[1])
        Assertions.assertEquals(1, result[2])
    }

    @Test
    fun `empty forEachReversed`()
    {
        val source = emptyList<Int>()
        var called = false
        source.forEachReversed { called = true }
        Assertions.assertFalse(called)
    }

    @Test
    fun `empty forEachReversedWithIndex`()
    {
        val source = emptyList<Int>()
        var called = false
        source.forEachReversedWithIndex { _, _ -> called = true }
        Assertions.assertFalse(called)
    }

    @Test
    fun `empty inverted`()
    {
        val source = emptyList<Int>()
        val iterable = source.inverted()
        Assertions.assertTrue(iterable.toList().isEmpty())
    }

    @Test
    fun `single element forEachReversed`()
    {
        val source = listOf(42)
        val result = mutableListOf<Int>()
        source.forEachReversed { result.add(it) }
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(42, result[0])
    }

    @Test
    fun `single element forEachReversedWithIndex`()
    {
        val source = listOf(42)
        val resultValues = mutableListOf<Int>()
        val resultIndices = mutableListOf<Int>()
        source.forEachReversedWithIndex { index, value ->
            resultIndices.add(index)
            resultValues.add(value)
        }
        Assertions.assertEquals(1, resultValues.size)
        Assertions.assertEquals(42, resultValues[0])
        Assertions.assertEquals(1, resultIndices.size)
        Assertions.assertEquals(0, resultIndices[0])
    }

    @Test
    fun `single element inverted`()
    {
        val source = listOf(42)
        val iterable = source.inverted()
        val result = iterable.toList()
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(42, result[0])
    }
}
