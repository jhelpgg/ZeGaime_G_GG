package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.utils.extensions.base64
import fr.khelp.zegaime.utils.extensions.serializeToByteArray
import java.util.Calendar

/**
 * Create condition that select rows, in given column, wih values are primary key not equals to given parameter
 */
infix fun Column.NOT_EQUALS_ID(id : Int) : Condition
{
    this.checkType(DataType.ID)
    return Condition(arrayOf(this), "${this.name}!=$id")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(string : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}!='$string'")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Boolean) : Condition
{
    this.checkType(DataType.BOOLEAN)
    return if (value)
    {
        Condition(arrayOf(this), "${this.name}!=TRUE")
    }
    else
    {
        Condition(arrayOf(this), "${this.name}!=FALSE")
    }
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Byte) : Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Short) : Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Int) : Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Long) : Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Float) : Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Double) : Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}!=$value")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : ByteArray) : Condition
{
    this.checkType(DataType.BYTE_ARRAY)
    return Condition(arrayOf(this), "${this.name}!='${value.base64}'")
}

/**
 * Create condition that select rows, in given column, wih values are equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : IntArray) : Condition
{
    this.checkType(DataType.INT_ARRAY)
    return Condition(arrayOf(this), "${this.name}='${value.serializeToByteArray().base64}'")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : Calendar) : Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}!=${value.timeInMillis}")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : DataDate) : Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}!=${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun Column.NOT_EQUALS(value : DataTime) : Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}!=${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are not equals to given parameter
 */
infix fun <E : Enum<E>> Column.NOT_EQUALS(value : E) : Condition
{
    this.checkType(DataType.ENUM)
    return Condition(arrayOf(this), "${this.name}!='${value::class.java.name}:${value.name}'")
}
