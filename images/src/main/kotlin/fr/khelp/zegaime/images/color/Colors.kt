package fr.khelp.zegaime.images.color

import fr.khelp.zegaime.images.color.base.Blue
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.base.Red

/** Transparent color. */
val TRANSPARENT : Color = 0.argb

/** Black color. */
val BLACK : Color = gray(0)

/** Dark gray color. */
val DARK_GRAY : Color = gray(64)

/** Gray color. */
val GRAY : Color = gray(128)

/** Light gray color. */
val LIGHT_GRAY : Color = gray(192)

/** White color. */
val WHITE : Color = gray(255)

/** Dark red color. */
val DARK_RED : Color = Red.RED_0900.color

/** Red color. */
val RED : Color = Red.RED_0500.color

/** Light red color. */
val LIGHT_RED : Color = Red.RED_0200.color

/** Dark green color. */
val DARK_GREEN : Color = Green.GREEN_0900.color

/** Green color. */
val GREEN : Color = Green.GREEN_0500.color

/** Light green color. */
val LIGHT_GREEN : Color = Green.GREEN_0200.color

/** Dark blue color. */
val DARK_BLUE : Color = Blue.BLUE_0900.color

/** Blue color. */
val BLUE : Color = Blue.BLUE_0500.color

/** Light blue color. */
val LIGHT_BLUE : Color = Blue.BLUE_0200.color
