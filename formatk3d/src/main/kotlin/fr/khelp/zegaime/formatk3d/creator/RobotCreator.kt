package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.COLOR_ARM_DATA
import fr.khelp.zegaime.formatk3d.data.COLOR_LEG_DATA
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.RobotData

class RobotCreator(name : String) : NodeCreator(name)
{
    var materialBody : String = ""
    var rightArmColor : ColorData = COLOR_ARM_DATA
    var leftArmColor : ColorData = COLOR_ARM_DATA
    var rightLegColor : ColorData = COLOR_LEG_DATA
    var leftLegColor : ColorData = COLOR_LEG_DATA

    private var headCreator = HeadCreator()
    private val robotPositionCreator = RobotPositionCreator()

    fun head(fill : HeadCreator.() -> Unit)
    {
        this.headCreator.fill()
    }

    fun robotPosition(fill : RobotPositionCreator.() -> Unit)
    {
        this.robotPositionCreator.fill()
    }

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.ROBOT,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 robotData = RobotData(head = this.headCreator.headData,
                                       materialBody = this.materialBody,
                                       robotPosition = this.robotPositionCreator.robotPositionData,
                                       rightArmColor = this.rightArmColor, leftArmColor = this.leftArmColor,
                                       rightLegColor = this.rightLegColor, leftLegColor = this.leftLegColor))
}