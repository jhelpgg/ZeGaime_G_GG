package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.formatk3d.data.NodeWithMaterialData

/**
 * Node with material creator
 * @param name Node name
 */
abstract class NodeWithMaterialCreator(name : String) : NodeCreator(name)
{
    /** Material name to use */
    var material : String = ""

    /** Material name to use for selection */
    var materialForSelection : String = ""

    /** Two sided rule */
    var twoSidedRule = TwoSidedRule.AS_MATERIAL

    /** Node with material data for save */
    val nodeWithMaterialData : NodeWithMaterialData
        get() =
            NodeWithMaterialData(this.material, this.materialForSelection, this.twoSidedRule)
}