package fr.khelp.zegaime.engine3d.gui.dsl

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.engine3d.gui.layout.constraint.GUIConstraintLayout

class GUIConstraintFiller internal constructor(private val layout : GUIConstraintLayout)
{
    infix fun GUIComponent.with(constraint : GUIConstraintParameterFiller.() -> Unit)
    {
        val constraintParameter = GUIConstraintParameterFiller()
        constraint(constraintParameter)
        this@GUIConstraintFiller.layout.add(this, constraintParameter.constraint)
    }
}
