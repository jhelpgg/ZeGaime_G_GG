package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.animations.dsl.AnimationDSL
import fr.khelp.zegaime.animations.dsl.DslAnimationKeyFrame
import fr.khelp.zegaime.engine3d.animations.AnimationNodeKeyTime
import fr.khelp.zegaime.engine3d.animations.AnimationRobotKeyTime
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.RobotPosition
import fr.khelp.zegaime.engine3d.utils.NodePosition

@AnimationDSL
fun animationNode(node:Node, create:AnimationNodeCreator.()->Unit) : AnimationNodeKeyTime
{
    val animationNodeCreator = AnimationNodeCreator(node)
    animationNodeCreator.create()
    return animationNodeCreator.animation
}

@AnimationDSL
fun animationRobot(robot : Robot, create:AnimationRobotCreator.()->Unit) : AnimationRobotKeyTime
{
    val animationNodeCreator = AnimationRobotCreator(robot)
    animationNodeCreator.create()
    return animationNodeCreator.animation
}

@AnimationDSL
class AnimationNodeCreator(node:Node) : DslAnimationKeyFrame<Node, NodePosition>(AnimationNodeKeyTime(node))
{
    internal val animation:  AnimationNodeKeyTime = this.animationKeyTime as AnimationNodeKeyTime
}

@AnimationDSL
class AnimationRobotCreator(robot:Robot) : DslAnimationKeyFrame<Robot, RobotPosition>(AnimationRobotKeyTime(robot))
{
    internal val animation:  AnimationRobotKeyTime = this.animationKeyTime as AnimationRobotKeyTime
}