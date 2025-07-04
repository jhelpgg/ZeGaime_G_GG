package fr.khelp.zegaime.utils.collections.maps

/**
 * Element of [IntMap]
 * @param key Element key
 * @param value Element value
 * @param T Mapped value type
 */
internal class IntMapElement<T>(val key : Int, var value : T? = null) : Comparable<IntMapElement<T>>
{
    /** Hash code */
    override fun hashCode() : Int = this.key

    /**
     * Indicates if an object is equals to this element
     */
    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                              -> true
            null == other || other !is IntMapElement<*> -> false
            else                                        -> this.key == other.key
        }

    /**
     * Compare with an other element
     */
    override operator fun compareTo(other : IntMapElement<T>) : Int = this.key - other.key

    /**
     * String representation
     */
    override fun toString() : String = "${this.key} => ${this.value}"
}
