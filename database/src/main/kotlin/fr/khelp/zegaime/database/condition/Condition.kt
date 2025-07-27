package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.query.Delete
import fr.khelp.zegaime.database.query.Select
import fr.khelp.zegaime.database.query.Update
import fr.khelp.zegaime.database.query.Where

/**
 * Represents a condition in a database query.
 *
 * A condition is used to filter the results of a query in a `WHERE` clause.
 * It is composed of one or more columns and an SQL condition string.
 *
 * **Creation example: **
 * This class is not meant to be instantiated directly.
 * Use the extension functions on `Column` to create conditions.
 * ```kotlin
 * // Create a condition to select users with ID 1
 * val condition = COLUMN_ID EQUALS_ID 1
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * // Use the condition in a SELECT query
 * val select = Select(table, listOf(COLUMN_NAME), condition)
 * ```
 *
 * @property columns The columns involved in the condition. For internal use.
 * @property sqlCondition The SQL representation of the condition. For internal use.
 * @see Where
 * @see Select
 * @see Update
 * @see Delete
 */
class Condition internal constructor(internal val columns: Array<Column>, internal val sqlCondition: String)
{
    /**
     * Checks if all columns in the condition exist in the given table.
     *
     * **Usage example:**
     * ```kotlin
     * val condition = COLUMN_NAME EQUALS "test"
     * try {
     *     condition.checkAllColumns(table) // This will not throw an exception if COLUMN_NAME is in the table
     * } catch (e: TableHaveNoSuchColumnException) {
     *     // Handle the exception
     *     println(e.message)
     * }
     * ```
     *
     * @param table The table to check against.
     * @throws fr.khelp.zegaime.database.exception.TableHaveNoSuchColumnException if a column in the condition does not exist in the table.
     */
    fun checkAllColumns(table: Table)
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
 * It is used to create queries that should not return any results, for example, to delete all rows in a table.
 *
 * ```kotlin
 * // This will delete all rows from the table
 * val delete = Delete(table, ALL)
 * // This will not delete any row
 * val delete = Delete(table, NEVER_MATCH_CONDITION)
 * ```
 */
val NEVER_MATCH_CONDITION = COLUMN_ID EQUALS_ID -123