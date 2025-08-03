package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import kotlinx.serialization.Serializable

@Serializable
data class RobotPositionData(val neckAngleX : Float, val neckAngleY : Float, val neckAngleZ : Float,
                             val rightShoulderAngleX : Float, val rightShoulderAngleZ : Float,
                             val rightElbowAngleX : Float,
                             val leftShoulderAngleX : Float, val leftShoulderAngleZ : Float,
                             val leftElbowAngleX : Float,
                             val rightAssAngleX : Float, val rightAssAngleZ : Float,
                             val rightKneeAngleX : Float,
                             val leftAssAngleX : Float, val leftAssAngleZ : Float,
                             val leftKneeAngleX : Float)
{
    constructor(robotPosition : RobotPosition) : this(neckAngleX = robotPosition.neckAngleX,
                                                      neckAngleY = robotPosition.neckAngleY,
                                                      neckAngleZ = robotPosition.neckAngleZ,
        //
                                                      rightShoulderAngleX = robotPosition.rightShoulderAngleX,
                                                      rightShoulderAngleZ = robotPosition.rightShoulderAngleZ,
        //
                                                      rightElbowAngleX = robotPosition.rightElbowAngleX,
        //
                                                      leftShoulderAngleX = robotPosition.leftShoulderAngleX,
                                                      leftShoulderAngleZ = robotPosition.leftShoulderAngleZ,
        //
                                                      leftElbowAngleX = robotPosition.leftElbowAngleX,
        //
                                                      rightAssAngleX = robotPosition.rightAssAngleX,
                                                      rightAssAngleZ = robotPosition.rightAssAngleZ,
        //
                                                      rightKneeAngleX = robotPosition.rightKneeAngleX,
        //
                                                      leftAssAngleX = robotPosition.leftAssAngleX,
                                                      leftAssAngleZ = robotPosition.leftAssAngleZ,
        //
                                                      leftKneeAngleX = robotPosition.leftKneeAngleX)

    val robotPosition : RobotPosition by lazy {
        RobotPosition(neckAngleX = this.neckAngleX, neckAngleY = this.neckAngleY, neckAngleZ = this.neckAngleZ,
                      rightShoulderAngleX = this.rightShoulderAngleX, rightShoulderAngleZ = this.rightShoulderAngleZ,
                      rightElbowAngleX = this.rightElbowAngleX,
                      leftShoulderAngleX = this.leftShoulderAngleX, leftShoulderAngleZ = this.leftShoulderAngleZ,
                      leftElbowAngleX = this.leftElbowAngleX,
                      rightAssAngleX = this.rightAssAngleX, rightAssAngleZ = this.rightAssAngleZ,
                      rightKneeAngleX = this.rightKneeAngleX,
                      leftAssAngleX = this.leftAssAngleX, leftAssAngleZ = this.leftAssAngleZ,
                      leftKneeAngleX = this.leftKneeAngleX)
    }
}