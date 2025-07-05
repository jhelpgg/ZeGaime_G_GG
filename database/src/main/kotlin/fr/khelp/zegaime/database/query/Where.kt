package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.MatchDSL
import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.condition.EQUALS
import fr.khelp.zegaime.database.condition.EQUALS_ID
import fr.khelp.zegaime.database.condition.IN
import fr.khelp.zegaime.database.condition.LIKE
import fr.khelp.zegaime.database.condition.LOWER
import fr.khelp.zegaime.database.condition.LOWER_EQUALS
import fr.khelp.zegaime.database.condition.NOT_EQUALS
import fr.khelp.zegaime.database.condition.NOT_EQUALS_ID
import fr.khelp.zegaime.database.condition.ONE_OF
import fr.khelp.zegaime.database.condition.ONE_OF_ID
import fr.khelp.zegaime.database.condition.UPPER
import fr.khelp.zegaime.database.condition.UPPER_EQUALS
import fr.khelp.zegaime.database.condition.regex
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.utils.regex.RegularExpression
import java.util.Calendar
import java.util.regex.Pattern

/**
 * Where condition definition
 *
 * See documentation for condition definition DSL syntax
 */
open class Where internal constructor(private val table : Table)
{
    /**Used for define the condition*/
    var condition : Condition? = null

    // EQUALS

    /**
     * Create condition that select rows, in given column, wih values are primary key equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS_ID(id : Int) = this@Where.table.getColumn(this) EQUALS_ID id

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : String) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Boolean) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Byte) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Short) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Int) = this@Where.table.getColumn(this) EQUALS value
    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Long) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Float) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Double) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : ByteArray) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : IntArray) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : Calendar) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : DataDate) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.EQUALS(value : DataTime) = this@Where.table.getColumn(this) EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are equals to given parameter
     *
     * Specification by column name
     */
    infix fun <E : Enum<E>> String.EQUALS(value : E) = this@Where.table.getColumn(this) EQUALS value

    // NOT_EQUALS

    /**
     * Create condition that select rows, in given column, wih values are primary key not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS_ID(id : Int) = this@Where.table.getColumn(this) NOT_EQUALS_ID id

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : String) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Boolean) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Byte) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Short) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Int) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Long) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Float) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Double) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : ByteArray) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : IntArray) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : Calendar) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : DataDate) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.NOT_EQUALS(value : DataTime) = this@Where.table.getColumn(this) NOT_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are not equals to given parameter
     *
     * Specification by column name
     */
    infix fun <E : Enum<E>> String.NOT_EQUALS(value : E) = this@Where.table.getColumn(this) NOT_EQUALS value

    // LOWER

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : String) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Byte) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Short) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Int) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Long) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Float) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Double) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : Calendar) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : DataDate) = this@Where.table.getColumn(this) LOWER value

    /**
     * Create condition that select rows, in given column, wih values are lower to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER(value : DataTime) = this@Where.table.getColumn(this) LOWER value

    // LOWER_EQUALS

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : String) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Byte) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Short) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Int) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Long) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Float) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Double) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : Calendar) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : DataDate) = this@Where.table.getColumn(this) LOWER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are lower or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.LOWER_EQUALS(value : DataTime) = this@Where.table.getColumn(this) LOWER_EQUALS value

    // UPPER

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : String) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Byte) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Short) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Int) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Long) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Float) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Double) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : Calendar) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : DataDate) = this@Where.table.getColumn(this) UPPER value

    /**
     * Create condition that select rows, in given column, wih values are upper to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER(value : DataTime) = this@Where.table.getColumn(this) UPPER value

    // UPPER_EQUALS

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : String) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Byte) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Short) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Int) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Long) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Float) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Double) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : Calendar) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : DataDate) = this@Where.table.getColumn(this) UPPER_EQUALS value

    /**
     * Create condition that select rows, in given column, wih values are upper or equals to given parameter
     *
     * Specification by column name
     */
    infix fun String.UPPER_EQUALS(value : DataTime) = this@Where.table.getColumn(this) UPPER_EQUALS value

    // Match select

    /**
     * Create condition that select rows, in given column, wih values match the result of given selection
     *
     * See documentation for match DSL syntax
     *
     * Specification by column name
     */
    @MatchDSL
    infix fun String.IN(matchCreator : Match.() -> Unit) = this@Where.table.getColumn(this) IN matchCreator

    // One of

    /**
     * Create condition that select rows, in given column, wih values are primary key inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF_ID(selection : IntArray) = this@Where.table.getColumn(this) ONE_OF_ID selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<String>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : BooleanArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : ByteArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : ShortArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : IntArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : LongArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : FloatArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : DoubleArray) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<ByteArray>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<IntArray>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<Calendar>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<DataDate>) = this@Where.table.getColumn(this) ONE_OF selection

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun String.ONE_OF(selection : Array<DataTime>) = this@Where.table.getColumn(this) ONE_OF selection

    infix fun String.LIKE(pattern : String) = this@Where.table.getColumn(this) LIKE pattern

    /**
     * Create condition that select rows, in given column, wih values are inside given array
     *
     * Specification by column name
     */
    infix fun <E : Enum<E>> String.ONE_OF(selection : Array<E>) = this@Where.table.getColumn(this) ONE_OF selection

    // Regex

    /**
     * Create condition that select rows, in given column, with values match given regular expression
     */
    infix fun Column.REGEX(pattern : Pattern) =
        this.regex(this@Where.table, pattern)

    /**
     * Create condition that select rows, in given column, with values match given regular expression
     *
     * Specification by column name
     */
    infix fun String.REGEX(pattern : Pattern) =
        this@Where.table.getColumn(this)
            .regex(this@Where.table, pattern)

    /**
     * Create condition that select rows, in given column, with values match given regular expression
     */
    infix fun Column.REGEX(regularExpression : RegularExpression) =
        this.regex(this@Where.table, regularExpression)

    /**
     * Create condition that select rows, in given column, with values match given regular expression
     *
     * Specification by column name
     */
    infix fun String.REGEX(regularExpression : RegularExpression) =
        this@Where.table.getColumn(this)
            .regex(this@Where.table, regularExpression)
}