package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.math.Percent
import fr.khelp.zegaime.utils.math.Rational

operator fun Number.plus(percent : Percent) : Double = this.toDouble() * (1.0 + percent.percent)
operator fun Number.minus(percent : Percent) : Double = this.toDouble() * (1.0 - percent.percent)
/**
 * Transform this number to rational
 */
fun Number.toRational() =
    when (this)
    {
        is Rational                        -> this
        is Byte, is Short, is Int, is Long -> Rational.createRational(this.toLong())
        is Float, is Double                -> Rational.createRational(this.toDouble())
        else                               -> Rational.INVALID
    }

/**
 * Add this number with given rational
 */
operator fun Number.plus(rational: Rational) = this.toRational() + rational

/**
 * Subtract this number with given rational
 */
operator fun Number.minus(rational: Rational) = this.toRational() - rational

/**
 * Multiply this number with given rational
 */
operator fun Number.times(rational: Rational) = this.toRational() * rational

/**
 * Divide this number with given rational
 */
operator fun Number.div(rational: Rational) = this.toRational() / rational

/**
 * Compare this number with given rational
 */
operator fun Number.compareTo(rational: Rational) =
    this.toRational()
        .compareTo(rational)
