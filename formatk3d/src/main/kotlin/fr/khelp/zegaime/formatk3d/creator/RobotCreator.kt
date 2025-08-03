package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.COLOR_ARM_DATA
import fr.khelp.zegaime.formatk3d.data.COLOR_LEG_DATA
import fr.khelp.zegaime.formatk3d.data.ColorData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.RobotData

/**
 * Robot creator
 * @param name Robot name
 */
class RobotCreator(name : String) : NodeCreator(name)
{
    /** Robot body material name */
    var materialBody : String = ""
    /** Robot right arm color */
    var rightArmColor : ColorData = COLOR_ARM_DATA
    /** Robot left arm color */
    var leftArmColor : ColorData = COLOR_ARM_DATA
    /** Robot right leg color */
    var rightLegColor : ColorData = COLOR_LEG_DATA
    /** Robot left leg color */
    var leftLegColor : ColorData = COLOR_LEG_DATA

    /** Robot head creator */
    private var headCreator = HeadCreator()
    /** Robot position creator */
    private val robotPositionCreator = RobotPositionCreator()

    /**
     * Define robot head
     * @param fill Head creation lambda
     */
    fun head(fill : HeadCreator.() -> Unit)
    {
        this.headCreator.fill()
    }

    /**
     * Define robot position
     * @param fill Position creation lambda
     */
    fun robotPosition(fill : RobotPositionCreator.() -> Unit)
    {
        this.robotPositionCreator.fill()
    }

    /**
     * Convert to node data for save
     * @return Node data for save
     */
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