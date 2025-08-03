package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import kotlinx.serialization.Serializable

/**
 * Robot position data for save
 * @param neckAngleX Neck X angle
 * @param neckAngleY Neck Y angle
 * @param neckAngleZ Neck Z angle
 * @param rightShoulderAngleX Right shoulder X angle
 * @param rightShoulderAngleZ Right shoulder Z angle
 * @param rightElbowAngleX Right elbow X angle
 * @param leftShoulderAngleX Left shoulder X angle
 * @param leftShoulderAngleZ Left shoulder Z angle
 * @param leftElbowAngleX Left elbow X angle
 * @param rightAssAngleX Right ass X angle
 * @param rightAssAngleZ Right ass Z angle
 * @param rightKneeAngleX Right knee X angle
 * @param leftAssAngleX Left ass X angle
 * @param leftAssAngleZ Left ass Z angle
 * @param leftKneeAngleX Left knee X angle
 */
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
    /**
     * Create a new instance of RobotPositionData from a RobotPosition
     * @param robotPosition RobotPosition to copy
     */
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

    /**
     * The RobotPosition
     */
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