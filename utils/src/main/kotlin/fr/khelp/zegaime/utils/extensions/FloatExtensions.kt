package fr.khelp.zegaime.utils.extensions

import fr.khelp.zegaime.utils.math.PI_FLOAT
import fr.khelp.zegaime.utils.math.isNul
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

val Float.degreeToRadian : Float get() = (this * PI_FLOAT) / 180f
val Float.radianToDegree : Float get() = (this * 180f) / PI_FLOAT

/**
 * Modulate a real inside an interval
 *
 * @param minimum  Minimum of interval
 * @param maximum  Maximum of interval
 * @return Modulated value
 */
@Throws(IllegalArgumentException::class)
fun Float.modulo(minimum : Float, maximum : Float) : Float
{
    var real = this
    val min = min(minimum, maximum)
    val max = max(min, maximum)

    if (real in min..max)
    {
        return real
    }

    val space = max - min

    if (isNul(space))
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    real = (real - min) / space

    return (space * (real - floor(real))) + min
}