package fr.khelp.zegaime.engine3d.animations

import fr.khelp.zegaime.animations.keytime.AnimationKeyTime
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

class AnimationNodeKeyTime(node : Node) : AnimationKeyTime<Node, NodePosition>(node)
{
    override fun getValue(animated : Node) : NodePosition =
        animated.position

    override fun setValue(animated : Node, value : NodePosition)
    {
        animated.position(value)
    }

    override fun interpolate(animated : Node,
                             beforeValue : NodePosition, beforeCoefficient : Double,
                             afterValue : NodePosition, afterCoefficient : Double)
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