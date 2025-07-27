package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Table

/**
 * Internal class that describes a table for a database object.
 *
 * This class is not meant to be used directly.
 * It is used by the [DataObjectManager] to store the table information.
 *
 * @property table The table instance.
 * @property primaryKeys The names of the primary key columns.
 * @constructor Creates a new table description.
 */
internal class TableDescription(val table : Table, val primaryKeys : Array<String>)