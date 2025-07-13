package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Yellow(color : Int) : BaseColor<Yellow>
{
    /** Yellow 50 */
    YELLOW_0050(COLOR_YELLOW_0050),

    /** Yellow 100 */
    YELLOW_0100(COLOR_YELLOW_0100),

    /** Yellow 200 */
    YELLOW_0200(COLOR_YELLOW_0200),

    /** Yellow 300 */
    YELLOW_0300(COLOR_YELLOW_0300),

    /** Yellow 400 */
    YELLOW_0400(COLOR_YELLOW_0400),

    /** Yellow 500 : Reference */
    YELLOW_0500(COLOR_YELLOW_0500),

    /** Yellow 600 */
    YELLOW_0600(COLOR_YELLOW_0600),

    /** Yellow 700 */
    YELLOW_0700(COLOR_YELLOW_0700),

    /** Yellow 800 */
    YELLOW_0800(COLOR_YELLOW_0800),

    /** Yellow 900 */
    YELLOW_0900(COLOR_YELLOW_0900),

    /** Yellow A100 */
    YELLOW_A100(COLOR_YELLOW_A100),

    /** Yellow A200 */
    YELLOW_A200(COLOR_YELLOW_A200),

    /** Yellow A400 */
    YELLOW_A400(COLOR_YELLOW_A400),

    /** Yellow A700 */
    YELLOW_A700(COLOR_YELLOW_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Yellow
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Yellow
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Yellow get() = Yellow.YELLOW_0050

    override val darkest : Yellow get() = Yellow.YELLOW_A700

    override val representative : Yellow get() = Yellow.YELLOW_0500
}
