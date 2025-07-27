package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.regex.RegularExpression
import java.util.regex.Pattern

/**
 * Creates a condition that checks if the column's value matches the given regular expression pattern.
 *
 * This method fetches all the values from the specified column, applies the regex pattern,
 * and then creates a condition to select the rows with matching values.
 *
 * The column type must be `DataType.STRING`.
 *
 * **Usage example**
 * ```kotlin
 * val pattern = Pattern.compile("test.*")
 * val condition = COLUMN_NAME.regex(table, pattern)
 * ```
 *
 * @param table The table to select the values from.
 * @param pattern The regex pattern to match.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
fun Column.regex(table : Table, pattern : Pattern) : Condition
{
    table.checkColumn(this)
    this.checkType(DataType.STRING)
    val identifiers = ArrayList<Int>()
    val result = table.select {
        +COLUMN_ID
        +this@regex
    }

    while (result.hasNext)
    {
        result.next {
            if (pattern.matcher(getString(2))
                    .matches())
            {
                identifiers += getID(1)
            }
        }
    }

    result.close()
    val ids = IntArray(identifiers.size) { index -> identifiers[index] }
    return COLUMN_ID ONE_OF_ID ids
}

/**
 * Creates a condition that checks if the column's value matches the given regular expression.
 *
 * This method fetches all the values from the specified column, applies the regex,
 * and then creates a condition to select the rows with matching values.
 *
 * The column type must be `DataType.STRING`.
 *
 * **Usage example**
 * ```kotlin
 * val regex = "test".regularExpression + ANY.zeroOrMore
 * val condition = COLUMN_NAME.regex(table, regex)
 * ```
 *
 * @param table The table to select the values from.
 * @param regularExpression The regular expression to match.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
fun Column.regex(table : Table, regularExpression : RegularExpression) : Condition
{
    table.checkColumn(this)
    this.checkType(DataType.STRING)
    val identifiers = ArrayList<Int>()
    val result = table.select {
        +COLUMN_ID
        +this@regex
    }

    while (result.hasNext)
    {
        result.next {
            if (regularExpression.matches(getString(2)))
            {
                identifiers += getID(1)
            }
        }
    }

    result.close()
    val ids = IntArray(identifiers.size) { index -> identifiers[index] }
    return COLUMN_ID ONE_OF_ID ids
}