package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.SwordData

class SwordCreator(name : String) : NodeCreator(name)
{
    var size : Float = 3.3f
        set(value)
        {
            field = value.coerceIn(2f, 5f)
        }

    var baseMaterial : String = ""
    var bladeMaterial : String = ""

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.SWORD,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 swordData = SwordData(size = this.size,
                                       baseMaterial = this.baseMaterial,
                                       bladeMaterial = this.bladeMaterial))
}