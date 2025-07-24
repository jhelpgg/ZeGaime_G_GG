package fr.khelp.zegaime.engine3d.animations

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition

class AnimationRobotKeyTime(robot : Robot) : AnimationKeyTime<Robot, RobotPosition>(robot)
{
    override fun getValue(animated : Robot) : RobotPosition =
        animated.robotPosition

    override fun setValue(animated : Robot, value : RobotPosition)
    {
        animated.robotPosition = value
    }

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