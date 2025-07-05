package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.InsertDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDSL
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.condition.EQUALS_ID
import fr.khelp.zegaime.database.exception.NoValueDefinedAndNoDefaultValue
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import java.util.Calendar
import java.util.TreeSet

/**
 * For insert a row in table
 *
 * It can be used as insert ot update if specifies a condition in [updateIfExactlyOneRowMatch] and that condition match to one and only one row
 *
 * See documentation about insert DSL syntax
 */
class Insert internal constructor(val table : Table)
{
    private val columnValues = HashMap<Column, String>()
    internal var conditionUpdateOneMatch : Condition? = null

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : String)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.STRING)
        this@Insert.columnValues[this] = "'$value'"
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : String)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Boolean)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BOOLEAN)
        this@Insert.columnValues[this] =
            if (value)
            {
                "TRUE"
            }
            else
            {
                "FALSE"
            }
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Boolean)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Byte)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Byte)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Short)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.SHORT)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Short)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Int)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.INTEGER)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Int)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Long)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.LONG)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Long)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Float)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.FLOAT)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Float)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Double)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DOUBLE)
        this@Insert.columnValues[this] = value.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Double)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : ByteArray)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.BYTE_ARRAY)
        this@Insert.columnValues[this] = "'${value.base64}'"
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : ByteArray)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : IntArray)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.INT_ARRAY)
        this@Insert.columnValues[this] = "'${value.serializeToByteArray().base64}'"
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : IntArray)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : Calendar)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.CALENDAR)
        this@Insert.columnValues[this] = value.timeInMillis.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : Calendar)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : DataDate)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.DATE)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : DataDate)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun Column.IS(value : DataTime)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.TIME)
        this@Insert.columnValues[this] = value.serialized.toString()
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun String.IS(value : DataTime)
    {
        this@Insert.table.getColumn(this) IS value
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun <E : Enum<E>> Column.IS(value : E)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Insert.columnValues[this] = "'${value::class.java.name}:${value.name}'"
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    infix fun <E : Enum<E>> String.IS(value : E)
    {
        this@Insert.table.getColumn(this) IS value
    }

    @InsertDSL
    internal infix fun Column.IS_ENUM(value : Any)
    {
        this@Insert.table.checkColumn(this)
        this.checkType(DataType.ENUM)
        this@Insert.columnValues[this] =
            "'${value::class.java.name}:${value::class.java.getDeclaredField("name").get(value)}'"
    }

    /**
     * Specifies value for a column
     */
    @InsertDSL
    internal infix fun String.IS_ENUM(value : Any)
    {
        this@Insert.table.getColumn(this) IS_ENUM value
    }

    /**
     * If specified and condition match to one and only one row, this row is updated.
     *
     * In other case an insert is done
     */
    @WhereDSL
    fun updateIfExactlyOneRowMatch(whereCreator : Where.() -> Unit)
    {
        val where = Where(this.table)
        whereCreator(where)
        val condition = where.condition ?: throw IllegalStateException("Choose a condition with 'condition ='")
        condition.checkAllColumns(this.table)
        this.conditionUpdateOneMatch = condition
    }

    internal fun insertSQL(suggestedID : Int) : String
    {
        val columnLeft = TreeSet<Column>()

        for (column in this.table)
        {
            if (column.type != DataType.ID)
            {
                columnLeft.add(column)
            }
        }

        val query = StringBuilder()
        query.append("INSERT INTO ")
        query.append(this.table.name)
        query.append(" (")
        val queryValues = StringBuilder()
        queryValues.append(") VALUES (")

        query.append(COLUMN_ID.name)
        queryValues.append(suggestedID)

        for ((column, value) in this.columnValues)
        {
            query.append(", ")
            queryValues.append(", ")
            query.append(column.name)
            queryValues.append(value)
            columnLeft.remove(column)
        }

        var defaultValue : String

        for (column in columnLeft)
        {
            defaultValue = column.type.defaultValueSerialized

            if (defaultValue.isEmpty())
            {
                throw NoValueDefinedAndNoDefaultValue(column)
            }

            query.append(", ")
            queryValues.append(", ")
            query.append(column.name)
            queryValues.append(defaultValue)
        }

        query.append(queryValues)
        query.append(")")

        return query.toString()
    }

    internal fun updateSQL(id : Int) : String
    {
        val update = Update(this.table)
        update.transfer(this.columnValues)
        update.where { condition = COLUMN_ID EQUALS_ID id }
        return update.updateSQL()
    }
}
