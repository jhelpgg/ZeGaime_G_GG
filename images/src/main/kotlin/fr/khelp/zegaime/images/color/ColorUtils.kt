package fr.khelp.zegaime.images.color

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

fun argb(alpha : Int, red : Int, green : Int, blue : Int) : ARGB = ARGB(alpha, red, green, blue)
fun rgb(red : Int, green : Int, blue : Int) : ARGB = ARGB(255, red, green, blue)
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