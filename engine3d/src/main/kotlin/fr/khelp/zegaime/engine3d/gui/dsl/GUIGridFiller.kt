package fr.khelp.zegaime.engine3d.gui.dsl

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.engine3d.gui.layout.grid.GUIGridConstraint
import fr.khelp.zegaime.engine3d.gui.layout.grid.GUIGridLayout

class GUIGridFiller internal constructor(private val layout : GUIGridLayout)
{
    operator fun GUIComponent.unaryPlus()
    {
        this@GUIGridFiller.layout.add(this, GUIGridConstraint)
    }
}
