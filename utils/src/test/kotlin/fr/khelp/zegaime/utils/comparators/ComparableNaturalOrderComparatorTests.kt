package fr.khelp.zegaime.utils.comparators

import fr.khelp.zegaime.utils.test.TestParent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComparableNaturalOrderComparatorTests
{
    @Test
    fun compareTo()
    {
        val comparator = ComparableNaturalOrderComparator<TestParent>()
        Assertions.assertTrue(comparator.compare(TestParent("ananas"), TestParent("banana")) < 0)
    }
}