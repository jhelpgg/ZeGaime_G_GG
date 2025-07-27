package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.InsertDSL
import fr.khelp.zegaime.database.Table

/**
 * Represents a list of insert queries.
 *
 * This class allows inserting several rows at one time.
 *
 * **Creation example**
 * This class is not meant to be instantiated directly.
 * Use the `Table.insertList` method.
 *
 * **Standard usage**
 * ```kotlin
 * table.insertList {
 *     add {
 *         COLUMN_NAME IS "John"
 *         COLUMN_AGE IS 30
 *     }
 *     add {
 *         COLUMN_NAME IS "Jane"
 *         COLUMN_AGE IS 25
 *     }
 * }
 * ```
 *
 * @property table The table to insert into.
 * @constructor Creates a new insert list query. For internal use only.
 */
class InsertList internal constructor(val table : Table)
{
    /**
     * Adds a row to the insert list.
     *
     * **Usage example**
     * ```kotlin
     * add {
     *     COLUMN_NAME IS "John"
     *     COLUMN_AGE IS 30
     * }
     * ```
     *
     * @param insertCreator A lambda function to define the row to insert.
     */
    @InsertDSL
    fun add(insertCreator : Insert.() -> Unit)
    {
        this.table.insert(insertCreator)
    }
}