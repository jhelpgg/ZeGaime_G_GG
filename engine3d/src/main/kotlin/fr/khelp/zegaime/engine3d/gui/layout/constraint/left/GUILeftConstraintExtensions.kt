package fr.khelp.zegaime.engine3d.gui.layout.constraint.left

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent

val GUILeftConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUILeftAtParent, is GUILeftFree -> null
            is GUILeftToLeftOf                 -> this.component
            is GUILeftToRightOf                -> this.component
        }