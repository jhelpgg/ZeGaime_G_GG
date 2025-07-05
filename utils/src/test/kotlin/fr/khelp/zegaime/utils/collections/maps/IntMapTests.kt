package fr.khelp.zegaime.utils.collections.maps

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IntMapTests
{
    private lateinit var intMap: IntMap<String>

    @BeforeEach
    fun setup()
    {
        intMap = IntMap()
        intMap[5] = "five"
        intMap[2] = "two"
        intMap[8] = "eight"
    }

    @Test
    fun `get value`()
    {
        Assertions.assertEquals("five", intMap[5])
        Assertions.assertEquals("two", intMap[2])
        Assertions.assertEquals("eight", intMap[8])
        Assertions.assertNull(intMap[3])
    }

    @Test
    fun `set value`()
    {
        intMap[3] = "three"
        Assertions.assertEquals("three", intMap[3])
        Assertions.assertEquals(4, intMap.size)
    }

    @Test
    fun `update value`()
    {
        intMap[5] = "new_five"
        Assertions.assertEquals("new_five", intMap[5])
        Assertions.assertEquals(3, intMap.size)
    }

    @Test
    fun `remove value`()
    {
        intMap.remove(2)
        Assertions.assertNull(intMap[2])
        Assertions.assertEquals(2, intMap.size)
    }

    @Test
    fun `iterator`()
    {
        val list = intMap.toList()
        Assertions.assertEquals(3, list.size)
        Assertions.assertEquals(Pair(2, "two"), list[0])
        Assertions.assertEquals(Pair(5, "five"), list[1])
        Assertions.assertEquals(Pair(8, "eight"), list[2])
    }

    @Test
    fun `keys`()
    {
        val keys = intMap.keys().toList()
        Assertions.assertEquals(3, keys.size)
        Assertions.assertEquals(2, keys[0])
        Assertions.assertEquals(5, keys[1])
        Assertions.assertEquals(8, keys[2])
    }

    @Test
    fun `values`()
    {
        val values = intMap.values().toList()
        Assertions.assertEquals(3, values.size)
        Assertions.assertEquals("two", values[0])
        Assertions.assertEquals("five", values[1])
        Assertions.assertEquals("eight", values[2])
    }

    @Test
    fun `clear`()
    {
        intMap.clear()
        Assertions.assertTrue(intMap.empty)
        Assertions.assertEquals(0, intMap.size)
    }
}
