package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Lime(color : Int) : BaseColor<Lime>
{
    /** Lime 50 */
    LIME_0050(COLOR_LIME_0050),

    /** Lime 100 */
    LIME_0100(COLOR_LIME_0100),

    /** Lime 200 */
    LIME_0200(COLOR_LIME_0200),

    /** Lime 300 */
    LIME_0300(COLOR_LIME_0300),

    /** Lime 400 */
    LIME_0400(COLOR_LIME_0400),

    /** Lime 500 : Reference */
    LIME_0500(COLOR_LIME_0500),

    /** Lime 600 */
    LIME_0600(COLOR_LIME_0600),

    /** Lime 700 */
    LIME_0700(COLOR_LIME_0700),

    /** Lime 800 */
    LIME_0800(COLOR_LIME_0800),

    /** Lime 900 */
    LIME_0900(COLOR_LIME_0900),

    /** Lime A100 */
    LIME_A100(COLOR_LIME_A100),

    /** Lime A200 */
    LIME_A200(COLOR_LIME_A200),

    /** Lime A400 */
    LIME_A400(COLOR_LIME_A400),

    /** Lime A700 */
    LIME_A700(COLOR_LIME_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Lime
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Lime
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Lime get() = Lime.LIME_0050

    override val darkest : Lime get() = Lime.LIME_A700

    override val representative : Lime get() = Lime.LIME_0500
}
