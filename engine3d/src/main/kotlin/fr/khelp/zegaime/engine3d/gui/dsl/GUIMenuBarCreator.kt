package fr.khelp.zegaime.engine3d.gui.dsl

import fr.khelp.zegaime.engine3d.gui.menu.GUIMenuBar

class GUIMenuBarCreator internal constructor(private val menuBar : GUIMenuBar)
{
    operator fun String.invoke(menuCreator : GUIMenuCreator.() -> Unit)
    {
        val menu = this@GUIMenuBarCreator.menuBar.addMenu(this)
        val creator = GUIMenuCreator(menu)
        menuCreator(creator)
    }
}
