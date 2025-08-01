package fr.khelp.zegaime.utils.collections.lists

import fr.khelp.zegaime.utils.collections.iterations.EnumerationIteratorInt
import java.util.Arrays
import kotlin.math.max

/**
 * Array list of Int
 * @param initialSize Initial capacity
 */
class ArrayInt(initialSize : Int = 128) : Iterable<Int>
{
    /**Array of int*/
    private var array = IntArray(max(128, initialSize))

    /**Array size*/
    var size = 0
        private set

    /**
     * Indicates the array sorted status
     */
    var sorted : SortedStatus = SortedStatus.SORTED
        private set

    constructor(vararg integers : Int) : this(integers.size)
    {
        for (integer in integers)
        {
            this.add(integer)
        }
    }

    /**
     * Check if an index is valid
     *
     * @param index Index checked
     * @throws IllegalArgumentException if index not valid
     */
    private fun checkIndex(index : Int)
    {
        if (index < 0 || index >= this.size)
        {
            throw IllegalArgumentException("index must be in [0, ${this.size}[ not $index")
        }
    }

    /**
     * Expand, if it needs, the capacity
     */
    private fun expand()
    {
        if (this.size + 1 >= this.array.size)
        {
            var newSize = this.size + 1
            newSize += newSize / 10 + 1

            val temp = IntArray(newSize)
            System.arraycopy(this.array, 0, temp, 0, this.size)

            this.array = temp
        }
    }

    /**
     * Add an integer is the array
     *
     * @param integer Integer to add
     */
    fun add(integer : Int)
    {
        this.expand()

        this.sorted =
            when
            {
                this.size == 0                                                             -> SortedStatus.SORTED
                this.sorted == SortedStatus.SORTED && this.array[this.size - 1] <= integer -> SortedStatus.SORTED
                this.sorted == SortedStatus.SORTED                                         -> SortedStatus.NOT_SORTED
                else                                                                       -> this.sorted
            }


        this.array[this.size] = integer
        this.size++
    }

    /***
     * Add all elements of an array
     *
     * @param arrayInt Array to add
     * Array to add its elements
     */
    fun addAll(arrayInt : ArrayInt) = arrayInt.forEach(this::add)

    operator fun plusAssign(integer : Int) = this.add(integer)
    operator fun plusAssign(arrayInt : ArrayInt) = this.addAll(arrayInt)

    /**
     * Enumeration/Iterator over int
     */
    fun iteratorInt() = EnumerationIteratorInt(this.array.copyOf(this.size))

    /**
     * Clear the array
     */
    fun clear()
    {
        this.size = 0
        this.sorted = SortedStatus.SORTED
    }

    /**
     * Indicates if an integer is in the array.
     *
     * Search is on O(n) or O(LN(n)) if sorted
     *
     * @param integer Integer search
     * @return `true` if the integer is inside
     */
    operator fun contains(integer : Int) = this.index(integer) >= 0

    /**
     * Create a copy of the array
     *
     * @return The copy
     */
    fun copy() : ArrayInt
    {
        val copy = ArrayInt()

        val length = this.array.size
        copy.array = IntArray(length)
        System.arraycopy(this.array, 0, copy.array, 0, length)

        copy.size = this.size
        copy.sorted = this.sorted

        return copy
    }

    /**
     * Index of an integer or -1 if integer not in the array.
     *
     * Search is on O(n) or O(LN(n)) if sorted
     *
     * @param integer Integer search
     * @return Integer index or -1 if integer not in the array
     */
    fun index(integer : Int) : Int
    {
        if (this.sorted == SortedStatus.SORTED)
        {
            return this.indexSupposeSorted(integer)
        }

        for (i in 0 until this.size)
        {
            if (this.array[i] == integer)
            {
                return i
            }
        }

        return -1
    }

    /**
     * Index of an integer or -1 if integer not in the array.
     *
     * Search is in O(LN(n)) but works only if the array is sorted
     *
     * @param integer Integer search
     * @return Integer index or -1 if integer not in the array
     */
    private fun indexSupposeSorted(integer : Int) : Int
    {
        if (this.size <= 0)
        {
            return -1
        }

        var actual = this.array[0]

        if (integer < actual)
        {
            return -1
        }

        if (integer == actual)
        {
            return 0
        }

        var min = 0
        var max = this.size - 1

        actual = this.array[max]

        if (integer > actual)
        {
            return -1
        }

        if (integer == actual)
        {
            return max
        }

        var mil : Int
        while (min < max - 1)
        {
            mil = min + max shr 1
            actual = this.array[mil]

            if (integer == actual)
            {
                return mil
            }

            if (integer > actual)
            {
                min = mil
            }
            else
            {
                max = mil
            }
        }

        return -1
    }

    /**
     * Gets an integer from the array
     *
     * @param index Integer index
     * @return Integer
     */
    operator fun get(index : Int) : Int
    {
        this.checkIndex(index)

        return this.array[index]
    }

    /**
     * Insert an integer to a given index
     *
     * @param integer Integer to insert
     * @param index   Index where insert
     */
    fun insert(integer : Int, index : Int)
    {
        var indexLocal = index
        this.expand()

        if (indexLocal < 0)
        {
            indexLocal = 0
        }

        if (indexLocal >= this.size)
        {
            this.add(integer)

            return
        }

        this.sorted =
            when
            {
                this.sorted == SortedStatus.SORTED
                && (indexLocal == 0 || integer >= this.array[indexLocal - 1]) && integer <= this.array[indexLocal] -> SortedStatus.SORTED

                this.sorted == SortedStatus.SORTED                                                  -> SortedStatus.NOT_SORTED
                else                                                                                -> this.sorted
            }

        System.arraycopy(this.array, indexLocal, this.array, indexLocal + 1, this.array.size - indexLocal - 1)

        this.array[indexLocal] = integer
        this.size++
    }

    /**Indicates if the array is empty*/
    val empty
        get() = this.size == 0

    /**
     * Indicates if the array is sorted.
     *
     * If the sorted status is unknown, it takes the time to compute and update the sorted status
     */
    fun sorted() : SortedStatus
    {
        if (this.sorted != SortedStatus.UNKNOWN)
        {
            return this.sorted
        }

        var previous = this.array[0]
        var actual : Int

        for (i in 1 until this.size)
        {
            actual = this.array[i]

            if (previous > actual)
            {
                this.sorted = SortedStatus.NOT_SORTED
                return SortedStatus.NOT_SORTED
            }

            previous = actual
        }

        this.sorted = SortedStatus.SORTED
        return SortedStatus.SORTED
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    override fun iterator() = this.iteratorInt()

    /**
     * remove an integer
     *
     * @param index Index of integer to remove
     */
    fun remove(index : Int)
    {
        this.checkIndex(index)

        System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1)
        this.size--

        if (this.size < 2)
        {
            this.sorted = SortedStatus.SORTED
        }
    }

    /**
     * Change an integer on the array
     *
     * @param index   Index to change
     * @param integer New value
     */
    operator fun set(index : Int, integer : Int)
    {
        this.checkIndex(index)

        this.array[index] = integer

        this.sorted =
            when
            {
                (this.sorted == SortedStatus.SORTED && (index == 0 || integer >= this.array[index - 1])
                 && (index == this.size - 1 || integer <= this.array[index + 1])) -> SortedStatus.SORTED

                this.sorted == SortedStatus.SORTED                                -> SortedStatus.NOT_SORTED
                else                                                              -> SortedStatus.UNKNOWN
            }
    }

    /**
     * Sort the array.
     *
     * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 2, 2, 5, 5, 6, 7, 9]
     */
    fun sort()
    {
        if (this.sorted == SortedStatus.SORTED)
        {
            return
        }

        Arrays.sort(this.array, 0, this.size)
        this.sorted = SortedStatus.SORTED
    }

    /**
     * Sort array in unique mode.
     *
     * That is to say, if two integers are equals, only one is kept.
     *
     * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 5, 6, 7, 9]
     */
    fun sortUniq()
    {
        if (this.size < 2)
        {
            return
        }

        this.sort()
        var actual : Int
        var previous = this.array[this.size - 1]

        for (index in this.size - 2 downTo 0)
        {
            actual = this.array[index]

            if (actual == previous)
            {
                System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1)
                this.size--
            }

            previous = actual
        }
    }

    /**
     * Convert in an int array
     *
     * @return Extracted array
     */
    fun toArray() : IntArray
    {
        val array = IntArray(this.size)
        System.arraycopy(this.array, 0, array, 0, this.size)
        return array
    }

    /**
     * String representation
     *
     * @return String representation
     * @see Object.toString
     */
    override fun toString() : String
    {
        val stringBuilder = StringBuilder("[")

        if (this.size > 0)
        {
            stringBuilder.append(this.array[0])

            for (i in 1 until this.size)
            {
                stringBuilder.append(", ")
                stringBuilder.append(this.array[i])
            }
        }

        stringBuilder.append("] : ${this.sorted}")

        return stringBuilder.toString()
    }
}