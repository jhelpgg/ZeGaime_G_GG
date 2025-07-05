package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition

/**
 * For delete rows in table
 *
 * See documentation for delete DSL syntax
 */
class Delete internal constructor(val table : Table)
{
    private var condition : Condition? = null

    /**
     * Specify delete condition
     */
    @WhereDSL
    fun where(whereCreator : Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        this.condition = condition
    }

    internal fun deleteSQL() : String
    {
        val query = StringBuilder()
        query.append("DELETE FROM ")
        query.append(this.table.name)

        this.condition?.let { where ->
            query.append(" WHERE ")
            query.append(where.sqlCondition)
        }

        return query.toString()
    }
}