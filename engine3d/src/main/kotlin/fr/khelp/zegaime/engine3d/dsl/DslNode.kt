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

/**
 * Adds a child node to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.node("childNode") {
 *     // ...
 * }
 * ```
 *
 * @param nodeId The ID of the child node.
 * @param nodeCreate The lambda function to create the child node.
 * @return The created child node.
 */
@NodeDSL
fun Node.node(nodeId : String, nodeCreate : Node.() -> Unit) : Node
{
    val node = Node(nodeId)
    node.nodeCreate()
    this.addChild(node)
    return node
}

/**
 * Adds a 3D object to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.object3D("myObject") {
 *     // ...
 * }
 * ```
 *
 * @param objectId The ID of the 3D object.
 * @param objectCreate The lambda function to create the 3D object.
 * @return The created 3D object.
 */
@NodeDSL
fun Node.object3D(objectId : String, objectCreate : Object3DCreator.() -> Unit) : Object3D
{
    val object3DCreator = Object3DCreator(objectId)
    object3DCreator.objectCreate()
    val object3D = object3DCreator.object3D
    this.addChild(object3D)
    return object3D
}

/**
 * Adds a plane to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.plane("myPlane") {
 *     // ...
 * }
 * ```
 *
 * @param planeID The ID of the plane.
 * @param planeCreate The lambda function to create the plane.
 * @return The created plane.
 */
@NodeDSL
fun Node.plane(planeID : String, planeCreate : PlaneCreator.() -> Unit) : Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.planeCreate()
    val plane = planeCreator()
    this.addChild(plane)
    return plane
}

/**
 * Adds a box to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.box("myBox") {
 *     // ...
 * }
 * ```
 *
 * @param boxID The ID of the box.
 * @param create The lambda function to create the box.
 * @return The created box.
 */
@NodeDSL
fun Node.box(boxID : String, create : BoxCreator.() -> Unit) : Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    val box = boxCreator()
    this.addChild(box)
    return box
}

/**
 * Adds a sphere to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.sphere("mySphere") {
 *     // ...
 * }
 * ```
 *
 * @param sphereID The ID of the sphere.
 * @param sphereCreate The lambda function to create the sphere.
 * @return The created sphere.
 */
@NodeDSL
fun Node.sphere(sphereID : String, sphereCreate : SphereCreator.() -> Unit) : Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.sphereCreate()
    val sphere = sphereCreator()
    this.addChild(sphere)
    return sphere
}

/**
 * Adds a revolution object to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.revolution("myRevolution") {
 *     // ...
 * }
 * ```
 *
 * @param revolutionID The ID of the revolution object.
 * @param revolutionCreate The lambda function to create the revolution object.
 * @return The created revolution object.
 */
@NodeDSL
fun Node.revolution(revolutionID : String, revolutionCreate : RevolutionCreator.() -> Unit) : Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(revolutionID))
    revolutionCreator.revolutionCreate()
    val revolution = revolutionCreator()
    this.addChild(revolution)
    return revolution
}

/**
 * Adds a dice to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.dice("myDice") {
 *     // ...
 * }
 * ```
 *
 * @param diceID The ID of the dice.
 * @param diceCreate The lambda function to create the dice.
 * @return The created dice.
 */
@NodeDSL
fun Node.dice(diceID : String, diceCreate : DiceCreator.() -> Unit) : Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.diceCreate()
    val dice = diceCreator()
    this.addChild(dice)
    return dice
}

/**
 * Adds a sword to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.sword("mySword") {
 *     // ...
 * }
 * ```
 *
 * @param swordID The ID of the sword.
 * @param create The lambda function to create the sword.
 * @return The created sword.
 */
@NodeDSL
fun Node.sword(swordID : String, create : SwordCreator.() -> Unit) : Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    val sword = swordCreator()
    this.addChild(sword)
    return sword
}

/**
 * Adds a robot to this node using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myNode.robot("myRobot") {
 *     // ...
 * }
 * ```
 *
 * @param robotID The ID of the robot.
 * @param create The lambda function to create the robot.
 * @return The created robot.
 */
@NodeDSL
fun Node.robot(robotID : String, create : RobotCreator.() -> Unit) : Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    val robot = robotCreator.robot
    this.addChild(robot)
    return robot
}
