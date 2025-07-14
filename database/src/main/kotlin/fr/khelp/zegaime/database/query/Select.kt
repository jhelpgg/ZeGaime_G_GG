package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.SelectDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition

/**
 * Represents a select query.
 *
 * If no columns are selected, it selects all columns in the table in their declaration order.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `Table.select` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val result = table.select {
 *     +COLUMN_NAME
 *     +COLUMN_AGE
 *     where { condition = COLUMN_AGE GREATER_THAN 18 }
 *     descendant(COLUMN_AGE)
 * }
 * ```
 *
 * @property table The table to select from.
 * @property numberColumns The number of columns in the selection.
 */
class Select internal constructor(val table : Table) : Iterable<Column>
{
    private val columns = ArrayList<Column>()
    private var condition : Condition? = null
    private var columnOrder : Column? = null
    private var ascendant = true

    val numberColumns
        get() =
            if (this.columns.isEmpty())
            {
                this.table.numberColumns
            }
            else
            {
                this.columns.size
            }

    /**
     * Returns the column at the given index.
     *
     * @param index The index of the column.
     * @return The column at the given index.
     */
    operator fun get(index : Int) =
        if (this.columns.isEmpty())
        {
            this.table[index]
        }
        else
        {
            this.columns[index]
        }

    /**
     * Returns an iterator over the selected columns.
     */
    override fun iterator() : Iterator<Column> =
        if (this.columns.isEmpty())
        {
            this.table.iterator()
        }
        else
        {
            this.columns.iterator()
        }

    /**
     * Returns the column with the given name.
     *
     * @param name The name of the column.
     * @return The column with the given name, or `null` if it does not exist.
     */
    fun column(name : String) : Column? =
        if (this.columns.isEmpty())
        {
            this.table.obtainColumn(name)
        }
        else
        {
            this.columns.firstOrNull { column -> name.equals(column.name, true) }
        }

    /**
     * Returns the index of the given column.
     *
     * @param column The column.
     * @return The index of the column, or -1 if it is not in the selection.
     */
    fun columnIndex(column : Column) : Int =
        if (this.columns.isEmpty())
        {
            this.table.indexOf(column)
        }
        else
        {
            this.columns.indexOf(column)
        }

    /**
     * Returns the index of the column with the given name.
     *
     * @param name The name of the column.
     * @return The index of the column, or -1 if it is not in the selection.
     */
    fun columnIndex(name : String) : Int
    {
        val column = column(name) ?: return -1
        return columnIndex(column)
    }

    /**
     * Selects a column to be present in the result.
     *
     * **Usage example:**
     * ```kotlin
     * +COLUMN_NAME
     * ```
     */
    @SelectDSL
    operator fun Column.unaryPlus()
    {
        this.name.unaryPlus()
    }

    /**
     * Selects a column to be present in the result.
     *
     * **Usage example:**
     * ```kotlin
     * +"name"
     * ```
     */
    @SelectDSL
    operator fun String.unaryPlus()
    {
        val column = this@Select.table.obtainColumn(this)
                     ?: throw IllegalArgumentException("No column $this in table${this@Select.table.name}")

        if (column !in this@Select.columns)
        {
            this@Select.columns += column
        }
    }

    /**
     * Specifies the condition that the selected rows must match.
     *
     * **Usage example:**
     * ```kotlin
     * where { condition = COLUMN_AGE GREATER_THAN 18 }
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
        condition.checkAllColumns(this.table)
        this.condition = condition
    }

    /**
     * If specified, it will sort the result on the given column in ascending order.
     *
     * **Usage example:**
     * ```kotlin
     * ascendant(COLUMN_AGE)
     * ```
     *
     * @param column The column to sort by.
     */
    @SelectDSL
    fun ascendant(column : Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = true
    }

    /**
     * If specified, it will sort the result on the given column in ascending order.
     *
     * **Usage example:**
     * ```kotlin
     * ascendant("age")
     * ```
     *
     * @param columnName The name of the column to sort by.
     */
    @SelectDSL
    fun ascendant(columnName : String)
    {
        this.ascendant(this.table.getColumn(columnName))
    }

    /**
     * If specified, it will sort the result on the given column in descending order.
     *
     * **Usage example:**
     * ```kotlin
     * descendant(COLUMN_AGE)
     * ```
     *
     * @param column The column to sort by.
     */
    @SelectDSL
    fun descendant(column : Column)
    {
        this.table.checkColumn(column)
        this.columnOrder = column
        this.ascendant = false
    }

    /**
     * If specified, it will sort the result on the given column in descending order.
     *
     * **Usage example:**
     * ```kotlin
     * descendant("age")
     * ```
     *
     * @param columnName The name of the column to sort by.
     */
    @SelectDSL
    fun descendant(columnName : String)
    {
        this.descendant(this.table.getColumn(columnName))
    }

    /**
     * Returns the SQL representation of the select query.
     * 
     */
    internal fun selectSQL() : String
    {
        val query = StringBuilder()
        query.append("SELECT ")
        query.append(this[0].name)

        for (index in 1 until this.numberColumns)
        {
            query.append(", ")
            query.append(this[index].name)
        }

        query.append(" FROM ")
        query.append(this.table.name)

        this.condition?.let { condition ->
            query.append(" WHERE ")
            query.append(condition.sqlCondition)
        }

        this.columnOrder?.let { column ->
            query.append(" ORDER BY ")
            query.append(column.name)

            if (this.ascendant)
            {
                query.append(" ASC")
            }
            else
            {
                query.append(" DESC")
            }
        }

        return query.toString()
    }
}
