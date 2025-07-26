package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Head
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@PrebuiltDSL
fun robot(robotID : String, create : RobotCreator.() -> Unit) : Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    return robotCreator.robot
}

@PrebuiltDSL
class RobotCreator(robotID : String)
{
    internal val robot = Robot(robotID)

    var position : NodePosition
        get() = this.robot.position
        set(value)
        {
            this.robot.position(value)
        }

    val materialBody : Material = this.robot.materialBody

    var robotPosition : RobotPosition
        get() = this.robot.robotPosition
        set(value)
        {
            this.robot.robotPosition = value
        }

    var rightArmColor : Color4f
        get() = this.robot.rightArmColor
        set(value)
        {
            this.robot.rightArmColor = value
        }

    var leftArmColor : Color4f
        get() = this.robot.leftArmColor
        set(value)
        {
            this.robot.leftArmColor = value
        }

    var rightLegColor : Color4f
        get() = this.robot.rightLegColor
        set(value)
        {
            this.robot.rightLegColor = value
        }

    var leftLegColor : Color4f
        get() = this.robot.leftLegColor
        set(value)
        {
            this.robot.leftLegColor = value
        }

    @RobotDSL
    fun head(fill : Head.() -> Unit)
    {
        this.robot.head.fill()
    }

    @MaterialDSL
    fun materialBody(edit : Material.() -> Unit)
    {
        this.materialBody.edit(edit)
    }
}