package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Table

/**
 * Throws if tries to modifiy a read only table
 */
class TableReadOnlyException(table : Table) : Exception("The table${table.name} is read only")
