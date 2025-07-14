package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition

/**
 * Represents a delete query.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `Table.delete` method.
 *
 * **Standard usage:**
 * ```kotlin
 * table.delete {
 *     where { condition = COLUMN_AGE LESS_THAN 18 }
 * }
 * ```
 *
 * @property table The table to delete from.
 */
class Delete internal constructor(val table : Table)
{
    private var condition : Condition? = null

    /**
     * Specifies the delete condition.
     *
     * **Usage example:**
     * ```kotlin
     * where { condition = COLUMN_AGE LESS_THAN 18 }
     * ```
     *
     * @param whereCreator A lambda function to define the where clause.
     */
    @WhereDSL
    fun where(whereCreator : Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        this.condition = condition
    }

    /**
     * Returns the SQL representation of the delete query.
     * 
     */
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
