package fr.khelp.zegaime.utils.extensions

import java.util.TreeSet

/**
 * Merge two array in one contains the union of the both without duplicates
 * @param array Array to merge with
 * @return Merged array
 */
inline fun <reified T> Array<T>.merge(array: Array<T>): Array<T>
{
    val treeSet = TreeSet<T>()

    for (element in this)
    {
        treeSet.add(element)
    }

    for (element in array)
    {
        treeSet.add(element)
    }

    return treeSet.toTypedArray()
}

/**
 * String representation with customizable header, separator and footer
 * @param header String before the content
 * @param separator String between elements
 * @param footer String after the content
 * @return The string representation
 */
fun <T> Array<T>.string(header: String = "[", separator: String = ", ", footer: String = "]"): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append(header)

    if (this.isNotEmpty())
    {
        stringBuilder.append(this[0])

        for (index in 1 until this.size)
        {
            stringBuilder.append(separator)
            stringBuilder.append(this[index])
        }
    }

    stringBuilder.append(footer)
    return stringBuilder.toString()
}

/**
 * Indicates if two arrays have the same content
 * @param other Array to compare with
 * @return `true` if the two arrays have the same content
 */
fun <T> Array<T>.same(other: Array<T>): Boolean
{
    val size = this.size

    if (size != other.size)
    {
        return false
    }

    for (index in 0 until size)
    {
        if (this[index] != other[index])
        {
            return false
        }
    }

    return true
}

/**
 * Transform an array of a type to an array of another type
 * @param transformation Transformation to apply to each element
 * @return The transformed array
 */
inline fun <S, reified D> Array<S>.transformArray(transformation: (S) -> D) =
    Array<D>(this.size) { index -> transformation(this[index]) }

/**
 * Transform an array of a type to an array of integers
 * @param transformation Transformation to apply to each element
 * @return The transformed array
 */
fun <T> Array<T>.transformInt(transformation: (T) -> Int) =
    IntArray(this.size) { index -> transformation(this[index]) }

/**
 * Transform an array of a type to an array of longs
 * @param transformation Transformation to apply to each element
 * @return The transformed array
 */
fun <T> Array<T>.transformLong(transformation: (T) -> Long) =
    LongArray(this.size) { index -> transformation(this[index]) }
