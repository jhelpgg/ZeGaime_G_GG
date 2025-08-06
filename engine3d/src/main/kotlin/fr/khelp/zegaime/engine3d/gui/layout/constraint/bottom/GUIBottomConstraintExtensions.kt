package fr.khelp.zegaime.engine3d.gui.layout.constraint.bottom

import fr.khelp.zegaime.engine3d.gui.component.GUIComponent

val GUIBottomConstraint.attached : GUIComponent?
    get() =
        when (this)
        {
            is GUIBottomAtParent, is GUIBottomFree -> null
            is GUIBottomToBottomOf                 -> this.component
            is GUIBottomToTopOf                    -> this.component
        }