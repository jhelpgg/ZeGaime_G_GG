package fr.khelp.zegaime.images.color

/**
 * Color in ARGB (Alpha, Red, Green, Blue)
 */
class ARGB(alpha : Int, red : Int, green : Int, blue : Int) : Color
{
    val alpha : Int = alpha.coerceIn(0, 255)
    val red : Int = red.coerceIn(0, 255)
    val green : Int = green.coerceIn(0, 255)
    val blue : Int = blue.coerceIn(0, 255)

    operator fun component1() : Int = this.alpha
    operator fun component2() : Int = this.red
    operator fun component3() : Int = this.green
    operator fun component4() : Int = this.blue
}