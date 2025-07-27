package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.condition.AND
import fr.khelp.zegaime.database.condition.not
import fr.khelp.zegaime.database.extensions.validName
import fr.khelp.zegaime.database.query.Delete
import fr.khelp.zegaime.database.query.Insert
import fr.khelp.zegaime.database.query.Select
import fr.khelp.zegaime.database.query.Update
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.security.des.TripleDES
import fr.khelp.zegaime.security.exception.LoginPasswordInvalidException
import fr.khelp.zegaime.security.rsa.RSAKeyPair
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.io.createFile
import fr.khelp.zegaime.utils.logs.exception
import fr.khelp.zegaime.utils.stateCheck
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Connection
import java.util.concurrent.atomic.AtomicBoolean

/**Table of metadata that stores the tables name*/
const val METADATA_TABLE_OF_TABLES = "TABLE_OF_TABLES"

/**Table name column in [METADATA_TABLE_OF_TABLES]*/
const val METADATA_TABLE_OF_TABLES_COLUMN_TABLE = "name"

/**Table of meta data that stores the columns description*/
const val METADATA_TABLE_OF_TABLES_COLUMNS = "TABLE_OF_TABLES_COLUMNS"

/**Column name column in [METADATA_TABLE_OF_TABLES_COLUMNS]*/
const val METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME = "name"

/**Column type column in [METADATA_TABLE_OF_TABLES_COLUMNS]*/
const val METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE = "type"

/**Column foreign table reference [METADATA_TABLE_OF_TABLES_COLUMNS]*/
const val METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE = "ForeignTable"

/**Column foreign column reference [METADATA_TABLE_OF_TABLES_COLUMNS]*/
const val METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN = "ForeignColumn"

/**Table ID where the column lies column in [METADATA_TABLE_OF_TABLES_COLUMNS]*/
const val METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID = "tableID"

/**
 * Returned by [Table.rowID] if no row match to condition
 */
const val ROW_NOT_EXISTS = -1

/**
 * Returned by [Table.rowID] if condition matches to more than one row match
 */
const val ROW_NOT_UNIQUE = -2

/**
 * Represents a database.
 *
 * A database is a collection of tables. It is identified by a path on the file system.
 * The database is encrypted with a login and a password.
 *
 * It is recommended to close the database properly with the [close] method when it is no longer needed,
 * at least before exiting the application.
 *
 * **Creation example**
 * ```kotlin
 * val database = Database.database("login", "password", "path/to/database")
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * val table = database.table("myTable") {
 *     "name" AS DataType.STRING
 *     "age" AS DataType.INTEGER
 * }
 * // ...
 * database.close()
 * ```
 *
 * @property path The path to the database file.
 * @see Table
 */
class Database private constructor(login : String, password : String, val path : String) : Iterable<Table>
{
    companion object
    {
        private val databases = HashMap<String, Database>()
        private var lock = Object()

        /**
         * Creates or opens a database connection.
         *
         * If a database is already opened for the given path, it will be returned.
         * Otherwise, a new database connection will be created.
         *
         * **Usage example**
         * ```kotlin
         * val database = Database.database("login", "password", "path/to/database")
         * ```
         *
         * @param login The login for the database.
         * @param password The password for the database.
         * @param path The path to the database file.
         * @return The database instance.
         * @throws LoginPasswordInvalidException if the login or password is not valid for an existing database.
         */
        fun database(login : String, password : String, path : String) : Database
        {
            synchronized(lock)
            {
                var database = Database.databases[path]

                if (database == null || database.closed)
                {
                    database = Database(login, password, path)
                    Database.databases[path] = database
                }

                if (!database.valid(login, password))
                {
                    throw LoginPasswordInvalidException()
                }

                return database
            }
        }
    }

    private val tripleDES = TripleDES(login, password)
    private val databaseConnection : Connection
    private val tables = ArrayList<Table>()
    private val checkForeignKey = AtomicBoolean(false)
    private val checkingForeignKey = AtomicBoolean(false)

    private val initialized = AtomicBoolean(false)

    /**
     * The table that contains all tables reference.
     *
     */
    val metadataTableOfTables : Table

    /**
     * The table of tables' columns.
     *
     */
    val metadataTableOfTablesColumn : Table

    /**
     * Indicates if the database connection is closed.
     */
    val closed get() = this.databaseConnection.isClosed

    init
    {
        val keyFile = File("${path}.key")
        val rsaKeyPair : RSAKeyPair

        if (keyFile.exists())
        {
            rsaKeyPair = RSAKeyPair(this.tripleDES, FileInputStream(keyFile))
        }
        else
        {
            if (!keyFile.createFile())
            {
                throw IOException("Can't create key file : ${keyFile.absolutePath}")
            }

            rsaKeyPair = RSAKeyPair()
            val fileOutputStream = FileOutputStream(keyFile)
            rsaKeyPair.save(this.tripleDES, fileOutputStream)
            fileOutputStream.close()
        }

        this.databaseConnection = DatabaseAccess.createConnection(path, rsaKeyPair)
        this.databaseConnection.autoCommit = false

        this.metadataTableOfTables = this.table(METADATA_TABLE_OF_TABLES, true) {
            METADATA_TABLE_OF_TABLES_COLUMN_TABLE AS DataType.STRING
        }
        this.metadataTableOfTablesColumn = this.table(METADATA_TABLE_OF_TABLES_COLUMNS, true) {
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME AS DataType.STRING
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE AS DataType.ENUM
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE AS DataType.STRING
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN AS DataType.STRING
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID AS DataType.INTEGER
        }

        this.initialized.set(true)
    }

    /**
     * Checks if the given login and password are valid for this database.
     *
     * **Usage example**
     * ```kotlin
     * if (database.valid("login", "password")) {
     *     // ...
     * }
     * ```
     *
     * @param login The login to check.
     * @param password The password to check.
     * @return `true` if the login and password are valid, `false` otherwise.
     */
    fun valid(login : String, password : String) : Boolean
    {
        this.checkClose()
        return this.tripleDES.valid(login, password)
    }

    /**
     * Commits the last changes and closes the database connection properly.
     *
     * **Usage example**
     * ```kotlin
     * database.close()
     * ```
     */
    fun close()
    {
        this.checkClose()
        this.checkIdForeignKey()
        this.databaseConnection.commit()
        this.simpleQuery("SHUTDOWN")
        this.databaseConnection.close()
    }

    /**
     * Obtains a table by its name.
     *
     * **Usage example**
     * ```kotlin
     * val table = database.obtainTable("myTable")
     * ```
     *
     * @param name The name of the table to obtain.
     * @return The table instance, or `null` if the table does not exist.
     */
    fun obtainTable(name : String) : Table?
    {
        this.checkClose()
        return this.tables.firstOrNull { table -> name.equals(table.name, true) }
    }

    internal fun obtainTableOrReadIt(name : String, alsoAddIt : Boolean) : Table?
    {
        this.checkClose()
        var table = this.tables.firstOrNull { table -> name.equals(table.name, true) }

        if (table == null)
        {
            val id = this.metadataTableOfTables.rowID { condition = METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS name }

            if (id != ROW_NOT_EXISTS)
            {
                val result = this.metadataTableOfTablesColumn.select {
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN
                    where { condition = METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID EQUALS id }
                }

                table = this.createTable(name) {
                    while (result.hasNext)
                    {
                        result.next {
                            val type = getEnum<DataType>(2)

                            val foreignTable = getString(3)

                            if (foreignTable.isEmpty())
                            {
                                if (type != DataType.ID)
                                {
                                    getString(1) AS type
                                }
                            }
                            else
                            {
                                val foreignColumn = getString(4)
                                val tableForeign = obtainTable(foreignTable)

                                if (tableForeign != null)
                                {
                                    if (type == DataType.ID)
                                    {
                                        idForeign(tableForeign, foreignColumn)
                                    }
                                    else
                                    {
                                        getString(1) FOREIGN tableForeign
                                    }
                                }
                            }
                        }
                    }

                    result.close()
                }

                if (alsoAddIt)
                {
                    this.tables += table
                }
            }
        }

        return table
    }

    /**
     * Removes a table from the database.
     *
     * **Usage example**
     * ```kotlin
     * database.dropTable("myTable")
     * ```
     *
     * @param tableName The name of the table to drop.
     * @return `true` if the table was dropped successfully, `false` otherwise.
     */
    fun dropTable(tableName : String) =
        this.obtainTable(tableName)
            ?.let { table -> this.dropTable(table) } ?: false

    /**
     * Creates a table.
     *
     * See the documentation to know more about the table creation DSL syntax.
     *
     * **Usage example**
     * ```kotlin
     * val table = database.table("myTable") {
     *     "name" AS DataType.STRING
     *     "age" AS DataType.INTEGER
     * }
     * ```
     *
     * @param name The name of the table to create.
     * @param creator A lambda function to define the table's columns.
     * @return The created table.
     */
    @CreateTableDSL
    fun table(name : String, creator : Table.() -> Unit) : Table
    {
        this.checkClose()
        argumentCheck(name.validName()) { "Invalid table name : $name" }
        argumentCheck(this.obtainTable(name) == null) { "A table $name already exists" }
        return this.table(name, false, creator)
    }

    /**
     * Removes a table from the database.
     *
     * **Usage example**
     * ```kotlin
     * database.dropTable(myTable)
     * ```
     *
     * @param table The table to drop.
     * @return `true` if the table was dropped successfully, `false` otherwise.
     */
    fun dropTable(table : Table) : Boolean
    {
        table.checkReadOnly()

        if (table !in this.tables)
        {
            return false
        }

        this.tables.remove(table)
        val tableName = table.name

        if (this.updateQuery("DROP TABLE $tableName") < 0)
        {
            return false
        }

        this.delete(this.metadataTableOfTablesColumn) {
            where {
                condition = METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID IN {
                    select(metadataTableOfTables) {
                        +COLUMN_ID
                        where { condition = METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS tableName }
                    }
                }
            }
        }

        this.delete(this.metadataTableOfTables) {
            where {
                condition =
                    METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS tableName
            }
        }
        return true
    }

    /**
     * Returns an iterator over the tables in the database.
     */
    override fun iterator() : Iterator<Table>
    {
        this.checkClose()
        return this.tables.iterator()
    }

    internal fun updateIDForeign(table : Table, foreignTable : String, foreignColumn : String)
    {
        table[0].foreignTable = foreignTable
        table[0].foreignColumn = foreignColumn
        this.update(this.metadataTableOfTablesColumn) {
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE IS foreignTable
            METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN IS foreignColumn
            where {
                condition = (METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME EQUALS "ID") AND
                        (METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID IN {
                            select(metadataTableOfTables) {
                                +"ID"
                                where { condition = METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS table.name }
                            }
                        })
            }
        }
    }

    fun select(select : Select) : DataRowResult
    {
        this.checkClose()
        val statement = this.databaseConnection.createStatement()
        val resultSet = statement.executeQuery(select.selectSQL())
        return DataRowResult(statement, resultSet, select, select.table)
    }

    internal fun insert(insert : Insert) : Int
    {
        this.checkClose()
        var rowID = ROW_NOT_EXISTS

        insert.conditionUpdateOneMatch?.let { conditionUpdate ->
            rowID = insert.table.rowID { condition = conditionUpdate }
        }

        if (rowID >= 0)
        {
            this.updateQuery(insert.updateSQL(rowID))
            return rowID
        }

        this.updateQuery(insert.insertSQL(this.biggestID(insert.table) + 1))
        return this.biggestID(insert.table)
    }

    internal fun update(update : Update) : Int
    {
        this.checkClose()
        val numberUpdate = this.updateQuery(update.updateSQL())

        if (numberUpdate > 0)
        {
            this.checkIdForeignKey()
        }

        return numberUpdate
    }

    fun delete(delete : Delete) : Int
    {
        this.checkClose()
        val numberDelete = this.updateQuery(delete.deleteSQL())

        if (numberDelete > 0)
        {
            this.checkIdForeignKey()
        }

        return numberDelete
    }

    internal fun addToTable(table : Table,
                            columnName : String,
                            dataType : DataType,
                            serializedDefaultValue : String,
                            before : String? = null)
    {
        val tableID =
            this.metadataTableOfTables.rowID {
                condition =
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME EQUALS table.name
            }

        if (tableID < 0)
        {
            return
        }

        val query = StringBuilder()
        query.append("ALTER TABLE ")
        query.append(table.name)
        query.append(" ADD COLUMN ")
        query.append(columnName)
        query.append(" ")
        query.append(dataType.typeSQL)
        query.append(" DEFAULT ")
        query.append(serializedDefaultValue)

        before?.let { beforeColumn ->
            query.append(" BEFORE ")
            query.append(beforeColumn)
        }

        this.updateQuery(query.toString())

        this.delete(this.metadataTableOfTablesColumn) {
            where {
                condition =
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID EQUALS tableID
            }
        }

        for (column in table)
        {
            this.insert(this.metadataTableOfTablesColumn) {
                METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME IS column.name
                METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE IS column.type
                METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE IS column.foreignTable
                METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN IS column.foreignColumn
                METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID IS tableID
            }
        }
    }

    internal fun removeFromTable(table : Table, columnName : String)
    {
        val tableID =
            this.metadataTableOfTables.rowID {
                condition =
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME EQUALS table.name
            }

        if (tableID < 0)
        {
            return
        }

        this.updateQuery("ALTER TABLE ${table.name} DROP COLUMN $columnName")

        this.delete(this.metadataTableOfTablesColumn) {
            where {
                condition = (METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID EQUALS tableID) AND
                        (METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME EQUALS columnName)
            }
        }
    }

    private fun biggestID(table : Table) : Int
    {
        val result = table.select {
            +COLUMN_ID
            descendant(COLUMN_ID)
        }

        var id = 0

        if (result.hasNext)
        {
            result.next { id = getID(1) }
        }

        result.close()
        return id
    }

    @CreateTableDSL
    private fun table(name : String, readOnly : Boolean, tableCreator : Table.() -> Unit) : Table
    {
        var table = this.obtainTable(name)

        if (table != null)
        {
            return table
        }

        table = Table(name, readOnly, this)
        tableCreator(table)
        table = this.createTable(table)
        this.tables += table
        return table
    }

    private fun createTable(table : Table) : Table
    {
        if (!table.readOnly)
        {
            val id =
                this.metadataTableOfTables.rowID { condition = METADATA_TABLE_OF_TABLES_COLUMN_TABLE EQUALS table.name }

            if (id != ROW_NOT_EXISTS)
            {
                val result = this.metadataTableOfTablesColumn.select {
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE
                    +METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN
                    where { condition = METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID EQUALS id }
                }

                return this.createTable(table.name) {
                    while (result.hasNext)
                    {
                        result.next {
                            val type = getEnum<DataType>(2)

                            val foreignTable = getString(3)

                            if (foreignTable.isEmpty())
                            {
                                if (type != DataType.ID)
                                {
                                    getString(1) AS type
                                }
                            }
                            else
                            {
                                val foreignColumn = getString(4)
                                val tableForeign = obtainTable(foreignTable)

                                if (tableForeign != null)
                                {
                                    if (type == DataType.ID)
                                    {
                                        idForeign(tableForeign, foreignColumn)
                                    }
                                    else
                                    {
                                        getString(1) FOREIGN tableForeign
                                    }
                                }
                            }
                        }
                    }

                    result.close()
                }
            }
        }

        val query = StringBuilder()
        query.append("CREATE TABLE ")
        query.append(table.name)
        query.append(" (")
        var notFirst = false

        for (column in table)
        {
            if (notFirst)
            {
                query.append(" , ")
            }

            query.append(column.name)
            query.append(" ")
            query.append(column.type.typeSQL)

            if (column.foreignTable.isNotEmpty() && column.type != DataType.ID)
            {
                query.append(" FOREIGN KEY REFERENCES ")
                query.append(column.foreignTable)
                query.append(" (")
                query.append(column.foreignColumn)
                query.append(") ON DELETE CASCADE ON UPDATE CASCADE")
            }

            notFirst = true
        }

        query.append(")")
        this.updateQuery(query.toString())

        if (!table.readOnly)
        {
            val tableID = this.insert(this.metadataTableOfTables) {
                METADATA_TABLE_OF_TABLES_COLUMN_TABLE IS table.name
            }

            for (column in table)
            {
                this.insert(this.metadataTableOfTablesColumn) {
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_NAME IS column.name
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TYPE IS column.type
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_TABLE IS column.foreignTable
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_FOREIGN_COLUMN IS column.foreignColumn
                    METADATA_TABLE_OF_TABLES_COLUMNS_COLUMN_TABLE_ID IS tableID
                }
            }
        }

        return table
    }

    @CreateTableDSL
    private fun createTable(name : String, tableCreator : Table.() -> Unit) : Table
    {
        val table = Table(name, false, this)
        tableCreator(table)
        return table
    }

    @InsertDSL
    private fun insert(table : Table, insertCreator : Insert.() -> Unit) : Int
    {
        val insert = Insert(table)
        insertCreator(insert)
        return this.insert(insert)
    }

    @UpdateDSL
    private fun update(table : Table, updateCreator : Update.() -> Unit) : Int
    {
        val update = Update(table)
        updateCreator(update)
        return this.update(update)
    }

    @DeleteSL
    private fun delete(table : Table, deleteCreator : Delete.() -> Unit) : Int
    {
        val delete = Delete(table)
        deleteCreator(delete)
        return this.delete(delete)
    }

    /**
     * Make a query that no need result and not modify the database
     * @param query Query to do
     */
    private fun simpleQuery(query : String)
    {
        if (query.isNotEmpty())
        {
            val statement = this.databaseConnection.createStatement()
            statement.executeQuery(query)
            statement.close()
            this.databaseConnection.commit()
        }
    }

    /**
     * Make a query that modify the database: [createTable], [delete], [update], [insert]
     * @param query Query to do
     */
    private fun updateQuery(query : String) : Int
    {
        if (query.isNotEmpty())
        {
            try
            {
                val statement = this.databaseConnection.createStatement()
                val count = statement.executeUpdate(query)
                statement.close()
                this.databaseConnection.commit()
                return count
            }
            catch (exception : Exception)
            {
                if (this.initialized.get())
                {
                    exception(exception, "Failed while do query : '", query, "'")
                }

                return -1
            }
        }

        return 0
    }

    private fun checkClose()
    {
        stateCheck(!this.closed) { "The database is closed, call 'Database.database' to reopen it!" }
    }

    internal fun checkIdForeignKey()
    {
        this.checkForeignKey.set(true)

        if (!this.checkingForeignKey.getAndSet(true))
        {
            this.checkingForeignKey()
        }
    }

    private fun checkingForeignKey()
    {
        while (this.checkForeignKey.getAndSet(false))
        {
            for (table in this.tables)
            {
                val columnID = table[0]

                if (columnID.foreignTable.isNotEmpty())
                {
                    val tableForeign = obtainTable(columnID.foreignTable) ?: continue
                    this.delete(table) {
                        where {
                            condition = not("ID" IN {
                                select(tableForeign) {
                                    +columnID.foreignColumn
                                }
                            })
                        }
                    }
                }
            }

            this.checkForeignKey.set(this.checkingForeignKey.getAndSet(false))
        }
    }
}
