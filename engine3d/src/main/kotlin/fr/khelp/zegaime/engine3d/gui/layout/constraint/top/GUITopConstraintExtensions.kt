package fr.khelp.zegaime.engine3d.gui.layout.constraint.top

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent

val GUITopConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUITopAtParent, is GUITopFree -> null
            is GUITopToTopOf                 -> this.component
            is GUITopToBottomOf              -> this.component
        }