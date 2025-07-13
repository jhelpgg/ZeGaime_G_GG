package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Red(color : Int) : BaseColor<Red>
{
    /** Red 50 */
    RED_0050(COLOR_RED_0050),

    /** Red 100 */
    RED_0100(COLOR_RED_0100),

    /** Red 200 */
    RED_0200(COLOR_RED_0200),

    /** Red 300 */
    RED_0300(COLOR_RED_0300),

    /** Red 400 */
    RED_0400(COLOR_RED_0400),

    /** Red 500 : Reference */
    RED_0500(COLOR_RED_0500),

    /** Red 600 */
    RED_0600(COLOR_RED_0600),

    /** Red 700 */
    RED_0700(COLOR_RED_0700),

    /** Red 800 */
    RED_0800(COLOR_RED_0800),

    /** Red 900 */
    RED_0900(COLOR_RED_0900),

    /** Red A100 */
    RED_A100(COLOR_RED_A100),

    /** Red A200 */
    RED_A200(COLOR_RED_A200),

    /** Red A400 */
    RED_A400(COLOR_RED_A400),

    /** Red A700 */
    RED_A700(COLOR_RED_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Red
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Red
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Red get() = Red.RED_0050

    override val darkest : Red get() = Red.RED_A700

    override val representative : Red get() = Red.RED_0500
}
