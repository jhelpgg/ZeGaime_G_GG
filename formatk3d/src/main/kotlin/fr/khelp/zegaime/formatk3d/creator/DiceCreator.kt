package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.DiceData
import fr.khelp.zegaime.formatk3d.data.GRAY_DATA
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType

/**
 * Creates a dice
 * @param name Dice name
 */
class DiceCreator(name : String) : NodeCreator(name)
{
    /** Dice color */
    var color : ColorData = GRAY_DATA

    /**
     * Convert to node data for save
     * @return Node data for save
     */
    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.DICE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 diceData = DiceData(this.color))
}