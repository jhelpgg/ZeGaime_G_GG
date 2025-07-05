package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.query.DataRow
import fr.khelp.zegaime.database.query.Select
import fr.khelp.zegaime.utils.elementExistsCheck
import java.sql.ResultSet
import java.sql.Statement

/**
 * Selected row result from [Table.select]
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

    /**Column index in the answer*/
    fun columnIndex(columnName : String) = this.select.columnIndex(columnName)

    /**Column index in the answer*/
    fun columnIndex(column : Column) = this.select.columnIndex(column)

    /**Column name at index in the answer*/
    fun column(index : Int) = this.select[index]

    /**
     * Read next row from result
     * See documentation for more explanation about read row result DSL syntax
     * @throws NoSuchElementException if result have no more result
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
     * Close the result properly and free link to database
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