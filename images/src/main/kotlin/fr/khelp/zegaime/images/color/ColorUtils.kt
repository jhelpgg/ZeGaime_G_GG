package fr.khelp.zegaime.images.color

import fr.khelp.zegaime.utils.extensions.blue
import fr.khelp.zegaime.utils.extensions.green
import fr.khelp.zegaime.utils.extensions.red

val Color.toArgb : ARGB
    get() =
        when (this)
        {
            is ARGB -> this

            is AYUV ->
                ARGB(alpha = this.alpha,
                     red = (this.y - 9_267 * (this.u - 128) + 14_016_868 * (this.v - 128)) / 10_000_000,
                     green = (this.y - 3_436_954 * (this.u - 128) - 7_141_690 * (this.v - 128)) / 10_000_000,
                     blue = (this.y + 17_721_604 * (this.u - 128) + 9_902 * (this.v - 128)) / 10_000_000)
        }

val Color.toAyuv : AYUV
    get() =
        when (this)
        {
            is ARGB ->
                AYUV(alpha = this.alpha,
                     y = (this.red * 299 + this.green * 587 + this.blue * 114) / 1_000,
                     u = (-169 * this.red - 331 * this.green + 500 * this.blue + 128) / 1_000,
                     v = (500 * this.red - 419 * this.green - 81 * this.blue + 128) / 1_000)

            is AYUV -> this
        }

val Int.argb : ARGB
    get() = ARGB(alpha = (this shr 24) and 0xFF,
                 red = (this shr 16) and 0xFF,
                 green = (this shr 8) and 0xFF,
                 blue = this and 0xFF)

val Int.ayuv : AYUV get() = this.argb.toAyuv

val Color.argb : Int
    get()
    {
        val argb = this.toArgb
        return (argb.alpha shl 24) or (argb.red shl 16) or (argb.green shl 8) or argb.blue
    }

val Color.alpha : Int
    get() =
        when (this)
        {
            is ARGB -> this.alpha
            is AYUV -> this.alpha
        }

val Color.red : Int
    get() =
        when (this)
        {
            is ARGB -> this.red
            is AYUV -> this.argb.red
        }

val Color.green : Int
    get() =
        when (this)
        {
            is ARGB -> this.green
            is AYUV -> this.argb.green
        }

val Color.blue : Int
    get() =
        when (this)
        {
            is ARGB -> this.blue
            is AYUV -> this.argb.blue
        }

val Color.y : Int
    get() =
        when (this)
        {
            is ARGB -> this.toAyuv.y
            is AYUV -> this.y
        }

val Color.u : Int
    get() =
        when (this)
        {
            is ARGB -> this.toAyuv.u
            is AYUV -> this.u
        }

val Color.v : Int
    get() =
        when (this)
        {
            is ARGB -> this.toAyuv.v
            is AYUV -> this.v
        }

fun argb(alpha : Int, red : Int, green : Int, blue : Int) : ARGB = ARGB(alpha, red, green, blue)
fun argb(alpha : Float, red : Float, green : Float, blue : Float) : ARGB =
    ARGB((alpha * 255).toInt(), (red).toInt(), (green).toInt(), (blue).toInt())

fun rgb(red : Int, green : Int, blue : Int) : ARGB = ARGB(255, red, green, blue)
fun rgb(red : Float, green : Float, blue : Float) : ARGB =
    ARGB(255, (red).toInt(), (green).toInt(), (blue).toInt())

fun gray(alpha : Int, gray : Int) : ARGB = ARGB(alpha, gray, gray, gray)
fun gray(gray : Int) : ARGB = ARGB(255, gray, gray, gray)
fun ayuv(alpha : Int, y : Int, u : Int, v : Int) : AYUV = AYUV(alpha, y, u, v)
fun yuv(y : Int, u : Int, v : Int) : AYUV = AYUV(255, y, u, v)

fun Color.changBrightness(factor : Double) : Color
{
    val ayuv = this.toAyuv
    return AYUV(alpha = ayuv.alpha,
                y = (ayuv.y * factor).toInt(),
                u = ayuv.u,
                v = ayuv.v)
}

val Color.semiVisible : Color
    get() =
        when (this)
        {
            is ARGB -> ARGB(128, this.red, this.green, this.blue)

            is AYUV -> AYUV(128, this.y, this.u, this.v)
        }

val Color.invert : Color
    get() =
        when (this)
        {
            is ARGB -> ARGB(this.alpha, 255 - this.red, 255 - this.green, 255 - this.blue)

            is AYUV -> AYUV(this.alpha, 255 - this.y, 255 - this.u, 255 - this.v)
        }

val Color.gray : Color
    get() = gray(this.alpha, this.y)

fun Color.contrast(contrast : Double) : Color
{
    val (a, y, u, v) = this.toAyuv
    val yy = (y * contrast).toInt().coerceIn(0, 255)
    return AYUV(a, yy, u, v)
}