package fr.khelp.zegaime.images.color

import fr.khelp.zegaime.images.color.base.Blue
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.base.Red

val TRANSPARENT : Color = 0.argb

val BLACK : Color = gray(0)
val DARK_GRAY : Color = gray(64)
val GRAY : Color = gray(128)
val LIGHT_GRAY : Color = gray(192)
val WHITE : Color = gray(255)

val DARK_RED : Color = Red.RED_0900.color
val RED : Color = Red.RED_0500.color
val LIGHT_RED : Color = Red.RED_0200.color

val DARK_GREEN : Color = Green.GREEN_0900.color
val GREEN : Color = Green.GREEN_0500.color
val LIGHT_GREEN : Color = Green.GREEN_0200.color

val DARK_BLUE : Color = Blue.BLUE_0900.color
val BLUE : Color = Blue.BLUE_0500.color
val LIGHT_BLUE : Color = Blue.BLUE_0200.color
