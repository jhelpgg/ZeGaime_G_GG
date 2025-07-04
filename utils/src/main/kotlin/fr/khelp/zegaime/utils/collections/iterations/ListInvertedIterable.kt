package fr.khelp.zegaime.utils.collections.iterations

/**
 * Iterable that iterate a list in reverse order
 * @param list List to iterate
 * @param T List element type
 */
class ListInvertedIterable<T>(private val list : List<T>) : Iterable<T>
{
    /**
     * Create the iterator
     */
    override fun iterator() : Iterator<T> = ListInvertedIterator<T>(this.list)
}
