package fr.khelp.zegaime.utils.collections.lists

import fr.khelp.zegaime.utils.collections.iterations.ListInvertedIterable
import fr.khelp.zegaime.utils.comparators.ComparableNaturalOrderComparator

/**
 * Create an array of [Comparable] sorted on their "natural order"
 * @param C Element type
 * @param unique `true` to have unique elements
 * @return Created sorted array
 */
fun <C : Comparable<C>> SortedArray(unique : Boolean = false) =
    SortedArray<C>(ComparableNaturalOrderComparator(), unique)

/**
 * Do action on all elements followed on reverse way (from end to begin) without create on other list
 * @param action Action to play on each element
 */
inline fun <T> List<T>.forEachReversed(action : (T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(this[index])
    }
}

/**
 * Do action on all elements with their index followed on reverse way (from end to begin) without create on other list
 * @param action Action to play on each element
 */
inline fun <T> List<T>.forEachReversedWithIndex(action : (Int, T) -> Unit)
{
    for (index in this.size - 1 downTo 0)
    {
        action(index, this[index])
    }
}

/**
 * Create an iterable that iterate the list in reverse order without create a new list
 * @return Iterable that iterate this list in reverse order
 */
fun <T> List<T>.inverted() : Iterable<T> = ListInvertedIterable(this)
