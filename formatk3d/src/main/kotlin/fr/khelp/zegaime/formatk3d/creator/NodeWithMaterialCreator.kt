package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.formatk3d.data.NodeWithMaterialData

abstract class NodeWithMaterialCreator(name : String) : NodeCreator(name)
{
    var material : String = ""

    var materialForSelection : String = ""

    var twoSidedRule = TwoSidedRule.AS_MATERIAL

    val nodeWithMaterialData : NodeWithMaterialData
        get() =
            NodeWithMaterialData(this.material, this.materialForSelection, this.twoSidedRule)
}