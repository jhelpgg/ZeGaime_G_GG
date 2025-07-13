package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Purple(color : Int) : BaseColor<Purple>
{
    /** Purple 50 */
    PURPLE_0050(COLOR_PURPLE_0050),

    /** Purple 100 */
    PURPLE_0100(COLOR_PURPLE_0100),

    /** Purple 200 */
    PURPLE_0200(COLOR_PURPLE_0200),

    /** Purple 300 */
    PURPLE_0300(COLOR_PURPLE_0300),

    /** Purple 400 */
    PURPLE_0400(COLOR_PURPLE_0400),

    /** Purple 500 : Reference */
    PURPLE_0500(COLOR_PURPLE_0500),

    /** Purple 600 */
    PURPLE_0600(COLOR_PURPLE_0600),

    /** Purple 700 */
    PURPLE_0700(COLOR_PURPLE_0700),

    /** Purple 800 */
    PURPLE_0800(COLOR_PURPLE_0800),

    /** Purple 900 */
    PURPLE_0900(COLOR_PURPLE_0900),

    /** Purple A100 */
    PURPLE_A100(COLOR_PURPLE_A100),

    /** Purple A200 */
    PURPLE_A200(COLOR_PURPLE_A200),

    /** Purple A400 */
    PURPLE_A400(COLOR_PURPLE_A400),

    /** Purple A700 */
    PURPLE_A700(COLOR_PURPLE_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Purple
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Purple
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Purple get() = Purple.PURPLE_0050

    override val darkest : Purple get() = Purple.PURPLE_A700

    override val representative : Purple get() = Purple.PURPLE_0500
}
