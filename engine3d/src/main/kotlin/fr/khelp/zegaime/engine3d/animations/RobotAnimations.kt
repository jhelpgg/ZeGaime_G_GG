package fr.khelp.zegaime.engine3d.animations

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.interpolation.Interpolation
import fr.khelp.zegaime.animations.interpolation.InterpolationLinear
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import kotlin.math.max

/**
 * Create animation that make the robot to return at it start position
 * @param numberMilliseconds Number milliseconds to return to start position
 * @param interpolation Interpolation to use
 * @return Created animation
 */
fun Robot.startPosition(numberMilliseconds : Long = 32L, interpolation : Interpolation = InterpolationLinear) : Animation
{
    val animation = AnimationRobotKeyTime(this)
    animation.addKeyTimeValue(max(32L, numberMilliseconds), RobotPosition(), interpolation)
    return animation
}

/**
 * Create animation that make robot to walk
 * @param numberMillisecondsPerStep Number milliseconds to make a step
 * @param numberStep Number step to make
 * @return Created animation
 */
fun Robot.walk(numberMillisecondsPerStep : Long = 64L, numberStep : Int = 1) : Animation
{
    val time = max(64L, numberMillisecondsPerStep);
    val semiTime = time shr 1
    val stepMax = max(1, numberStep)
    val animation = AnimationRobotKeyTime(this)
    val robotPosition1 = RobotPosition(rightShoulderAngleX = 144f, leftShoulderAngleX = 216f,
                                       rightAssAngleX = 216f, leftAssAngleX = 144f)
    val robotPosition2 = RobotPosition(rightShoulderAngleX = 216f, leftShoulderAngleX = 144f,
                                       rightAssAngleX = 144f, leftAssAngleX = 216f)
    var keyTime = semiTime
    animation.addKeyTimeValue(keyTime, robotPosition1)
    var step = 1

    while (step < stepMax)
    {
        keyTime += time
        animation.addKeyTimeValue(keyTime, if ((step and 1) == 0) robotPosition1 else robotPosition2)
        step++
    }

    keyTime += semiTime
    animation.addKeyTimeValue(keyTime, RobotPosition())
    return animation
}

/**
 * Create animation that make robot to run
 * @param numberMillisecondsPerStep Number milliseconds to make a step
 * @param numberStep Number step to make
 * @return Created animation
 */
fun Robot.run(numberMillisecondsPerStep : Long = 32L, numberStep : Int = 1) : Animation
{
    val frame = max(32L, numberMillisecondsPerStep);
    val semiFrame = frame shr 1
    val portion = 5
    val part = frame / portion
    val left = frame - part
    val angle = (36 * portion) / (portion + 1)
    val stepMax = max(1, numberStep)
    val animation = AnimationRobotKeyTime(this)
    val robotPosition1 = RobotPosition(rightShoulderAngleX = 180f - angle, leftShoulderAngleX = 180f + angle,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 180f + angle, leftAssAngleX = 90f,
                                       rightKneeAngleX = 0f, leftKneeAngleX = 90f)
    val robotPosition2 = RobotPosition(rightShoulderAngleX = 144f, leftShoulderAngleX = 216f,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 216f, leftAssAngleX = 144f)
    val robotPosition3 = RobotPosition(rightShoulderAngleX = 180f + angle, leftShoulderAngleX = 180f - angle,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 90f, leftAssAngleX = 180f + angle,
                                       rightKneeAngleX = 90f, leftKneeAngleX = 0f)
    val robotPosition4 = RobotPosition(rightShoulderAngleX = 216f, leftShoulderAngleX = 144f,
                                       rightElbowAngleX = -90f, leftElbowAngleX = -90f,
                                       rightAssAngleX = 144f, leftAssAngleX = 216f)
    var key = semiFrame
    animation.addKeyTimeValue(key, robotPosition1)
    var step = 1

    while (step < stepMax)
    {
        key += left
        animation.addKeyTimeValue(key, if ((step and 1) == 0) robotPosition1 else robotPosition3)
        key += part
        animation.addKeyTimeValue(key, if ((step and 1) == 0) robotPosition2 else robotPosition4)
        step++
    }

    key += semiFrame
    animation.addKeyTimeValue(key, RobotPosition())
    return animation
}
