package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class BlueGrey(color : Int) : BaseColor<BlueGrey>
{
    /** Blue_grey 50 */
    BLUE_GREY_0050(COLOR_BLUE_GREY_0050),

    /** Blue_grey 100 */
    BLUE_GREY_0100(COLOR_BLUE_GREY_0100),

    /** Blue_grey 200 */
    BLUE_GREY_0200(COLOR_BLUE_GREY_0200),

    /** Blue_grey 300 */
    BLUE_GREY_0300(COLOR_BLUE_GREY_0300),

    /** Blue_grey 400 */
    BLUE_GREY_0400(COLOR_BLUE_GREY_0400),

    /** Blue_grey 500 : Reference */
    BLUE_GREY_0500(COLOR_BLUE_GREY_0500),

    /** Blue_grey 600 */
    BLUE_GREY_0600(COLOR_BLUE_GREY_0600),

    /** Blue_grey 700 */
    BLUE_GREY_0700(COLOR_BLUE_GREY_0700),

    /** Blue_grey 800 */
    BLUE_GREY_0800(COLOR_BLUE_GREY_0800),

    /** Blue_grey 900 */
    BLUE_GREY_0900(COLOR_BLUE_GREY_0900)

    ;

    override val color : ARGB = color.argb

    override val lighter : BlueGrey
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : BlueGrey
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : BlueGrey get() = BlueGrey.BLUE_GREY_0050

    override val darkest : BlueGrey get() = BlueGrey.BLUE_GREY_0900

    override val representative : BlueGrey get() = BlueGrey.BLUE_GREY_0500
}
