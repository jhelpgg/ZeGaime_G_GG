package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.Color

fun BaseColor<*>.colorAlpha(alpha : Int) : Color = ARGB(alpha, this.color.red, this.color.green, this.color.blue)
