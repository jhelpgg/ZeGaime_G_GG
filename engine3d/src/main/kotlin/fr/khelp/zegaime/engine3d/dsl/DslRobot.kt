package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Head
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Creates a robot using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val robot = robot("myRobot") {
 *     position = NodePosition(x = 1f)
 *     head {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param robotID The ID of the robot.
 * @param create The lambda function to create the robot.
 * @return The created robot.
 */
@PrebuiltDSL
fun robot(robotID: String, create: RobotCreator.() -> Unit): Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    return robotCreator.robot
}

/**
 * DSL creator for robots.
 *
 * @property robotID The ID of the robot.
 * @constructor Creates a new robot creator.
 */
@PrebuiltDSL
class RobotCreator(robotID: String)
{
    /**
     * The created robot.
     */
    internal val robot = Robot(robotID)

    /**
     * The position of the robot.
     */
    var position: NodePosition
        get() = this.robot.position
        set(value)
        {
            this.robot.position(value)
        }

    /**
     * The material of the robot's body.
     */
    val materialBody: Material = this.robot.materialBody

    /**
     * The position of the robot's limbs.
     */
    var robotPosition: RobotPosition
        get() = this.robot.robotPosition
        set(value)
        {
            this.robot.robotPosition = value
        }

    /**
     * The color of the robot's right arm.
     */
    var rightArmColor: Color4f
        get() = this.robot.rightArmColor
        set(value)
        {
            this.robot.rightArmColor = value
        }

    /**
     * The color of the robot's left arm.
     */
    var leftArmColor: Color4f
        get() = this.robot.leftArmColor
        set(value)
        {
            this.robot.leftArmColor = value
        }

    /**
     * The color of the robot's right leg.
     */
    var rightLegColor: Color4f
        get() = this.robot.rightLegColor
        set(value)
        {
            this.robot.rightLegColor = value
        }

    /**
     * The color of the robot's left leg.
     */
    var leftLegColor: Color4f
        get() = this.robot.leftLegColor
        set(value)
        {
            this.robot.leftLegColor = value
        }

    /**
     * Configures the robot's head.
     *
     * @param fill The lambda function to configure the head.
     */
    @RobotDSL
    fun head(fill: Head.() -> Unit)
    {
        this.robot.head.fill()
    }

    /**
     * Edits the material of the robot's body.
     *
     * @param edit The lambda function to edit the material.
     */
    @MaterialDSL
    fun materialBody(edit: Material.() -> Unit)
    {
        this.materialBody.edit(edit)
    }
}
