package fr.khelp.zegaime.utils.collections.maps

import fr.khelp.zegaime.utils.collections.iterations.transform
import fr.khelp.zegaime.utils.collections.lists.SortedArray
import java.util.Optional

/**
 * Map of integer to something
 *
 * It is based on [SortedArray] of [IntMapElement]
 * @param T Mapped value type
 */
class IntMap<T> : Iterable<Pair<Int, T>>
{
    /** The map */
    private val map = SortedArray<IntMapElement<T>>(true)

    /** Map size */
    val size : Int get() = this.map.size

    /** Indicates if map is empty */
    val empty : Boolean get() = this.map.empty

    /** Indicates if map is not empty */
    val notEmpty : Boolean get() = this.map.notEmpty

    /**
     * Obtain a value from a key
     *
     * @param key Key to look for
     * @return The mapped value or `null` if key not exists
     */
    operator fun get(key : Int) : T?
    {
        val elementIndex = this.map.indexOf(IntMapElement(key))

        return if (elementIndex >= 0) this.map[elementIndex].value else null
    }

    /**
     * Associate a value to a key
     *
     * @param key Key to associate
     * @param value Value to associate
     */
    operator fun set(key : Int, value : T)
    {
        val elementIndex = this.map.indexOf(IntMapElement(key))

        if (elementIndex >= 0)
        {
            this.map[elementIndex].value = value
        }
        else
        {
            this.map += IntMapElement<T>(key, value)
        }
    }

    /**
     * Remove a key from the map
     *
     * @param key Key to remove
     */
    fun remove(key : Int)
    {
        this.map -= IntMapElement(key)
    }

    /**
     * Iterator on (key, value)
     */
    override fun iterator() : Iterator<Pair<Int, T>> =
        this.map.iterator().transform { intMapElement -> Pair(intMapElement.key, intMapElement.value!!) }

    /**
     * Iterable on keys
     */
    fun keys() : Iterable<Int> =
        this.map.transform { intMapElement -> intMapElement.key }

    /**
     * Iterable on values
     */
    fun values() : Iterable<T> =
        this.map.transform { intMapElement -> intMapElement.value!! }

    /**
     * Clear the map
     */
    fun clear()
    {
        this.map.clear()
    }

    /**
     * Get all `(key, value)` couple from value
     */
    fun fromValue(value : T, collector : (key : Int, value : T) -> Boolean)
    {
        for (element in this.map)
        {
            if (element.value == value)
            {
                if(!collector(element.key, element.value!!))
                {
                    return
                }
            }
        }
    }

    /**
     * Get first from value
     */
    fun firstFromValue(value : T) : Optional<Pair<Int, T>>
    {
        var response : Optional<Pair<Int, T>> = Optional.empty()

        this.fromValue(value) { key, valueFound ->
            response = Optional.of(Pair(key, valueFound))
            false
        }

        return response
    }

    /**
     * String representation
     */
    override fun toString() : String = this.map.toString()
}