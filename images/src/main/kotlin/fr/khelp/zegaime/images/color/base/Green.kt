package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Green(color : Int) : BaseColor<Green>
{
    /** Green 50 */
    GREEN_0050(COLOR_GREEN_0050),

    /** Green 100 */
    GREEN_0100(COLOR_GREEN_0100),

    /** Green 200 */
    GREEN_0200(COLOR_GREEN_0200),

    /** Green 300 */
    GREEN_0300(COLOR_GREEN_0300),

    /** Green 400 */
    GREEN_0400(COLOR_GREEN_0400),

    /** Green 500 : Reference */
    GREEN_0500(COLOR_GREEN_0500),

    /** Green 600 */
    GREEN_0600(COLOR_GREEN_0600),

    /** Green 700 */
    GREEN_0700(COLOR_GREEN_0700),

    /** Green 800 */
    GREEN_0800(COLOR_GREEN_0800),

    /** Green 900 */
    GREEN_0900(COLOR_GREEN_0900),

    /** Green A100 */
    GREEN_A100(COLOR_GREEN_A100),

    /** Green A200 */
    GREEN_A200(COLOR_GREEN_A200),

    /** Green A400 */
    GREEN_A400(COLOR_GREEN_A400),

    /** Green A700 */
    GREEN_A700(COLOR_GREEN_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Green
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Green
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Green get() = Green.GREEN_0050

    override val darkest : Green get() = Green.GREEN_A700

    override val representative : Green get() = Green.GREEN_0500
}
