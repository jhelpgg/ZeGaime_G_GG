package fr.khelp.zegaime.utils.collections.iterations

class ListInvertedIterable<T>(private val list : List<T>) : Iterable<T>
{
    override fun iterator() : Iterator<T> = ListInvertedIterator<T>(this.list)
}
