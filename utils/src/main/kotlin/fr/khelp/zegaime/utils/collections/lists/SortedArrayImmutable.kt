package fr.khelp.zegaime.utils.collections.lists

/**
 * Immutable view of [SortedArray]
 * @param sortedArray Sorted array to view
 * @param T Element type
 */
internal class SortedArrayImmutable<T>(private val sortedArray: SortedArray<T>) : List<T>
{
    /** List size */
    override val size: Int get() = this.sortedArray.size

    /**
     * Indicates if an element is in the list
     */
    override fun contains(element: T): Boolean =
        element in this.sortedArray

    /**
     * Indicates if all elements of a collection are in the list
     */
    override fun containsAll(elements: Collection<T>): Boolean =
        this.sortedArray.containsAll(elements)

    /**
     * Obtain an element at given index
     */
    override fun get(index: Int): T =
        this.sortedArray[index]

    /**
     * Index of an element
     */
    override fun indexOf(element: T): Int =
        this.sortedArray.indexOf(element)

    /**
     * Indicates if list is empty
     */
    override fun isEmpty(): Boolean =
        this.sortedArray.empty

    /**
     * Iterator on elements
     */
    override fun iterator(): Iterator<T> =
        this.sortedArray.iterator()

    /**
     * Last index of an element
     */
    override fun lastIndexOf(element: T): Int =
        this.sortedArray.indexOf(element)

    /**
     * List iterator on elements
     */
    override fun listIterator(): ListIterator<T> =
        this.sortedArray.listIterator()

    /**
     * List iterator on elements starting at given index
     */
    override fun listIterator(index: Int): ListIterator<T> =
        this.sortedArray.listIterator(index)

    /**
     * Extract a part of the list
     */
    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        this.sortedArray.subList(fromIndex, toIndex)
            .immutableList()
}