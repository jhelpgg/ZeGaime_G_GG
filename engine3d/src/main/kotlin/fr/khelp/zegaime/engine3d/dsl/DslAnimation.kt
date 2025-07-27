package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.animations.dsl.AnimationDSL
import fr.khelp.zegaime.animations.dsl.DslAnimationKeyFrame
import fr.khelp.zegaime.engine3d.animations.AnimationNodeKeyTime
import fr.khelp.zegaime.engine3d.animations.AnimationRobotKeyTime
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import fr.khelp.zegaime.engine3d.utils.NodePosition

/**
 * Creates a node animation using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val animation = animationNode(myNode) {
 *     NodePosition(x = 0f).at(0L)
 *     NodePosition(x = 10f).at(1000L, InterpolationSine)
 * }
 * ```
 *
 * @param node The node to animate.
 * @param create The lambda function to create the animation.
 * @return The created animation.
 */
@AnimationDSL
fun animationNode(node : Node, create : AnimationNodeCreator.() -> Unit) : AnimationNodeKeyTime
{
    val animationNodeCreator = AnimationNodeCreator(node)
    animationNodeCreator.create()
    return animationNodeCreator.animation
}

/**
 * Creates a robot animation using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val animation = animationRobot(myRobot) {
 *     RobotPosition(neckAngleX = 0f).at(100L)
 *     RobotPosition(neckAngleX = 10f).at(1000L)
 * }
 * ```
 *
 * @param robot The robot to animate.
 * @param create The lambda function to create the animation.
 * @return The created animation.
 */
@AnimationDSL
fun animationRobot(robot : Robot, create : AnimationRobotCreator.() -> Unit) : AnimationRobotKeyTime
{
    val animationNodeCreator = AnimationRobotCreator(robot)
    animationNodeCreator.create()
    return animationNodeCreator.animation
}

/**
 * DSL creator for node animations.
 *
 * @property animation The created animation.
 * @constructor Creates a new node animation creator.
 */
@AnimationDSL
class AnimationNodeCreator(node : Node) : DslAnimationKeyFrame<Node, NodePosition>(AnimationNodeKeyTime(node))
{
    /**
     * The created animation.
     */
    internal val animation : AnimationNodeKeyTime = this.animationKeyTime as AnimationNodeKeyTime
}

/**
 * DSL creator for robot animations.
 *
 * @property animation The created animation.
 * @constructor Creates a new robot animation creator.
 */
@AnimationDSL
class AnimationRobotCreator(robot : Robot) : DslAnimationKeyFrame<Robot, RobotPosition>(AnimationRobotKeyTime(robot))
{
    /**
     * The created animation.
     */
    internal val animation : AnimationRobotKeyTime = this.animationKeyTime as AnimationRobotKeyTime
}
