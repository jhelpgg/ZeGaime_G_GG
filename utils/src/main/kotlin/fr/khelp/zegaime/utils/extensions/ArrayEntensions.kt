package fr.khelp.zegaime.utils.extensions

import java.util.TreeSet

/**
 * Merge two array in one contains the union of the both without duplicates
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

inline fun <S, reified D> Array<S>.transformArray(transformation: (S) -> D) =
    Array<D>(this.size) { index -> transformation(this[index]) }

fun <T> Array<T>.transformInt(transformation: (T) -> Int) =
    IntArray(this.size) { index -> transformation(this[index]) }

fun <T> Array<T>.transformLong(transformation: (T) -> Long) =
    LongArray(this.size) { index -> transformation(this[index]) }
