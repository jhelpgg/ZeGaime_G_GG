package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.math.PI_FLOAT

val Float.degreeToRadian : Float get() = (this * PI_FLOAT) / 180f
val Float.radianToDegree : Float get() = (this * 180f) / PI_FLOAT
