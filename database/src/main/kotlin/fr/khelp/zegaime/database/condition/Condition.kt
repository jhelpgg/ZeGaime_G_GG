package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table

/**
 * Represents a condition in a database query.
 *
 * A condition is composed of one or more columns and an SQL condition string.
 * It is used to filter the results of a query.
 *
 * **Creation example: **
 * This class is not meant to be instantiated directly.
 * Use the extension functions on `Column` to create conditions.
 * ```kotlin
 * val condition = COLUMN_ID EQUALS_ID 1
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val select = Select(table, listOf(COLUMN_NAME), condition)
 * ```
 *
 * @property columns The columns involved in the condition.
 * @property sqlCondition The SQL representation of the condition.
 * @see Where
 * @see Select
 * @see Update
 * @see Delete
 */
class Condition internal constructor(internal val columns : Array<Column>, internal val sqlCondition : String)
{
    /**
     * Checks if all columns in the condition exist in the given table.
     *
     * **Usage example:**
     * ```kotlin
     * val condition = COLUMN_NAME EQUALS "test"
     * condition.checkAllColumns(table) // This will not throw an exception if COLUMN_NAME is in the table
     * ```
     *
     * @param table The table to check against.
     * @throws TableHaveNoSuchColumnException if a column in the condition does not exist in the table.
     */
    fun checkAllColumns(table : Table)
    {
        for (column in this.columns)
        {
            table.checkColumn(column)
        }
    }
}

/**
 * A condition that never matches.
 *
 * It is used to create queries that should not return any results.
 */
val NEVER_MATCH_CONDITION = COLUMN_ID EQUALS_ID -123

