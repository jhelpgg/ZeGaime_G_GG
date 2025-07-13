package fr.khelp.zegaime.images.color

/**
 * Color in A-YUV format (Alpha, Y:Luminance, U:Chromatic 1, V:Chromatic 2)
 */
class AYUV(alpha : Int, y : Int, u : Int, v : Int) : Color
{
    val alpha = alpha.coerceIn(0, 255)
    val y = y.coerceIn(0, 255)
    val u = u.coerceIn(0, 255)
    val v = v.coerceIn(0, 255)

    operator fun component1() : Int = this.alpha
    operator fun component2() : Int = this.y
    operator fun component3() : Int = this.u
    operator fun component4() : Int = this.v
}