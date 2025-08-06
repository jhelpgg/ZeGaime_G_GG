package fr.khelp.zegaime.engine3d.gui.layout.constraint

import fr.khelp.zegaime.engine3d.gui.GUIMargin
import fr.khelp.zegaime.engine3d.gui.layout.GUIConstraints
import fr.khelp.zegaime.engine3d.gui.layout.constraint.bottom.GUIBottomAtParent
import fr.khelp.zegaime.engine3d.gui.layout.constraint.bottom.GUIBottomConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.left.GUILeftAtParent
import fr.khelp.zegaime.engine3d.gui.layout.constraint.left.GUILeftConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.right.GUIRightAtParent
import fr.khelp.zegaime.engine3d.gui.layout.constraint.right.GUIRightConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.top.GUITopAtParent
import fr.khelp.zegaime.engine3d.gui.layout.constraint.top.GUITopConstraint

class GUIConstraintConstraint() : GUIConstraints
{
    var horizontalSize : GUIConstraintsSize = GUIConstraintsSize.EXPANDED
    var verticalSize : GUIConstraintsSize = GUIConstraintsSize.EXPANDED
    var leftConstraint : GUILeftConstraint = GUILeftAtParent
    var rightConstraint : GUIRightConstraint = GUIRightAtParent
    var topConstraint : GUITopConstraint = GUITopAtParent
    var bottomConstraint : GUIBottomConstraint = GUIBottomAtParent
    var margin = GUIMargin()

    internal constructor(x : Int, y : Int, xMax : Int, yMax : Int) : this()
    {
        this.x = x
        this.y = y
        this.xMax = xMax
        this.yMax = yMax
        this.computed = true
    }

    internal var x = 0
    internal var y = 0
    internal var xMax = 0
    internal var yMax = 0
    internal var computed = false
    internal var constraintsLeft : GUIConstraintConstraint? = null
    internal var constraintsRight : GUIConstraintConstraint? = null
    internal var constraintsTop : GUIConstraintConstraint? = null
    internal var constraintsBottom : GUIConstraintConstraint? = null
    internal val allDependencyComputed : Boolean
        get() =
            (this.constraintsLeft?.computed ?: true)
            && (this.constraintsRight?.computed ?: true)
            && (this.constraintsTop?.computed ?: true)
            && (this.constraintsBottom?.computed ?: true)
}
