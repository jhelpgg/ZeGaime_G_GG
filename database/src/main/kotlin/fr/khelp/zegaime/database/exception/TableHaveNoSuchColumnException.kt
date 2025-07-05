package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table

/**
 * Throws if a column not exists in a table
 */
class TableHaveNoSuchColumnException(table : Table, column : Column) :
    Exception("Table ${table.name} have no such column : $column")