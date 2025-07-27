package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.query.DataRow
import fr.khelp.zegaime.database.query.Select
import fr.khelp.zegaime.utils.elementExistsCheck
import java.sql.ResultSet
import java.sql.Statement

/**
 * Represents the result of a select query.
 *
 * This class is an iterator over the selected rows.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is returned by the `Table.select` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val result = table.select {
 *     +COLUMN_NAME
 *     +COLUMN_AGE
 *     where { COLUMN_AGE GREATER_THAN 18 }
 * }
 * while (result.hasNext) {
 *     result.next {
 *         val name = getString(COLUMN_NAME)
 *         val age = getInt(COLUMN_AGE)
 *         // ...
 *     }
 * }
 * result.close()
 * ```
 *
 * @property table The table from which the result was obtained.
 * @property closed Indicates if the result set is closed.
 * @property hasNext Indicates if there are more rows in the result set.
 * @property numberOfColumns The number of columns in the result.
 */
class DataRowResult internal constructor(private val statement : Statement,
                                         private val resultSet : ResultSet,
                                         private val select : Select,
                                         val table : Table)
{
    /**Indicates if result is finished*/
    var closed = false
        private set
    val hasNext get() = !this.closed

    /**Number of columns in the answer*/
    val numberOfColumns = this.select.numberColumns

    init
    {
        if (!this.resultSet.next())
        {
            this.close()
        }
    }

    /**
     * Returns the index of the column with the given name.
     *
     * **Usage example**
     * ```kotlin
     * val nameIndex = result.columnIndex("name")
     * ```
     *
     * @param columnName The name of the column.
     * @return The index of the column.
     */
    fun columnIndex(columnName : String) = this.select.columnIndex(columnName)

    /**
     * Returns the index of the given column.
     *
     * **Usage example**
     * ```kotlin
     * val nameIndex = result.columnIndex(COLUMN_NAME)
     * ```
     *
     * @param column The column.
     * @return The index of the column.
     */
    fun columnIndex(column : Column) = this.select.columnIndex(column)

    /**
     * Returns the column at the given index.
     *
     * **Usage example**
     * ```kotlin
     * val column = result.column(0)
     * ```
     *
     * @param index The index of the column.
     * @return The column at the given index.
     */
    fun column(index : Int) = this.select[index]

    /**
     * Reads the next row from the result.
     *
     * See the documentation for more explanation about the read row result DSL syntax.
     *
     * **Usage example**
     * ```kotlin
     * result.next {
     *     val name = getString(COLUMN_NAME)
     *     val age = getInt(COLUMN_AGE)
     *     // ...
     * }
     * ```
     *
     * @param dataRowReader A lambda function to read the data from the row.
     * @throws NoSuchElementException if the result has no more rows.
     */
    @Throws(NoSuchElementException::class)
    @RowResultDSL
    fun next(dataRowReader : DataRow.() -> Unit)
    {
        elementExistsCheck(!this.closed) { "No more data to read" }
        val dataRow = DataRow(this.resultSet, this.select, this.table)
        dataRowReader(dataRow)

        if (!this.resultSet.next())
        {
            this.close()
        }
    }

    /**
     * Closes the result properly and frees the link to the database.
     *
     * **Usage example**
     * ```kotlin
     * result.close()
     * ```
     */
    fun close()
    {
        if (!this.closed)
        {
            this.closed = true
            this.statement.close()
            this.resultSet.close()
        }
    }
}
