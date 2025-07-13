package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Cyan(color : Int) : BaseColor<Cyan>
{
    /** Cyan 50 */
    CYAN_0050(COLOR_CYAN_0050),

    /** Cyan 100 */
    CYAN_0100(COLOR_CYAN_0100),

    /** Cyan 200 */
    CYAN_0200(COLOR_CYAN_0200),

    /** Cyan 300 */
    CYAN_0300(COLOR_CYAN_0300),

    /** Cyan 400 */
    CYAN_0400(COLOR_CYAN_0400),

    /** Cyan 500 : Reference */
    CYAN_0500(COLOR_CYAN_0500),

    /** Cyan 600 */
    CYAN_0600(COLOR_CYAN_0600),

    /** Cyan 700 */
    CYAN_0700(COLOR_CYAN_0700),

    /** Cyan 800 */
    CYAN_0800(COLOR_CYAN_0800),

    /** Cyan 900 */
    CYAN_0900(COLOR_CYAN_0900),

    /** Cyan A100 */
    CYAN_A100(COLOR_CYAN_A100),

    /** Cyan A200 */
    CYAN_A200(COLOR_CYAN_A200),

    /** Cyan A400 */
    CYAN_A400(COLOR_CYAN_A400),

    /** Cyan A700 */
    CYAN_A700(COLOR_CYAN_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Cyan
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Cyan
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Cyan get() = Cyan.CYAN_0050

    override val darkest : Cyan get() = Cyan.CYAN_A700

    override val representative : Cyan get() = Cyan.CYAN_0500
}
