package fr.khelp.zegaime.engine3d.gui.renderer

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent

fun interface GUIGridCellRenderer<V : Any>
{
    fun component(cellValue : V) : GUIComponent
}
