package fr.khelp.zegaime.utils.collections.iterations

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IterableTransformedTests
{
    @Test
    fun `transform elements`()
    {
        val source = listOf(1, 2, 3, 4, 5)
        val transformation = { number : Int -> "Number: $number" }
        val iterableTransformed = IterableTransformed(transformation, source)
        val transformed = iterableTransformed.toList()
        Assertions.assertEquals(5, transformed.size)
        Assertions.assertEquals("Number: 1", transformed[0])
        Assertions.assertEquals("Number: 2", transformed[1])
        Assertions.assertEquals("Number: 3", transformed[2])
        Assertions.assertEquals("Number: 4", transformed[3])
        Assertions.assertEquals("Number: 5", transformed[4])
    }

    @Test
    fun `empty list`()
    {
        val source = emptyList<Int>()
        val transformation = { number : Int -> "Number: $number" }
        val iterableTransformed = IterableTransformed(transformation, source)
        val transformed = iterableTransformed.toList()
        Assertions.assertTrue(transformed.isEmpty())
    }
}
