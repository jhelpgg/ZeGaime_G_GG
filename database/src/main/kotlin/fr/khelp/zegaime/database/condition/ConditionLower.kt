package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.database.type.DataType
import java.util.Calendar

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(string : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name}<'$string'")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Byte) : Condition
{
    this.checkType(DataType.BYTE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Short) : Condition
{
    this.checkType(DataType.SHORT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Int) : Condition
{
    this.checkType(DataType.INTEGER)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Long) : Condition
{
    this.checkType(DataType.LONG)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Float) : Condition
{
    this.checkType(DataType.FLOAT)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Double) : Condition
{
    this.checkType(DataType.DOUBLE)
    return Condition(arrayOf(this), "${this.name}<$value")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : Calendar) : Condition
{
    this.checkType(DataType.CALENDAR)
    return Condition(arrayOf(this), "${this.name}<${value.timeInMillis}")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : DataDate) : Condition
{
    this.checkType(DataType.DATE)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}

/**
 * Create condition that select rows, in given column, wih values are lower to given parameter
 */
infix fun Column.LOWER(value : DataTime) : Condition
{
    this.checkType(DataType.TIME)
    return Condition(arrayOf(this), "${this.name}<${value.serialized}")
}

