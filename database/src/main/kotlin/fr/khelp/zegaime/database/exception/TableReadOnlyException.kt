package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Table

/**
 * Exception thrown when trying to modify a read-only table.
 *
 * This exception is thrown when trying to insert, update or delete a row in a read-only table.
 *
 * **Creation example:**
 * ```kotlin
 * throw TableReadOnlyException(table)
 * ```
 *
 * @param table The read-only table.
 * @constructor Creates a new table read only exception.
 */
class TableReadOnlyException(table: Table) : Exception("The table ${table.name} is read only")