package fr.khelp.zegaime.formatk3d.filler

import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.NodeWithMaterial
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.scene.ObjectClone
import fr.khelp.zegaime.engine3d.scene.Scene
import fr.khelp.zegaime.engine3d.scene.prebuilt.Box
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Dice
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Sword
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.engine3d.utils.position
import fr.khelp.zegaime.formatk3d.data.BoxData
import fr.khelp.zegaime.formatk3d.data.DiceData
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.NodeWithMaterialData
import fr.khelp.zegaime.formatk3d.data.ObjectCloneData
import fr.khelp.zegaime.formatk3d.data.ObjectData
import fr.khelp.zegaime.formatk3d.data.PlaneData
import fr.khelp.zegaime.formatk3d.data.RevolutionData
import fr.khelp.zegaime.formatk3d.data.RobotData
import fr.khelp.zegaime.formatk3d.data.SceneData
import fr.khelp.zegaime.formatk3d.data.SphereData
import fr.khelp.zegaime.formatk3d.data.SwordData
import fr.khelp.zegaime.images.color.argb
import fr.khelp.zegaime.resources.Resources
import fr.khelp.zegaime.utils.source.DirectorySource
import java.io.File

/**
 * Fills a scene from a scene description
 *
 * @param directory Scene description directory
 * @param sceneData Scene description to use
 */
fun Scene.fill(directory : File, sceneData : SceneData)
{
    val sceneFiller = SceneFiller(this, directory, sceneData)
    sceneFiller.fill()
}

/**
 * Fills a scene from a scene description
 *
 * @property scene Scene to fill
 * @param directory Scene directory
 * @property sceneData Scene data to use
 */
private class SceneFiller(val scene : Scene, directory : File, val sceneData : SceneData)
{
    /** Resources to find files like images, GIFs, videos, sounds, ... */
    private val resources = Resources(DirectorySource(directory))

    /** Materials map */
    private val materials = HashMap<String, Material>()

    /**
     * Fill the scene
     */
    fun fill()
    {
        this.resolveMaterials()

        this.scene.backgroundColor = this.sceneData.backgroundColor.color
        this.scene.backgroundTexture = this.sceneData.backgroundTexture?.texture(this.resources)
        this.scene.root.position(this.sceneData.position.nodePosition)
        this.sceneData.limits.setTo(this.scene.root)

        this.scene.root.removeAllChildren()
        this.fillNodeChildren(this.scene.root, this.sceneData.nodes)
    }

    /**
     * Resolve materials.
     * That is to say, transforms material information to real materials
     */
    private fun resolveMaterials()
    {
        for ((name, materialData) in this.sceneData.materialsMap.materials)
        {
            this.materials[name] = materialData.material(this.resources)
        }
    }

    /**
     * Fills all children's node
     *
     * @param nodeParent Node to fill its children
     * @param children Children to give to the node
     */
    private fun fillNodeChildren(nodeParent : Node, children : List<NodeData>)
    {
        for (child in children)
        {
            nodeParent.addChild(this.createNode(child))
        }
    }

    /**
     * Creates a node from node data
     *
     * @param nodeData Node data to parse
     *
     * @return Created node
     */
    private fun createNode(nodeData : NodeData) : Node
    {
        val node =
            when (nodeData.nodeType)
            {
                NodeType.NODE  -> Node(nodeData.name)

                NodeType.DICE  -> this.createDice(nodeData.name, nodeData.diceData!!)

                NodeType.SWORD -> this.createSword(nodeData.name, nodeData.swordData!!)

                NodeType.ROBOT -> this.createRobot(nodeData.name, nodeData.robotData!!)

                else           -> this.createNodeWithMaterial(nodeData)
            }

        this.fill(node, nodeData)
        return node
    }

    /**
     * Fills node common information from node data
     *
     * @param node Node to fill
     * @param nodeData Node data to parse
     */
    private fun fill(node : Node, nodeData : NodeData)
    {
        node.position(nodeData.positionData.nodePosition)
        nodeData.limitData.setTo(node)
        this.fillNodeChildren(node, nodeData.children)
    }

    /**
     * Creates a dice from dice data
     *
     * @param name Dice name
     * @param diceData Dice data to parse
     *
     * @return Created dice
     */
    private fun createDice(name : String, diceData : DiceData) : Dice
    {
        val dice = Dice(name)
        dice.color(diceData.color.color)
        return dice
    }

    /**
     * Creates a sword from sword data
     *
     * @param name Sword name
     * @param swordData Sword data to parse
     *
     * @return Created sword
     */
    private fun createSword(name : String, swordData : SwordData) : Sword
    {
        val sword = Sword(name, swordData.size)

        if (swordData.baseMaterial.isNotEmpty())
        {
            val material = this.materials[swordData.baseMaterial]

            if (material != null)
            {
                sword.baseMaterial.copy(material)
            }
        }

        if (swordData.bladeMaterial.isNotEmpty())
        {
            val material = this.materials[swordData.bladeMaterial]

            if (material != null)
            {
                sword.bladeMaterial.copy(material)
            }
        }

        return sword
    }

    /**
     * Creates a robot from robot data
     *
     * @param name Robot name
     * @param robotData Robot data to parse
     *
     * @return Created robot
     */
    private fun createRobot(name : String, robotData : RobotData) : Robot
    {
        val robot = Robot(name)
        robot.robotPosition = robotData.robotPosition.robotPosition
        robot.leftArmColor = robotData.leftArmColor.color
        robot.rightArmColor = robotData.rightArmColor.color
        robot.leftLegColor = robotData.leftLegColor.color
        robot.rightLegColor = robotData.rightLegColor.color

        if (robotData.materialBody.isNotEmpty())
        {
            val material = this.materials[robotData.materialBody]

            if (material != null)
            {
                robot.materialBody.copy(material)
            }
        }

        robot.head.leftEye = robotData.head.leftEye
        robot.head.rightEye = robotData.head.rightEye
        robot.head.mouth = robotData.head.mouth
        robot.head.hairColor = robotData.head.hairColor.argb

        return robot
    }

    /**
     * Creates a node with material from node data
     *
     * @param nodeData Node data to parse
     *
     * @return Created node with material
     */
    private fun createNodeWithMaterial(nodeData : NodeData) : NodeWithMaterial
    {
        val nodeWithMaterial =
            when (nodeData.nodeType)
            {
                NodeType.OBJECT     -> this.createObject3D(nodeData.name, nodeData.objectData!!)

                NodeType.CLONE      -> this.createObjectClone(nodeData.name, nodeData.objectCloneData!!)

                NodeType.PLANE      -> this.createPlane(nodeData.name, nodeData.planeData!!)

                NodeType.BOX        -> this.createBox(nodeData.name, nodeData.boxData!!)

                NodeType.SPHERE     -> this.createSphere(nodeData.name, nodeData.sphereData!!)

                NodeType.REVOLUTION -> this.createRevolution(nodeData.name, nodeData.revolutionData!!)

                else                -> throw RuntimeException("${nodeData.nodeType} is not a NodeWithMaterial")
            }

        this.fill(nodeWithMaterial, nodeData.nodeWithMaterialData!!)
        return nodeWithMaterial
    }

    /**
     * Fills common information of node with material
     *
     * @param nodeWithMaterial Node with material to fill
     * @param nodeWithMaterialData Node with material information to ues
     */
    private fun fill(nodeWithMaterial : NodeWithMaterial, nodeWithMaterialData : NodeWithMaterialData)
    {
        if (nodeWithMaterialData.material.isNotEmpty())
        {
            val material = this.materials[nodeWithMaterialData.material]

            if (material != null)
            {
                nodeWithMaterial.material = material
            }
        }

        if (nodeWithMaterialData.materialForSelection.isNotEmpty())
        {
            val material = this.materials[nodeWithMaterialData.materialForSelection]

            if (material != null)
            {
                nodeWithMaterial.materialForSelection = material
            }
        }

        nodeWithMaterial.twoSidedRule = nodeWithMaterialData.twoSidedRule
    }

    /**
     * Creates a 3D object from 3D object data
     *
     * @param name 3D object name
     * @param objectData 3D object data to parse
     *
     * @return Created 3D object
     */
    private fun createObject3D(name : String, objectData : ObjectData) : Object3D
    {
        val object3D = Object3D(name, objectData.mesh.mesh)
        object3D.showWire = objectData.showWire
        object3D.wireColor = objectData.wireColor.color
        return object3D
    }

    /**
     * Creates a clone from clone data
     *
     * @param name Clone name
     * @param objectCloneData Clone data to parse
     *
     * @return Created clone
     */
    private fun createObjectClone(name : String, objectCloneData : ObjectCloneData) : ObjectClone
    {
        val cloned =
            this.scene.findById<Object3D>(objectCloneData.objectReference)
            ?: throw RuntimeException("Object ${objectCloneData.objectReference} not found, be sure it is declare before it is cloned")

        return ObjectClone(name, cloned)
    }

    /**
     * Creates a plane from plane data
     *
     * @param name Plane name
     * @param planeData Plane data to parse
     *
     * @return Created plane
     */
    private fun createPlane(name : String, planeData : PlaneData) : Plane =
        Plane(name, planeData.faceUV.faceUV)

    /**
     * Creates a box from box data
     *
     * @param name Box name
     * @param boxData Box data to parse
     *
     * @return Created box
     */
    private fun createBox(name : String, boxData : BoxData) : Box =
        Box(name, boxData.boxUV.boxUV)

    /**
     * Creates a sphere from node data
     *
     * @param name Sphere name
     * @param sphereData Sphere data to parse
     *
     * @return Created a sphere
     */
    private fun createSphere(name : String, sphereData : SphereData) : Sphere =
        Sphere(id = name,
               slice = sphereData.slice, stack = sphereData.slack,
               multiplierU = sphereData.multiplierU, multiplierV = sphereData.multiplierV)

    /**
     * Creates a revolution from revolution data
     *
     * @param name Revolution name
     * @param revolutionData Revolution data to parse
     *
     * @return Created revolution
     */
    private fun createRevolution(name : String, revolutionData : RevolutionData) : Revolution
    {
        val revolution = Revolution(name)
        revolution.path(precision = revolutionData.precision,
                        angle = revolutionData.angle, rotationPrecision = revolutionData.rotationPrecision,
                        start = revolutionData.start, end = revolutionData.end,
                        multiplierU = revolutionData.multiplierU,
                        path = revolutionData.path.path)
        return revolution
    }
}