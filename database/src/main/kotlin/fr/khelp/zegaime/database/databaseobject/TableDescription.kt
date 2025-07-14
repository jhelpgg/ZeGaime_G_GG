package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Table

/**
 * Internal class that describes a table for a database object.
 *
 * @property table The table instance.
 * @property primaryKeys The names of the primary key columns.
 * 
 */
internal class TableDescription(val table : Table, val primaryKeys : Array<String>)
