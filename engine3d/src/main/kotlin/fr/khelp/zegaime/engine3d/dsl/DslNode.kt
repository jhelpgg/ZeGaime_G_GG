package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.scene.prebuilt.Box
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Dice
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Sword
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot

@NodeDSL
fun Node.node(nodeId : String, nodeCreate : Node.() -> Unit) : Node
{
    val node = Node(nodeId)
    node.nodeCreate()
    this.addChild(node)
    return node
}

@NodeDSL
fun Node.object3D(objectId : String, objectCreate : Object3DCreator.() -> Unit) : Object3D
{
    val object3DCreator = Object3DCreator(objectId)
    object3DCreator.objectCreate()
    val object3D = object3DCreator.object3D
    this.addChild(object3D)
    return object3D
}

@NodeDSL
fun Node.plane(planeID : String, planeCreate : PlaneCreator.() -> Unit) : Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.planeCreate()
    val plane = planeCreator()
    this.addChild(plane)
    return plane
}

@NodeDSL
fun Node.box(boxID : String, create : BoxCreator.() -> Unit) : Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    val box = boxCreator()
    this.addChild(box)
    return box
}

@NodeDSL
fun Node.sphere(sphereID : String, sphereCreate : SphereCreator.() -> Unit) : Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.sphereCreate()
    val sphere = sphereCreator()
    this.addChild(sphere)
    return sphere
}

@NodeDSL
fun Node.revolution(revolutionID : String, revolutionCreate : RevolutionCreator.() -> Unit) : Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(revolutionID))
    revolutionCreator.revolutionCreate()
    val revolution = revolutionCreator()
    this.addChild(revolution)
    return revolution
}

@NodeDSL
fun Node.dice(diceID : String, diceCreate : DiceCreator.() -> Unit) : Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.diceCreate()
    val dice = diceCreator()
    this.addChild(dice)
    return dice
}

@NodeDSL
fun Node.sword(swordID : String, create : SwordCreator.() -> Unit) : Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    val sword = swordCreator()
    this.addChild(sword)
    return sword
}

@NodeDSL
fun Node.robot(robotID : String, create : RobotCreator.() -> Unit) : Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    val robot = robotCreator.robot
    this.addChild(robot)
    return robot
}