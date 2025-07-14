package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table

/**
 * Exception thrown when a column does not exist in a table.
 *
 * **Creation example:**
 * ```kotlin
 * throw TableHaveNoSuchColumnException(table, column)
 * ```
 *
 * @param table The table.
 * @param column The column that does not exist.
 */
class TableHaveNoSuchColumnException(table : Table, column : Column) :
    Exception("Table ${table.name} have no such column : $column")
