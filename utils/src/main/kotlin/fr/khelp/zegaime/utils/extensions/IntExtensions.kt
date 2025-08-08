package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.Time
import fr.khelp.zegaime.utils.math.Percent

val Int.alpha : Int get() = (this shr 24) and 0xFF
val Int.red : Int get() = (this shr 16) and 0xFF
val Int.green : Int get() = (this shr 8) and 0xFF
val Int.blue : Int get() = this and 0xFF

val Int.milliseconds get() = Time(this)
val Int.seconds get() = Time(this * 1000L)
val Int.minutes get() = Time(this * 1000L * 60L)
val Int.hours get() = Time(this * 1000L * 60L * 60L)
val Int.days get() = Time(this * 1000L * 60L * 60L * 24L)

val Int.percent : Percent get() = Percent(this)