package fr.khelp.zegaime.database

import fr.khelp.zegaime.database.exception.InvalidDataTypeException
import fr.khelp.zegaime.database.type.DataType
import java.util.Objects

/**
 * Represents a table's column.
 *
 * A column is defined by its name and its data type.
 * It can also have a foreign key relationship with another table.
 *
 * **Creation example**
 * ```kotlin
 * // Create a column for the user name
 * val nameColumn = Column("name", DataType.TEXT)
 * // Create a column for the user age
 * val ageColumn = Column("age", DataType.INTEGER)
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * // Create a list of columns for a table
 * val columns = listOf(nameColumn, ageColumn)
 * // Create a table with the columns
 * val table = Table("users", columns)
 * ```
 *
 * @property name The name of the column. It is case-insensitive.
 * @property type The data type of the column.
 * @see Table
 * @see DataType
 */
data class Column internal constructor(val name : String, val type : DataType) : Comparable<Column>
{
    /**
     * The name of the foreign table if this column is a foreign key.
     *
     * This property is for internal use of the database system.
     */
    internal var foreignTable = ""

    /**
     * The name of the foreign column if this column is a foreign key.
     *
     * This property is for internal use of the database system.
     */
    internal var foreignColumn = ""

    /**
     * Checks if the column's data type matches the expected data type.
     *
     * **Usage example**
     * ```kotlin
     * val nameColumn = Column("name", DataType.TEXT)
     * nameColumn.checkType(DataType.TEXT) // This will not throw an exception
     * try {
     *     nameColumn.checkType(DataType.INTEGER) // This will throw an InvalidDataTypeException
     * } catch (e: InvalidDataTypeException) {
     *     // Handle the exception
     *     println(e.message)
     * }
     * ```
     *
     * @param expectedType The expected data type.
     * @throws InvalidDataTypeException if the column's data type does not match the expected data type.
     */
    fun checkType(expectedType : DataType)
    {
        if (this.type != expectedType)
        {
            throw InvalidDataTypeException(this.type, expectedType)
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * The comparison is based on the column name (case-insensitive) and the data type.
     *
     * @param other The reference object with which to compare.
     * @return `true` if this object is the same as the obj argument; `false` otherwise.
     */
    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Column)
        {
            return false
        }

        return this.name.equals(other.name, true) && this.type == other.type
    }

    /**
     * Returns a hash code value for the object.
     *
     * The hash code is based on the uppercase column name and the data type to be consistent with the [equals] method.
     *
     * @return A hash code value for this object.
     */
    override fun hashCode() : Int =
        Objects.hash(this.name.uppercase(), this.type)

    /**
     * Compares this object with the specified object for order.
     *
     * The comparison is based on the column name (case-insensitive).
     *
     * @param other The object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    override operator fun compareTo(other : Column) : Int =
        this.name.compareTo(other.name, true)
}

/**
 * The common column for the table's primary key.
 *
 * It is an ID column with the name "ID" and the data type `DataType.ID`.
 * This column is automatically added to tables that are created from a `DatabaseObject`.
 */
val COLUMN_ID = Column("ID", DataType.ID)