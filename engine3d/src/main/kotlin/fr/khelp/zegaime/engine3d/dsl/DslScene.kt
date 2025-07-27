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

/**
 * Fills the scene of the 3D window using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myWindow3D.scene {
 *     // ...
 * }
 * ```
 *
 * @param sceneFill The lambda function to fill the scene.
 */
@SceneDSL
fun Window3D.scene(sceneFill: Scene.() -> Unit)
{
    this.scene.sceneFill()
}

/**
 * Adds a node to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.node("myNode") {
 *     // ...
 * }
 * ```
 *
 * @param nodeId The ID of the node.
 * @param nodeCreate The lambda function to create the node.
 * @return The created node.
 */
@SceneDSL
fun Scene.node(nodeId: String, nodeCreate: Node.() -> Unit): Node
{
    val node = Node(nodeId)
    node.nodeCreate()
    this.root.addChild(node)
    return node
}

/**
 * Adds a 3D object to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.object3D("myObject") {
 *     // ...
 * }
 * ```
 *
 * @param objectId The ID of the 3D object.
 * @param objectCreate The lambda function to create the 3D object.
 * @return The created 3D object.
 */
@SceneDSL
fun Scene.object3D(objectId: String, objectCreate: Object3DCreator.() -> Unit): Object3D
{
    val object3DCreator = Object3DCreator(objectId)
    object3DCreator.objectCreate()
    val object3D = object3DCreator.object3D
    this.root.addChild(object3D)
    return object3D
}

/**
 * Adds a plane to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.plane("myPlane") {
 *     // ...
 * }
 * ```
 *
 * @param planeID The ID of the plane.
 * @param planeCreate The lambda function to create the plane.
 * @return The created plane.
 */
@SceneDSL
fun Scene.plane(planeID: String, planeCreate: PlaneCreator.() -> Unit): Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.planeCreate()
    val plane = planeCreator()
    this.root.addChild(plane)
    return plane
}

/**
 * Adds a box to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.box("myBox") {
 *     // ...
 * }
 * ```
 *
 * @param boxID The ID of the box.
 * @param create The lambda function to create the box.
 * @return The created box.
 */
@SceneDSL
fun Scene.box(boxID: String, create: BoxCreator.() -> Unit): Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    val box = boxCreator()
    this.root.addChild(box)
    return box
}

/**
 * Adds a sphere to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.sphere("mySphere") {
 *     // ...
 * }
 * ```
 *
 * @param sphereID The ID of the sphere.
 * @param sphereCreate The lambda function to create the sphere.
 * @return The created sphere.
 */
@SceneDSL
fun Scene.sphere(sphereID: String, sphereCreate: SphereCreator.() -> Unit): Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.sphereCreate()
    val sphere = sphereCreator()
    this.root.addChild(sphere)
    return sphere
}

/**
 * Adds a revolution object to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.revolution("myRevolution") {
 *     // ...
 * }
 * ```
 *
 * @param revolutionID The ID of the revolution object.
 * @param revolutionCreate The lambda function to create the revolution object.
 * @return The created revolution object.
 */
@SceneDSL
fun Scene.revolution(revolutionID: String, revolutionCreate: RevolutionCreator.() -> Unit): Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(revolutionID))
    revolutionCreator.revolutionCreate()
    val revolution = revolutionCreator()
    this.root.addChild(revolution)
    return revolution
}

/**
 * Adds a dice to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.dice("myDice") {
 *     // ...
 * }
 * ```
 *
 * @param diceID The ID of the dice.
 * @param diceCreate The lambda function to create the dice.
 * @return The created dice.
 */
@SceneDSL
fun Scene.dice(diceID: String, diceCreate: DiceCreator.() -> Unit): Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.diceCreate()
    val dice = diceCreator()
    this.root.addChild(dice)
    return dice
}

/**
 * Adds a sword to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.sword("mySword") {
 *     // ...
 * }
 * ```
 *
 * @param swordID The ID of the sword.
 * @param create The lambda function to create the sword.
 * @return The created sword.
 */
@SceneDSL
fun Scene.sword(swordID: String, create: SwordCreator.() -> Unit): Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    val sword = swordCreator()
    this.root.addChild(sword)
    return sword
}

/**
 * Adds a robot to the scene using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myScene.robot("myRobot") {
 *     // ...
 * }
 * ```
 *
 * @param robotID The ID of the robot.
 * @param create The lambda function to create the robot.
 * @return The created robot.
 */
@SceneDSL
fun Scene.robot(robotID: String, create: RobotCreator.() -> Unit): Robot
{
    val robotCreator = RobotCreator(robotID)
    robotCreator.create()
    val robot = robotCreator.robot
    this.root.addChild(robot)
    return robot
}
