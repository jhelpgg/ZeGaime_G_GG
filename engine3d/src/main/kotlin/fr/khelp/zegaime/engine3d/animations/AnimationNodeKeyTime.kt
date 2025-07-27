package fr.khelp.zegaime.engine3d.animations

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * An animation of a [Node] based on key times.
 *
 * This animation interpolates the position, rotation and scale of a node over time.
 *
 * **Creation example:**
 * ```kotlin
 * val animation = AnimationNodeKeyTime(myNode)
 * animation.addKeyTimeValue(0, NodePosition(x = 0f))
 * animation.addKeyTimeValue(1000, NodePosition(x = 10f))
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * AnimationManager.play(animation)
 * ```
 *
 * @param node The node to animate.
 * @constructor Creates a new node key time animation.
 */
class AnimationNodeKeyTime(node: Node) : AnimationKeyTime<Node, NodePosition>(node)
{
    /**
     * Gets the current position of the node.
     *
     * @param animated The node.
     * @return The current position of the node.
     */
    override fun getValue(animated: Node): NodePosition =
        animated.position

    /**
     * Sets the position of the node.
     *
     * @param animated The node.
     * @param value The new position of the node.
     */
    override fun setValue(animated: Node, value: NodePosition)
    {
        animated.position(value)
    }

    /**
     * Interpolates the position of the node between two keyframes.
     *
     * @param animated The node.
     * @param beforeValue The position of the previous keyframe.
     * @param beforeCoefficient The coefficient of the previous keyframe.
     * @param afterValue The position of the next keyframe.
     * @param afterCoefficient The coefficient of the next keyframe.
     */
    override fun interpolate(animated: Node,
                             beforeValue: NodePosition, beforeCoefficient: Double,
                             afterValue: NodePosition, afterCoefficient: Double)
    {
        animated.position(NodePosition(x = (beforeValue.x * beforeCoefficient + afterValue.x * afterCoefficient).toFloat(),
                                       y = (beforeValue.y * beforeCoefficient + afterValue.y * afterCoefficient).toFloat(),
                                       z = (beforeValue.z * beforeCoefficient + afterValue.z * afterCoefficient).toFloat(),

                                       angleX = (beforeValue.angleX * beforeCoefficient + afterValue.angleX * afterCoefficient).toFloat(),
                                       angleY = (beforeValue.angleY * beforeCoefficient + afterValue.angleY * afterCoefficient).toFloat(),
                                       angleZ = (beforeValue.angleZ * beforeCoefficient + afterValue.angleZ * afterCoefficient).toFloat(),

                                       scaleX = (beforeValue.scaleX * beforeCoefficient + afterValue.scaleX * afterCoefficient).toFloat(),
                                       scaleY = (beforeValue.scaleY * beforeCoefficient + afterValue.scaleY * afterCoefficient).toFloat(),
                                       scaleZ = (beforeValue.scaleZ * beforeCoefficient + afterValue.scaleZ * afterCoefficient).toFloat()))
    }
}
