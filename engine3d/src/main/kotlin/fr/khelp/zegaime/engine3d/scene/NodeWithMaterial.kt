package fr.khelp.zegaime.engine3d.scene

import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.render.TwoSidedRule

abstract class NodeWithMaterial(id : String) : Node(id)
{
    init
    {
        this.countInRender = true
    }

    var material : Material = Material()
    var materialForSelection : Material = Material()
    var selected : Boolean = false
    var twoSidedRule : TwoSidedRule = TwoSidedRule.AS_MATERIAL

    protected fun material(user : (Material) -> Unit)
    {
        val material =
            if (this.selected)
            {
                this.materialForSelection
            }
            else
            {
                this.material
            }

        val twoSided = material.twoSided

        material.twoSided =
            when (this.twoSidedRule)
            {
                TwoSidedRule.AS_MATERIAL    -> twoSided
                TwoSidedRule.FORCE_ONE_SIDE -> false
                TwoSidedRule.FORCE_TWO_SIDE -> true
            }

        user(material)

        material.twoSided = twoSided
    }
}