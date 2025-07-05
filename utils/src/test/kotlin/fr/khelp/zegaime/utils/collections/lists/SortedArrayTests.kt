package fr.khelp.zegaime.utils.collections.lists

import fr.khelp.zegaime.utils.comparators.ComparableNaturalOrderComparator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SortedArrayTests
{
    @Test
    fun `add and check order`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        sortedArray += 1
        Assertions.assertEquals(4, sortedArray.size)
        Assertions.assertEquals(1, sortedArray[0])
        Assertions.assertEquals(2, sortedArray[1])
        Assertions.assertEquals(5, sortedArray[2])
        Assertions.assertEquals(8, sortedArray[3])
    }

    @Test
    fun `unique mode`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator(), unique = true)
        sortedArray += 5
        sortedArray += 2
        sortedArray += 5
        sortedArray += 8
        sortedArray += 2
        Assertions.assertEquals(3, sortedArray.size)
        Assertions.assertEquals(2, sortedArray[0])
        Assertions.assertEquals(5, sortedArray[1])
        Assertions.assertEquals(8, sortedArray[2])
    }

    @Test
    fun `remove element`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        sortedArray -= 5
        Assertions.assertEquals(2, sortedArray.size)
        Assertions.assertEquals(2, sortedArray[0])
        Assertions.assertEquals(8, sortedArray[1])
    }

    @Test
    fun `contains element`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        Assertions.assertTrue(sortedArray.contains(5))
        Assertions.assertFalse(sortedArray.contains(3))
    }

    @Test
    fun `indexOf element`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        Assertions.assertEquals(1, sortedArray.indexOf(5))
        Assertions.assertTrue(sortedArray.indexOf(3) < 0)
    }

    @Test
    fun `clear array`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        sortedArray.clear()
        Assertions.assertTrue(sortedArray.isEmpty())
    }

    @Test
    fun `subList`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        sortedArray += 1
        sortedArray += 9
        val subList = sortedArray.subList(1, 4)
        Assertions.assertEquals(3, subList.size)
        Assertions.assertEquals(2, subList[0])
        Assertions.assertEquals(5, subList[1])
        Assertions.assertEquals(8, subList[2])
    }

    @Test
    fun `intervalOf`()
    {
        val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        Assertions.assertEquals(Pair(1, 1), sortedArray.intervalOf(5))
        Assertions.assertEquals(Pair(0, 1), sortedArray.intervalOf(3))
        Assertions.assertEquals(Pair(-1, 0), sortedArray.intervalOf(1))
        Assertions.assertEquals(Pair(3, -1), sortedArray.intervalOf(9))
    }
}
