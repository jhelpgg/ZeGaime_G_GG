package fr.khelp.zegaime.engine3d.gui.dsl

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.engine3d.gui.layout.horizontal.GUIHorizontalConstraint
import fr.khelp.zegaime.engine3d.gui.layout.horizontal.GUIHorizontalLayout

class GUIHorizontalFiller internal constructor(private val layout : GUIHorizontalLayout)
{
    var margin : Int
        get() = this.layout.margin
        set(value)
        {
            this.layout.margin = value
        }

    val GUIComponent.top : Unit
        get()
        {
            this@GUIHorizontalFiller.layout.add(this, GUIHorizontalConstraint.TOP)
        }

    val GUIComponent.center : Unit
        get()
        {
            this@GUIHorizontalFiller.layout.add(this, GUIHorizontalConstraint.CENTER)
        }

    val GUIComponent.bottom : Unit
        get()
        {
            this@GUIHorizontalFiller.layout.add(this, GUIHorizontalConstraint.BOTTOM)
        }
}
