package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Grey(color : Int) : BaseColor<Grey>
{
    /** White */
    WHITE(COLOR_WHITE),

    /** Grey 50 */
    GREY_0050(COLOR_GREY_0050),

    /** Grey 100 */
    GREY_0100(COLOR_GREY_0100),

    /** Grey 200 */
    GREY_0200(COLOR_GREY_0200),

    /** Grey 300 */
    GREY_0300(COLOR_GREY_0300),

    /** Grey 400 */
    GREY_0400(COLOR_GREY_0400),

    /** Grey 500 : Reference */
    GREY_0500(COLOR_GREY_0500),

    /** Grey 600 */
    GREY_0600(COLOR_GREY_0600),

    /** Grey 700 */
    GREY_0700(COLOR_GREY_0700),

    /** Grey 800 */
    GREY_0800(COLOR_GREY_0800),

    /** Grey 900 */
    GREY_0900(COLOR_GREY_0900),

    /** Black */
    BLACK(COLOR_BLACK)

    ;

    override val color : ARGB = color.argb

    override val lighter : Grey
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Grey
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Grey get() = Grey.WHITE

    override val darkest : Grey get() = Grey.BLACK

    override val representative : Grey get() = Grey.GREY_0500
}
