package fr.khelp.zegaime.images.color.base

import fr.khelp.zegaime.images.color.ARGB
import fr.khelp.zegaime.images.color.argb
import kotlin.math.max
import kotlin.math.min

enum class Indigo(color : Int) : BaseColor<Indigo>
{
    /** Indigo 50 */
    INDIGO_0050(COLOR_INDIGO_0050),

    /** Indigo 100 */
    INDIGO_0100(COLOR_INDIGO_0100),

    /** Indigo 200 */
    INDIGO_0200(COLOR_INDIGO_0200),

    /** Indigo 300 */
    INDIGO_0300(COLOR_INDIGO_0300),

    /** Indigo 400 */
    INDIGO_0400(COLOR_INDIGO_0400),

    /** Indigo 500 : Reference */
    INDIGO_0500(COLOR_INDIGO_0500),

    /** Indigo 600 */
    INDIGO_0600(COLOR_INDIGO_0600),

    /** Indigo 700 */
    INDIGO_0700(COLOR_INDIGO_0700),

    /** Indigo 800 */
    INDIGO_0800(COLOR_INDIGO_0800),

    /** Indigo 900 */
    INDIGO_0900(COLOR_INDIGO_0900),

    /** Indigo A100 */
    INDIGO_A100(COLOR_INDIGO_A100),

    /** Indigo A200 */
    INDIGO_A200(COLOR_INDIGO_A200),

    /** Indigo A400 */
    INDIGO_A400(COLOR_INDIGO_A400),

    /** Indigo A700 */
    INDIGO_A700(COLOR_INDIGO_A700)

    ;

    override val color : ARGB = color.argb

    override val lighter : Indigo
        get() = entries[max(0, this.ordinal - 1)]

    override val darker : Indigo
        get() = entries[min(entries.size - 1, this.ordinal + 1)]

    override val lightest : Indigo get() = Indigo.INDIGO_0050

    override val darkest : Indigo get() = Indigo.INDIGO_A700

    override val representative : Indigo get() = Indigo.INDIGO_0500
}
