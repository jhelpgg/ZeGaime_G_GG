package fr.khelp.zegaime.engine3d.gui.layout.constraint.right

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent

val GUIRightConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUIRightAtParent, is GUIRightFree -> null
            is GUIRightToLeftOf                  -> this.component
            is GUIRightToRightOf                 -> this.component
        }
