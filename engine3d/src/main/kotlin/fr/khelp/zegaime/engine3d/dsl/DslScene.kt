package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.Window3D
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.scene.Scene
import fr.khelp.zegaime.engine3d.scene.prebuilt.Box
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Dice
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Sword
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot

@SceneDSL
fun Window3D.scene(sceneFill : Scene.() -> Unit)
{
    this.scene.sceneFill()
}

@SceneDSL
fun Scene.node(nodeId : String, nodeCreate : Node.() -> Unit) : Node
{
    val node = Node(nodeId)
    node.nodeCreate()
    this.root.addChild(node)
    return node
}

@SceneDSL
fun Scene.object3D(objectId : String, objectCreate : Object3DCreator.() -> Unit) : Object3D
{
    val object3DCreator = Object3DCreator(objectId)
    object3DCreator.objectCreate()
    val object3D = object3DCreator.object3D
    this.root.addChild(object3D)
    return object3D
}

@SceneDSL
fun Scene.plane(planeID : String, planeCreate : PlaneCreator.() -> Unit) : Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.planeCreate()
    val plane = planeCreator()
    this.root.addChild(plane)
    return plane
}

@SceneDSL
fun Scene.box(boxID : String, create : BoxCreator.() -> Unit) : Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    val box = boxCreator()
    this.root.addChild(box)
    return box
}

@SceneDSL
fun Scene.sphere(sphereID : String, sphereCreate : SphereCreator.() -> Unit) : Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.sphereCreate()
    val sphere = sphereCreator()
    this.root.addChild(sphere)
    return sphere
}

@SceneDSL
fun Scene.revolution(revolutionID : String, revolutionCreate : RevolutionCreator.() -> Unit) : Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(revolutionID))
    revolutionCreator.revolutionCreate()
    val revolution = revolutionCreator()
    this.root.addChild(revolution)
    return revolution
}

@SceneDSL
fun Scene.dice(diceID : String, diceCreate : DiceCreator.() -> Unit) : Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.diceCreate()
    val dice = diceCreator()
    this.root.addChild(dice)
    return dice
}

@SceneDSL
fun Scene.sword(swordID : String, create : SwordCreator.() -> Unit) : Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    val sword = swordCreator()
    this.root.addChild(sword)
    return sword
}

@SceneDSL
fun Scene.robot(robotID : String, create : RobotCreator.() -> Unit) : Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    val robot = robotCreator.robot
    this.root.addChild(robot)
    return robot
}