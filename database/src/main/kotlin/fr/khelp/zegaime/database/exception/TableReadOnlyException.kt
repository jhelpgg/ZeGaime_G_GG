package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Table

/**
 * Exception thrown when trying to modify a read-only table.
 *
 * **Creation example:**
 * ```kotlin
 * throw TableReadOnlyException(table)
 * ```
 *
 * @param table The read-only table.
 */
class TableReadOnlyException(table : Table) : Exception("The table${table.name} is read only")
