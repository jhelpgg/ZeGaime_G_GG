package fr.khelp.zegaime.utils.collections.iterations

/**
 * Iterator that iterate a list in reverse order
 * @param list List to iterate
 * @param T List element type
 */
class ListInvertedIterator<T>(private val list : List<T>) : Iterator<T>
{
    /** Current index */
    private var index = this.list.size - 1

    /**
     * Indicates if their more element to iterate
     */
    override fun hasNext() : Boolean =
        this.index >= 0

    /**
     * Next element
     */
    override fun next() : T
    {
        if (this.index < 0)
        {
            throw NoSuchElementException("No more elements to iterates")
        }

        val value = this.list[this.index]
        this.index --
        return value
    }
}
