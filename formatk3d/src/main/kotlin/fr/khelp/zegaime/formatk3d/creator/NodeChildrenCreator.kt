package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData

/**
 * Creator for node children
 */
class NodeChildrenCreator
{
    /** List of children */
    internal val nodes = ArrayList<NodeData>()

    /**
     * Add a node
     *
     * @param name Node name
     * @param create How to create the node
     */
    fun node(name : String, create : NodeCreator.() -> Unit)
    {
        this.nodeGenric(NodeCreator(name), create)
    }

    /**
     * Add an object 3D
     *
     * @param name Object name
     * @param create How to create the object
     */
    fun object3D(name : String, create : ObjectCreator.() -> Unit)
    {
        this.nodeGenric(ObjectCreator(name), create)
    }

    /**
     * Add a clone of an object
     *
     * @param name Clone name
     * @param create How to create the clone
     */
    fun objectClone(name : String, create : ObjectCloneCreator.() -> Unit)
    {
        this.nodeGenric(ObjectCloneCreator(name), create)
    }

    /**
     * Add a plane
     *
     * @param name Plane name
     * @param create How to create the plane
     */
    fun plane(name : String, create : PlaneCreator.() -> Unit)
    {
        this.nodeGenric(PlaneCreator(name), create)
    }

    /**
     * Add a box
     *
     * @param name Box name
     * @param create How to create the box
     */
    fun box(name : String, create : BoxCreator.() -> Unit)
    {
        this.nodeGenric(BoxCreator(name), create)
    }

    /**
     * Add a sphere
     *
     * @param name Sphere name
     * @param create How to create the sphere
     */
    fun sphere(name : String, create : SphereCreator.() -> Unit)
    {
        this.nodeGenric(SphereCreator(name), create)
    }

    /**
     * Add a revolution
     *
     * @param name Revolution name
     * @param create How to create the revolution
     */
    fun revolution(name : String, create : RevolutionCreator.() -> Unit)
    {
        this.nodeGenric(RevolutionCreator(name), create)
    }

    /**
     * Add a dice
     *
     * @param name Dice name
     * @param create How to create the dice
     */
    fun dice(name : String, create : DiceCreator.() -> Unit)
    {
        this.nodeGenric(DiceCreator(name), create)
    }

    /**
     * Add a sword
     *
     * @param name Sword name
     * @param create How to create the sword
     */
    fun sword(name : String, create : SwordCreator.() -> Unit)
    {
        this.nodeGenric(SwordCreator(name), create)
    }

    /**
     * Add a robot
     *
     * @param name Robot name
     * @param create How to create the robot
     */
    fun robot(name : String, create : RobotCreator.() -> Unit)
    {
        this.nodeGenric(RobotCreator(name), create)
    }

    /**
     * Generic way to add a node
     *
     * @param NC Node creator type
     * @param nodeCreator Node creator to use
     * @param create How to create the node
     */
    private fun <NC : NodeCreator> nodeGenric(nodeCreator : NC, create : NC.() -> Unit)
    {
        nodeCreator.create()
        this.nodes.add(nodeCreator())
    }
}