package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.SwordData

/**
 * Sword creator
 * @param name Sword name
 */
class SwordCreator(name : String) : NodeCreator(name)
{
    /** Sword size */
    var size : Float = 3.3f
        set(value)
        {
            field = value.coerceIn(2f, 5f)
        }

    /** Sword base material name */
    var baseMaterial : String = ""
    /** Sword blade material name */
    var bladeMaterial : String = ""

    /**
     * Convert to node data for save
     * @return Node data for save
     */
    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.SWORD,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 swordData = SwordData(size = this.size,
                                       baseMaterial = this.baseMaterial,
                                       bladeMaterial = this.bladeMaterial))
}