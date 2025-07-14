package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.exception.TableHaveNoSuchColumnException
import fr.khelp.zegaime.database.exception.TableReadOnlyException
import fr.khelp.zegaime.database.extensions.validName
import fr.khelp.zegaime.database.query.Delete
import fr.khelp.zegaime.database.query.Insert
import fr.khelp.zegaime.database.query.InsertList
import fr.khelp.zegaime.database.query.Select
import fr.khelp.zegaime.database.query.Update
import fr.khelp.zegaime.database.query.Where
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.collections.iterations.transform

/**
 * Represents a table in the database.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `Database.table` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val table = database.table("myTable") {
 *     "name" AS DataType.STRING
 *     "age" AS DataType.INTEGER
 * }
 * ```
 *
 * @property name The name of the table.
 * @property readOnly Indicates if the table is read-only.
 * @property numberColumns The number of columns in the table.
 */
class Table internal constructor(val name: String, val readOnly: Boolean, private val database: Database) :
    Iterable<Column>
{
    private val columns = ArrayList<Column>()
    val numberColumns get() = this.columns.size
    private val columnID = Column("ID", DataType.ID)

    init
    {
        argumentCheck(this.name.validName()) { "Invalid table name : ${this.name}" }
        this.columns += this.columnID
    }

    /**
     * Checks if a column exists in the table.
     *
     * **Usage example:**
     * ```kotlin
     * table.checkColumn(COLUMN_NAME)
     * ```
     *
     * @param column The column to check.
     * @throws TableHaveNoSuchColumnException If the column does not exist in the table.
     */
    @Throws(TableHaveNoSuchColumnException::class)
    fun checkColumn(column: Column)
    {
        if (column !in this)
        {
            throw TableHaveNoSuchColumnException(this, column)
        }
    }

    /**
     * Checks if the table is read-only.
     *
     * **Usage example:**
     * ```kotlin
     * table.checkReadOnly()
     * ```
     *
     * @throws TableReadOnlyException If the table is read-only.
     */
    @Throws(TableReadOnlyException::class)
    fun checkReadOnly()
    {
        if (this.readOnly)
        {
            throw TableReadOnlyException(this)
        }
    }

    /**
     * Returns the column at the given index.
     *
     * **Usage example:**
     * ```kotlin
     * val column = table[0]
     * ```
     *
     * @param index The index of the column.
     * @return The column at the given index.
     */
    operator fun get(index: Int) =
        this.columns[index]

    /**
     * Returns the column with the given name.
     *
     * **Usage example:**
     * ```kotlin
     * val column = table.getColumn("name")
     * ```
     *
     * @param nameSearched The name of the column.
     * @return The column with the given name.
     * @throws NoSuchElementException If the column does not exist.
     */
    @Throws(NoSuchElementException::class)
    fun getColumn(nameSearched: String) =
        this.columns.first { column -> nameSearched.equals(column.name, true) }

    /**
     * Returns the column with the given name.
     *
     * **Usage example:**
     * ```kotlin
     * val column = table.obtainColumn("name")
     * ```
     *
     * @param nameSearched The name of the column.
     * @return The column with the given name, or `null` if it does not exist.
     */
    fun obtainColumn(nameSearched: String) =
        this.columns.firstOrNull { column -> nameSearched.equals(column.name, true) }

    /**
     * Checks if the table contains the given column.
     *
     * **Usage example:**
     * ```kotlin
     * if (COLUMN_NAME in table) {
     *     // ...
     * }
     * ```
     *
     * @param column The column to check.
     * @return `true` if the table contains the column, `false` otherwise.
     */
    operator fun contains(column: Column) =
        column in this.columns

    /**
     * Adds a column to the table.
     *
     * This method is used in the table creation DSL.
     *
     * **Usage example:**
     * ```kotlin
     * "name" AS DataType.STRING
     * ```
     *
     * @param dataType The data type of the column.
     */
    @CreateTableDSL
    infix fun String.AS(dataType: DataType)
    {
        argumentCheck(this.validName()) { "Invalid column name : $this" }
        argumentCheck(this@Table.obtainColumn(this) == null) { "A column named $this already exists in table ${this@Table.name}" }
        argumentCheck(dataType != DataType.ID) { "The primary key was already automatically defined" }
        this@Table.columns += Column(this, dataType)
    }

    /**
     * Defines a foreign key for the ID column.
     *
     * **Usage example:**
     * ```kotlin
     * idForeign(otherTable, "other_id")
     * ```
     *
     * @param table The foreign table.
     * @param column The name of the foreign column.
     */
    @CreateTableDSL
    fun idForeign(table: Table, column: String)
    {
        this.idForeign(table, table.getColumn(column))
    }

    /**
     * Defines a foreign key for the ID column.
     *
     * **Usage example:**
     * ```kotlin
     * idForeign(otherTable, COLUMN_OTHER_ID)
     * ```
     *
     * @param table The foreign table.
     * @param column The foreign column.
     */
    @CreateTableDSL
    fun idForeign(table: Table, column: Column)
    {
        column.checkType(DataType.INTEGER)
        table.checkColumn(column)
        this.columnID.foreignTable = table.name
        this.columnID.foreignColumn = column.name
    }

    /**
     * Defines a foreign key for a column.
     *
     * **Usage example:**
     * ```kotlin
     * "other_id" FOREIGN otherTable
     * ```
     *
     * @param table The foreign table.
     */
    @CreateTableDSL
    infix fun String.FOREIGN(table: Table)
    {
        argumentCheck(this.validName()) { "Invalid column name : $name" }
        argumentCheck(this@Table.obtainColumn(this) == null) { "A column named $this already exists in table ${this@Table.name}" }
        val columnCreated = Column(this, DataType.INTEGER)
        columnCreated.foreignTable = table.name
        columnCreated.foreignColumn = "ID"
        this@Table.columns += columnCreated
    }

    /**
     * Returns an iterator over the columns of the table.
     */
    override fun iterator(): Iterator<Column> =
        this.columns.iterator()

    /**
     * Returns a list of the column names.
     */
    fun columnNames() =
        this.columns.transform { column -> column.name }

    /**
     * Selects rows from the table.
     *
     * See the documentation for more explanation about the select DSL syntax.
     *
     * **Usage example:**
     * ```kotlin
     * val result = table.select {
     *     +COLUMN_NAME
     *     where { condition = COLUMN_AGE GREATER_THAN 18 }
     * }
     * ```
     *
     * @param selectCreator A lambda function to define the select query.
     * @return The result of the query.
     */
    @SelectDSL
    fun select(selectCreator: Select.() -> Unit): DataRowResult
    {
        val select = Select(this)
        selectCreator(select)
        return this.database.select(select)
    }

    /**
     * Inserts a row into the table.
     *
     * See the documentation for more explanation about the insert DSL syntax.
     *
     * **Usage example:**
     * ```kotlin
     * val id = table.insert {
     *     COLUMN_NAME IS "John"
     *     COLUMN_AGE IS 30
     * }
     * ```
     *
     * @param insertCreator A lambda function to define the insert query.
     * @return The ID of the inserted row.
     */
    @InsertDSL
    fun insert(insertCreator: Insert.() -> Unit): Int
    {
        this.checkReadOnly()
        val insert = Insert(this)
        insertCreator(insert)
        return this.database.insert(insert)
    }

    /**
     * Inserts several rows in one time.
     *
     * See the documentation for more explanation about the insert list DSL syntax.
     *
     * **Usage example:**
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
     * @param insertListCreator A lambda function to define the insert list.
     */
    @InsertDSL
    fun insertList(insertListCreator: InsertList.() -> Unit)
    {
        insertListCreator(InsertList(this))
    }

    /**
     * Updates rows in the table.
     *
     * See the documentation for more explanation about the update DSL syntax.
     *
     * **Usage example:**
     * ```kotlin
     * val updatedRows = table.update {
     *     COLUMN_AGE IS 31
     *     where { condition = COLUMN_NAME EQUALS "John" }
     * }
     * ```
     *
     * @param updateCreator A lambda function to define the update query.
     * @return The number of updated rows.
     */
    @UpdateDSL
    fun update(updateCreator: Update.() -> Unit): Int
    {
        this.checkReadOnly()
        val update = Update(this)
        updateCreator(update)
        return this.database.update(update)
    }

    /**
     * Deletes rows from the table.
     *
     * See the documentation for more explanation about the delete DSL syntax.
     *
     * **Usage example:**
     * ```kotlin
     * val deletedRows = table.delete {
     *     where { condition = COLUMN_AGE LESS_THAN 18 }
     * }
     * ```
     *
     * @param deleteCreator A lambda function to define the delete query.
     * @return The number of deleted rows.
     */
    @DeleteSL
    fun delete(deleteCreator: Delete.() -> Unit): Int
    {
        this.checkReadOnly()
        val delete = Delete(this)
        deleteCreator(delete)
        return this.database.delete(delete)
    }

    /**
     * Obtains the ID of a row that matches a condition.
     *
     * If no row matches, [ROW_NOT_EXISTS] is returned.
     * If at least 2 rows match, [ROW_NOT_UNIQUE] is returned.
     *
     * **Usage example:**
     * ```kotlin
     * val id = table.rowID {
     *     condition = COLUMN_NAME EQUALS "John"
     * }
     * ```
     *
     * @param whereCreator A lambda function to define the where clause.
     * @return The row ID, or [ROW_NOT_EXISTS] or [ROW_NOT_UNIQUE].
     */
    @WhereDSL
    fun rowID(whereCreator: Where.() -> Unit): Int
    {
        val result = this.select {
            +COLUMN_ID
            where(whereCreator)
        }

        if (!result.hasNext)
        {
            result.close()
            return ROW_NOT_EXISTS
        }

        var id: Int = -1
        result.next {
            id = this.getID(1)
        }

        if (result.hasNext)
        {
            result.close()
            return ROW_NOT_UNIQUE
        }

        result.close()
        return id
    }

    /**
     * Appends a column at the end of the table.
     *
     * For enumerations, use the other [appendColumn] method.
     *
     * **Usage example:**
     * ```kotlin
     * table.appendColumn("email", DataType.STRING)
     * ```
     *
     * @param name The name of the column.
     * @param type The data type of the column.
     */
    fun appendColumn(name: String, type: DataType)
    {
        this.checkReadOnly()
        argumentCheck(name.validName()) { "Invalid column name : $name" }
        argumentCheck(this.obtainColumn(name) == null) { "A column named $name already exists in table ${this.name}" }
        argumentCheck(type != DataType.ID) { "The primary key was already automatically defined" }
        argumentCheck(type != DataType.ENUM) { "Type enum can't add by this method, use `appendColumn(name, Enum)`" }

        this.columns += Column(name, type)
        this.database.addToTable(this, name, type, type.defaultValueSerialized)
    }

    /**
     * Appends a column of enumeration type.
     *
     * **Usage example:**
     * ```kotlin
     * table.appendColumn("status", MyEnum.A)
     * ```
     *
     * @param name The name of the column.
     * @param defaultValue The default value for the column.
     */
    fun <E : Enum<E>> appendColumn(name: String, defaultValue: E)
    {
        this.checkReadOnly()
        argumentCheck(name.validName()) { "Invalid column name : $name" }
        argumentCheck(this.obtainColumn(name) == null) { "A column named $name already exists in table ${this.name}" }

        this.columns += Column(name, DataType.ENUM)
        this.database.addToTable(this, name, DataType.ENUM, "'${defaultValue.javaClass.name}:${defaultValue.name}'")
    }

    /**
     * Inserts a column before another column.
     *
     * For enumerations, use the other [insertColumn] method.
     *
     * **Usage example:**
     * ```kotlin
     * table.insertColumn("email", DataType.STRING, COLUMN_AGE)
     * ```
     *
     * @param name The name of the new column.
     * @param type The data type of the new column.
     * @param before The column before which to insert the new column.
     */
    fun insertColumn(name: String, type: DataType, before: Column)
    {
        this.checkReadOnly()
        this.checkColumn(before)

        argumentCheck(name.validName()) { "Invalid column name : $name" }
        argumentCheck(this.obtainColumn(name) == null) { "A column named $name already exists in table ${this.name}" }
        argumentCheck(type != DataType.ID) { "The primary key was already automatically defined" }
        argumentCheck(type != DataType.ENUM) { "Type enum can't add by this method, use `insertColumn(name, Enum, Column)` or `insertColumn(name, Enum, String)`" }
        argumentCheck(before.type != DataType.ID) { "Can't add column before the identifier since identifier must always be the first column" }

        val index = this.columns.indexOfFirst { col -> col.name == before.name }
        this.columns.add(index, Column(name, type))
        this.database.addToTable(this, name, type, type.defaultValueSerialized, before.name)
    }

    /**
     * Inserts a column of enumeration type before another column.
     *
     * **Usage example:**
     * ```kotlin
     * table.insertColumn("status", MyEnum.A, COLUMN_AGE)
     * ```
     *
     * @param name The name of the new column.
     * @param defaultValue The default value for the new column.
     * @param before The column before which to insert the new column.
     */
    fun <E : Enum<E>> insertColumn(name: String, defaultValue: E, before: Column)
    {
        this.checkReadOnly()
        this.checkColumn(before)
        argumentCheck(name.validName()) { "Invalid column name : $name" }
        argumentCheck(this.obtainColumn(name) == null) { "A column named $name already exists in table ${this.name}" }
        argumentCheck(before.type != DataType.ID) { "Can't add column before the identifier since identifier must always be the first column" }

        val index = this.columns.indexOfFirst { col -> col.name == before.name }
        this.columns.add(index, Column(name, DataType.ENUM))
        this.database.addToTable(this,
                                 name,
                                 DataType.ENUM,
                                 "'${defaultValue.javaClass.name}:${defaultValue.name}'",
                                 before.name)
    }

    /**
     * Inserts a column before another column.
     *
     * For enumerations, use the other [insertColumn] method.
     *
     * **Usage example:**
     * ```kotlin
     * table.insertColumn("email", DataType.STRING, "age")
     * ```
     *
     * @param name The name of the new column.
     * @param type The data type of the new column.
     * @param beforeColumnName The name of the column before which to insert the new column.
     */
    fun insertColumn(name: String, type: DataType, beforeColumnName: String)
    {
        this.insertColumn(name, type, this.getColumn(beforeColumnName))
    }

    /**
     * Inserts a column of enumeration type before another column.
     *
     * **Usage example:**
     * ```kotlin
     * table.insertColumn("status", MyEnum.A, "age")
     * ```
     *
     * @param name The name of the new column.
     * @param defaultValue The default value for the new column.
     * @param beforeColumnName The name of the column before which to insert the new column.
     */
    fun <E : Enum<E>> insertColumn(name: String, defaultValue: E, beforeColumnName: String)
    {
        this.insertColumn(name, defaultValue, this.getColumn(beforeColumnName))
    }

    /**
     * Removes a column from the table.
     *
     * **Usage example:**
     * ```kotlin
     * table.removeColumn(COLUMN_EMAIL)
     * ```
     *
     * @param column The column to remove.
     */
    fun removeColumn(column: Column)
    {
        this.checkReadOnly()
        this.checkColumn(column)

        if (column.type == DataType.ID)
        {
            throw IllegalArgumentException("Can't remove the table ID column")
        }

        this.columns.removeIf { col -> col.name == column.name }
        this.database.removeFromTable(this, column.name)
    }

    /**
     * Removes a column from the table.
     *
     * **Usage example:**
     * ```kotlin
     * table.removeColumn("email")
     * ```
     *
     * @param columnName The name of the column to remove.
     */
    fun removeColumn(columnName: String)
    {
        this.removeColumn(this.getColumn(columnName))
    }
}
