package fr.khelp.zegaime.engine3d.gui

/**
 * Margin around a component
 *
 * @property left Left margin
 * @property right Right margin
 * @property top Top margin
 * @property bottom Bottom margin
 */
data class GUIMargin(val left : Int = 0, val right : Int = 0, val top : Int = 0, val bottom : Int = 0)
{
    /** Width adds to the component with the margin */
    val width = this.left + this.right

    /** Height adds to the component with the margin */
    val height = this.top + this.bottom
}
