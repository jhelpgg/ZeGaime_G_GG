package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Brown(color : Int) : BaseColor<Brown>
{
    /** Brown 50 */
    BROWN_0050(COLOR_BROWN_0050),

    /** Brown 100 */
    BROWN_0100(COLOR_BROWN_0100),

    /** Brown 200 */
    BROWN_0200(COLOR_BROWN_0200),

    /** Brown 300 */
    BROWN_0300(COLOR_BROWN_0300),

    /** Brown 400 */
    BROWN_0400(COLOR_BROWN_0400),

    /** Brown 500 : Reference */
    BROWN_0500(COLOR_BROWN_0500),

    /** Brown 600 */
    BROWN_0600(COLOR_BROWN_0600),

    /** Brown 700 */
    BROWN_0700(COLOR_BROWN_0700),

    /** Brown 800 */
    BROWN_0800(COLOR_BROWN_0800),

    /** Brown 900 */
    BROWN_0900(COLOR_BROWN_0900),

    ;

    override val color : ARGB = color.argb

    override val lighter : Brown
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Brown
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Brown get() = Brown.BROWN_0050

    override val darkest : Brown get() = Brown.BROWN_0900

    override val representative : Brown get() = Brown.BROWN_0500
}
