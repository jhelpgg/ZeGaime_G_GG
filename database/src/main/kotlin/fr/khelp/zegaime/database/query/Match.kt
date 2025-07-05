package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.SelectDSL
import fr.khelp.zegaime.database.Table

/**
 * For select column row result based on an other selection
 *
 * See documentation for match DSL syntax
 */
class Match internal constructor()
{
    internal var select : Select? = null

    /**
     * Choose the selection
     */
    @SelectDSL
    fun select(table : Table, selectCreator : Select.() -> Unit)
    {
        val select = Select(table)
        selectCreator(select)
        this.select = select
    }
}