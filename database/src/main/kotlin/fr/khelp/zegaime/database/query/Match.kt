package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.SelectDSL
import fr.khelp.zegaime.database.Table

/**
 * Represents a subquery for a `IN` condition.
 *
 * **Creation example**
 * This class is not meant to be instantiated directly.
 * It is used in the `Column.IN` method.
 *
 * **Standard usage**
 * ```kotlin
 * val condition = COLUMN_ID IN {
 *     select(otherTable) {
 *         +COLUMN_ID
 *         where { COLUMN_NAME EQUALS "test" }
 *     }
 * }
 * ```
 *
 * @constructor Creates a new match query. For internal use only.
 */
class Match internal constructor()
{
    /**
     * The select query of the subquery.
     */
    internal var select : Select? = null

    /**
     * Specifies the select query for the subquery.
     *
     * **Usage example**
     * ```kotlin
     * select(otherTable) {
     *     +COLUMN_ID
     *     where { COLUMN_NAME EQUALS "test" }
     * }
     * ```
     *
     * @param table The table to select from.
     * @param selectCreator A lambda function to define the select query.
     */
    @SelectDSL
    fun select(table : Table, selectCreator : Select.() -> Unit)
    {
        val select = Select(table)
        selectCreator(select)
        this.select = select
    }
}