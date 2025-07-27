package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataType

/**
 * Creates a condition that checks if the column's value matches the given pattern using the `LIKE` operator.
 *
 * The column type must be `DataType.STRING`.
 *
 * The pattern can contain wildcards:
 * - `%` matches any sequence of characters.
 * - `_` matches any single character.
 *
 * **Usage example**
 * ```kotlin
 * // Matches any name starting with "test"
 * val condition = COLUMN_NAME LIKE "test%"
 * ```
 *
 * @param pattern The pattern to match.
 * @return A new condition.
 * @throws fr.khelp.zegaime.database.exception.InvalidDataTypeException if the column type is not `DataType.STRING`.
 */
infix fun Column.LIKE(pattern : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name} LIKE '$pattern'")
}