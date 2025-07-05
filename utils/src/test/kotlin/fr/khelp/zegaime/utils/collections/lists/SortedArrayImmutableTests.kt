package fr.khelp.zegaime.utils.collections.lists

import fr.khelp.zegaime.utils.comparators.ComparableNaturalOrderComparator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SortedArrayImmutableTests
{
    private lateinit var sortedArray: SortedArray<Int>
    private lateinit var immutableList: List<Int>

    @BeforeEach
    fun setup()
    {
        sortedArray = SortedArray(ComparableNaturalOrderComparator<Int>())
        sortedArray += 5
        sortedArray += 2
        sortedArray += 8
        immutableList = sortedArray.immutableList()
    }

    @Test
    fun `size`()
    {
        Assertions.assertEquals(3, immutableList.size)
    }

    @Test
    fun `contains`()
    {
        Assertions.assertTrue(immutableList.contains(5))
        Assertions.assertFalse(immutableList.contains(3))
    }

    @Test
    fun `containsAll`()
    {
        Assertions.assertTrue(immutableList.containsAll(listOf(2, 8)))
        Assertions.assertFalse(immutableList.containsAll(listOf(2, 9)))
    }

    @Test
    fun `get`()
    {
        Assertions.assertEquals(2, immutableList[0])
        Assertions.assertEquals(5, immutableList[1])
        Assertions.assertEquals(8, immutableList[2])
    }

    @Test
    fun `indexOf`()
    {
        Assertions.assertEquals(1, immutableList.indexOf(5))
        Assertions.assertEquals(-1, immutableList.indexOf(3))
    }

    @Test
    fun `isEmpty`()
    {
        Assertions.assertFalse(immutableList.isEmpty())
        sortedArray.clear()
        Assertions.assertTrue(immutableList.isEmpty())
    }

    @Test
    fun `iterator`()
    {
        val iterator = immutableList.iterator()
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(2, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(5, iterator.next())
        Assertions.assertTrue(iterator.hasNext())
        Assertions.assertEquals(8, iterator.next())
        Assertions.assertFalse(iterator.hasNext())
    }

    @Test
    fun `subList`()
    {
        val subList = immutableList.subList(1, 3)
        Assertions.assertEquals(2, subList.size)
        Assertions.assertEquals(5, subList[0])
        Assertions.assertEquals(8, subList[1])
    }
}
