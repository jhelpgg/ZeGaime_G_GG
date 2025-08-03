package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.DiceData
import fr.khelp.zegaime.formatk3d.data.GRAY_DATA
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType

class DiceCreator(name : String) : NodeCreator(name)
{
    var color : ColorData = GRAY_DATA

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.DICE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 diceData = DiceData(this.color))
}