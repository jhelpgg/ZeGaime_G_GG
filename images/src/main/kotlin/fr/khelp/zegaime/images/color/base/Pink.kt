package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Pink(color : Int) : BaseColor<Pink>
{
    /** Pink 50 */
    PINK_0050(COLOR_PINK_0050),

    /** Pink 100 */
    PINK_0100(COLOR_PINK_0100),

    /** Pink 200 */
    PINK_0200(COLOR_PINK_0200),

    /** Pink 300 */
    PINK_0300(COLOR_PINK_0300),

    /** Pink 400 */
    PINK_0400(COLOR_PINK_0400),

    /** Pink 500 : Reference */
    PINK_0500(COLOR_PINK_0500),

    /** Pink 600 */
    PINK_0600(COLOR_PINK_0600),

    /** Pink 700 */
    PINK_0700(COLOR_PINK_0700),

    /** Pink 800 */
    PINK_0800(COLOR_PINK_0800),

    /** Pink 900 */
    PINK_0900(COLOR_PINK_0900),

    /** Pink A100 */
    PINK_A100(COLOR_PINK_A100),

    /** Pink A200 */
    PINK_A200(COLOR_PINK_A200),

    /** Pink A400 */
    PINK_A400(COLOR_PINK_A400),

    /** Pink A700 */
    PINK_A700(COLOR_PINK_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Pink
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Pink
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Pink get() = Pink.PINK_0050

    override val darkest : Pink get() = Pink.PINK_A700

    override val representative : Pink get() = Pink.PINK_0500
}
