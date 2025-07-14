package fr.khelp.zegaime.database

import java.io.PrintStream
import kotlin.math.max

/**
 * Creates a string representation of a row.
 *
 * @param row The row to represent.
 * @param maxWidths The maximum widths of the columns.
 * @return The string representation of the row.
 */
private fun rowString(row: Array<String>, maxWidths: IntArray): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append("|")
    row.indices.forEach { index ->
        val width = maxWidths[index]
        val value = row[index]
        val length = value.length
        val left = (width - length) / 2
        val right = width - length - left
        stringBuilder.append(" ")
        (0 until left).forEach { _ -> stringBuilder.append(" ") }
        stringBuilder.append(value)
        (0 until right).forEach { _ -> stringBuilder.append(" ") }
        stringBuilder.append(" |")
    }
    return stringBuilder.toString()
}

/**
 * Prints a row result in ASCII table format.
 *
 * The result is printed from its current line to the end.
 * The result is consumed at the end of this method.
 *
 * **Usage example:**
 * ```kotlin
 * val result = table.select { ... }
 * printDataRowResult(result, System.out)
 * ```
 *
 * @param dataRowResult The row result to print.
 * @param printStream The print stream to use.
 */
fun printDataRowResult(dataRowResult: DataRowResult, printStream: PrintStream)
{
    // Collect data and compute cells' width
    val numberColumn = dataRowResult.numberOfColumns
    val maxWidths = IntArray(numberColumn)
    val columns = Array(numberColumn) { index ->
        val column = dataRowResult.column(index).name
        maxWidths[index] = column.length
        column
    }

    val data = ArrayList<Array<String>>()

    while (dataRowResult.hasNext)
    {
        dataRowResult.next {
            data += Array<String>(numberColumn) { index ->
                val value = toString(index + 1)
                maxWidths[index] = max(maxWidths[index], value.length)
                value
            }
        }
    }

    dataRowResult.close()

    val table = dataRowResult.table.name
    val titleLength = 2 + table.length + 2
    var maxWidthIndex = 0
    var separatorLength = 2 + maxWidths.sum() + 3 * (numberColumn - 1) + 2

    while (separatorLength < titleLength)
    {
        separatorLength++
        maxWidths[maxWidthIndex]++
        maxWidthIndex = (maxWidthIndex + 1) % maxWidths.size
    }

    // Print result
    val separatorCharacters = CharArray(separatorLength)
    separatorCharacters[0] = '+'
    var index = 1
    maxWidths.forEach { width ->
        separatorCharacters[index++] = '-'
        (0 until width).forEach { separatorCharacters[index++] = '-' }
        separatorCharacters[index++] = '-'
        separatorCharacters[index++] = '+'
    }
    val separator = String(separatorCharacters, 0, index)
    val space = max(0, (separator.length - table.length) / 2 - 1)
    val left = max(0, separator.length - table.length - space - 2)

    printStream.print('+')
    printStream.print(String(CharArray(separator.length - 2) { '-' }))
    printStream.println('+')

    printStream.print('|')
    printStream.print(String(CharArray(space) { ' ' }))
    printStream.print(table)
    printStream.print(String(CharArray(left) { ' ' }))
    printStream.println('|')

    printStream.println(separator)
    printStream.println(rowString(columns, maxWidths))
    printStream.println(separator)

    for (row in data)
    {
        printStream.println(rowString(row, maxWidths))
    }

    printStream.println(separator)
}
