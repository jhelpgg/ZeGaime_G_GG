package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup
import fr.khelp.zegaime.utils.texts.CharactersInterval
import fr.khelp.zegaime.utils.texts.SimpleCharactersInterval

/** The character as an interval */
val Char.interval : CharactersInterval get() = SimpleCharactersInterval(this, this)

/** Interval with the character in a lower and upper case version */
val Char.ignoreCaseInterval : CharactersInterval get() = this.lowercaseChar().interval + this.uppercaseChar()

/**
 * Add character to an interval
 */
operator fun Char.plus(charactersInterval : CharactersInterval) : CharactersInterval =
    charactersInterval + this

/**
 * String representation in unicode `\uHHHH`
 */
fun Char.toUnicode() : String =
    String.format("\\u%04x",this.code)

val Char.regularExpression : RegularExpression get() = this.interval.regularExpression

val Char.allCharactersExcludeThis : RegularExpression get() = this.interval.allCharactersExcludeThose

val Char.ignoreCaseRegularExpression : RegularExpression get() = this.ignoreCaseInterval.regularExpression

operator fun Char.plus(regularExpression : RegularExpression) =
    this.regularExpression + regularExpression

operator fun Char.plus(regularExpression : RegularExpressionGroup) =
    this.regularExpression + regularExpression

infix fun Char.OR(regularExpression : RegularExpression) =
    this.regularExpression OR regularExpression

infix fun Char.OR(regularExpression : RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun Char.zeroOrMore() : RegularExpression =
    this.regularExpression.zeroOrMore()

fun Char.oneOrMore() : RegularExpression =
    this.regularExpression.oneOrMore()

fun Char.zeroOrOne() : RegularExpression =
    this.regularExpression.zeroOrOne()

fun Char.exactTimes(times : Int) : RegularExpression =
    this.regularExpression.exactTimes(times)

fun Char.atLeast(times : Int) : RegularExpression =
    this.regularExpression.atLeast(times)

fun Char.atMost(times : Int) : RegularExpression =
    this.regularExpression.atMost(times)

fun Char.between(minimum : Int, maximum : Int) : RegularExpression =
    this.regularExpression.between(minimum, maximum)

fun Char.repeat(number : Int) : String
{
    val stringBuilder = StringBuilder()

    for (time in 0 until number)
    {
        stringBuilder.append(this)
    }

    return stringBuilder.toString()
}