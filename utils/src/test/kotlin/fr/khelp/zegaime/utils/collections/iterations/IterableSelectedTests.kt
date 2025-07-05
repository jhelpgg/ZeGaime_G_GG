package fr.khelp.zegaime.utils.collections.iterations

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterableSelectedTests
{
    @Test
    fun `for each`()
    {
        val collected = ArrayList<Int>()
        val iterableSelected = IterableSelected<Int>({ value -> value % 2 == 0 },
                                                     listOf(42, 73, 85, 44, 666, 999))

        for (value in iterableSelected)
        {
            collected.add(value)
        }

        Assertions.assertEquals(3, collected.size)
        Assertions.assertEquals(42, collected[0])
        Assertions.assertEquals(44, collected[1])
        Assertions.assertEquals(666, collected[2])
    }
}