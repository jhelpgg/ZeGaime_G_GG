package fr.khelp.zegaime.engine3d.animations

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition

/**
 * An animation of a [Robot] based on key times.
 *
 * This animation interpolates the position of the robot's limbs over time.
 *
 * **Creation example**
 * ```kotlin
 * val animation = AnimationRobotKeyTime(myRobot)
 * animation.addKeyTimeValue(0, RobotPosition(neckAngleX = 0f))
 * animation.addKeyTimeValue(1000, RobotPosition(neckAngleX = 10f))
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * AnimationManager.play(animation)
 * ```
 *
 * @param robot The robot to animate.
 * @constructor Creates a new robot key time animation.
 */
class AnimationRobotKeyTime(robot : Robot) : AnimationKeyTime<Robot, RobotPosition>(robot)
{
    /**
     * Gets the current position of the robot.
     *
     * @param animated The robot.
     * @return The current position of the robot.
     */
    override fun getValue(animated : Robot) : RobotPosition =
        animated.robotPosition

    /**
     * Sets the position of the robot.
     *
     * @param animated The robot.
     * @param value The new position of the robot.
     */
    override fun setValue(animated : Robot, value : RobotPosition)
    {
        animated.robotPosition = value
    }

    /**
     * Interpolates the position of the robot between two keyframes.
     *
     * @param animated The robot.
     * @param beforeValue The position of the previous keyframe.
     * @param beforeCoefficient The coefficient of the previous keyframe.
     * @param afterValue The position of the next keyframe.
     * @param afterCoefficient The coefficient of the next keyframe.
     */
    override fun interpolate(animated : Robot,
                             beforeValue : RobotPosition,
                             beforeCoefficient : Double,
                             afterValue : RobotPosition,
                             afterCoefficient : Double)
    {
        animated.robotPosition =
            RobotPosition(neckAngleX = (beforeValue.neckAngleX * beforeCoefficient + afterValue.neckAngleX * afterCoefficient).toFloat(),
                          neckAngleY = (beforeValue.neckAngleY * beforeCoefficient + afterValue.neckAngleY * afterCoefficient).toFloat(),
                          neckAngleZ = (beforeValue.neckAngleZ * beforeCoefficient + afterValue.neckAngleZ * afterCoefficient).toFloat(),

                          rightShoulderAngleX = (beforeValue.rightShoulderAngleX * beforeCoefficient + afterValue.rightShoulderAngleX * afterCoefficient).toFloat(),
                          rightShoulderAngleZ = (beforeValue.rightShoulderAngleZ * beforeCoefficient + afterValue.rightShoulderAngleZ * afterCoefficient).toFloat(),

                          rightElbowAngleX = (beforeValue.rightElbowAngleX * beforeCoefficient + afterValue.rightElbowAngleX * afterCoefficient).toFloat(),

                          leftShoulderAngleX = (beforeValue.leftShoulderAngleX * beforeCoefficient + afterValue.leftShoulderAngleX * afterCoefficient).toFloat(),
                          leftShoulderAngleZ = (beforeValue.leftShoulderAngleZ * beforeCoefficient + afterValue.leftShoulderAngleZ * afterCoefficient).toFloat(),

                          leftElbowAngleX = (beforeValue.leftElbowAngleX * beforeCoefficient + afterValue.leftElbowAngleX * afterCoefficient).toFloat(),

                          rightAssAngleX = (beforeValue.rightAssAngleX * beforeCoefficient + afterValue.rightAssAngleX * afterCoefficient).toFloat(),
                          rightAssAngleZ = (beforeValue.rightAssAngleZ * beforeCoefficient + afterValue.rightAssAngleZ * afterCoefficient).toFloat(),

                          rightKneeAngleX = (beforeValue.rightKneeAngleX * beforeCoefficient + afterValue.rightKneeAngleX * afterCoefficient).toFloat(),

                          leftAssAngleX = (beforeValue.leftAssAngleX * beforeCoefficient + afterValue.leftAssAngleX * afterCoefficient).toFloat(),
                          leftAssAngleZ = (beforeValue.leftAssAngleZ * beforeCoefficient + afterValue.leftAssAngleZ * afterCoefficient).toFloat(),

                          leftKneeAngleX = (beforeValue.leftKneeAngleX * beforeCoefficient + afterValue.leftKneeAngleX * afterCoefficient).toFloat())
    }
}
