package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.collections.lists.SortedArray
import fr.khelp.zegaime.utils.math.max
import fr.khelp.zegaime.utils.math.min
import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup
import fr.khelp.zegaime.utils.texts.CharactersInterval
import fr.khelp.zegaime.utils.texts.EmptyCharactersInterval
import fr.khelp.zegaime.utils.texts.SimpleCharactersInterval
import fr.khelp.zegaime.utils.texts.UnionCharactersInterval

operator fun CharactersInterval.plus(charactersInterval : CharactersInterval) : CharactersInterval
{
    if (this == EmptyCharactersInterval)
    {
        return charactersInterval
    }

    if (charactersInterval == EmptyCharactersInterval)
    {
        return this
    }

    val simpleIntervals = SortedArray<SimpleCharactersInterval>(true)

    if (this is SimpleCharactersInterval)
    {
        simpleIntervals += this
    }
    else if (this is UnionCharactersInterval)
    {
        for (interval in this.simpleIntervals)
        {
            simpleIntervals += interval
        }
    }

    if (charactersInterval is SimpleCharactersInterval)
    {
        simpleIntervals += charactersInterval
    }
    else if (charactersInterval is UnionCharactersInterval)
    {
        for (interval in charactersInterval.simpleIntervals)
        {
            simpleIntervals += interval
        }
    }

    val size = simpleIntervals.size

    if (size == 0)
    {
        return EmptyCharactersInterval
    }

    var currentInterval = simpleIntervals[0]

    if (size == 1)
    {
        return currentInterval
    }

    val collectIntervals = SortedArray<SimpleCharactersInterval>(true)
    var observedInterval : SimpleCharactersInterval

    for (index in 1 until size)
    {
        observedInterval = simpleIntervals[index]

        if (currentInterval.maximum + 1 < observedInterval.minimum)
        {
            collectIntervals += currentInterval
            currentInterval = observedInterval
        }
        else
        {
            currentInterval = SimpleCharactersInterval(min(currentInterval.minimum, observedInterval.minimum),
                                                       max(currentInterval.maximum, observedInterval.maximum))
        }
    }

    if (collectIntervals.empty)
    {
        return currentInterval
    }

    collectIntervals += currentInterval
    return UnionCharactersInterval(collectIntervals.immutableList())
}

operator fun CharactersInterval.plus(char : Char) : CharactersInterval =
    this + char.interval

val CharactersInterval.regularExpression : RegularExpression get() = RegularExpression.interval(this)

val CharactersInterval.allCharactersExcludeThose : RegularExpression get() = RegularExpression.allCharactersExclude(this)

operator fun CharactersInterval.plus(regularExpression : RegularExpression) =
    this.regularExpression + regularExpression

operator fun CharactersInterval.plus(regularExpression : RegularExpressionGroup) =
    this.regularExpression + regularExpression

infix fun CharactersInterval.OR(regularExpression : RegularExpression) =
    this.regularExpression OR regularExpression

infix fun CharactersInterval.OR(regularExpression : RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun CharactersInterval.zeroOrMore() : RegularExpression =
    this.regularExpression.zeroOrMore()

fun CharactersInterval.oneOrMore() : RegularExpression =
    this.regularExpression.oneOrMore()

fun CharactersInterval.zeroOrOne() : RegularExpression =
    this.regularExpression.zeroOrOne()

fun CharactersInterval.exactTimes(times : Int) : RegularExpression =
    this.regularExpression.exactTimes(times)

fun CharactersInterval.atLeast(times : Int) : RegularExpression =
    this.regularExpression.atLeast(times)

fun CharactersInterval.atMost(times : Int) : RegularExpression =
    this.regularExpression.atMost(times)

fun CharactersInterval.between(minimum : Int, maximum : Int) : RegularExpression =
    this.regularExpression.between(minimum, maximum)
