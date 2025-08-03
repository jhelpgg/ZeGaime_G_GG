package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData

class NodeChildrenCreator
{
    val nodes = ArrayList<NodeData>()

    fun node(name : String, create : NodeCreator.() -> Unit)
    {
        this.nodeGenric(NodeCreator(name), create)
    }

    fun object3D(name : String, create : ObjectCreator.() -> Unit)
    {
        this.nodeGenric(ObjectCreator(name), create)
    }

    fun objectClone(name : String, create : ObjectCloneCreator.() -> Unit)
    {
        this.nodeGenric(ObjectCloneCreator(name), create)
    }

    fun plane(name : String, create : PlaneCreator.() -> Unit)
    {
        this.nodeGenric(PlaneCreator(name), create)
    }

    fun box(name : String, create : BoxCreator.() -> Unit)
    {
        this.nodeGenric(BoxCreator(name), create)
    }

    fun sphere(name : String, create : SphereCreator.() -> Unit)
    {
        this.nodeGenric(SphereCreator(name), create)
    }

    fun revolution(name : String, create : RevolutionCreator.() -> Unit)
    {
        this.nodeGenric(RevolutionCreator(name), create)
    }

    fun dice(name : String, create : DiceCreator.() -> Unit)
    {
        this.nodeGenric(DiceCreator(name), create)
    }

    fun sword(name : String, create : SwordCreator.() -> Unit)
    {
        this.nodeGenric(SwordCreator(name), create)
    }

    fun robot(name : String, create : RobotCreator.() -> Unit)
    {
        this.nodeGenric(RobotCreator(name), create)
    }

    private fun <NC : NodeCreator> nodeGenric(nodeCreator : NC, create : NC.() -> Unit)
    {
        nodeCreator.create()
        this.nodes.add(nodeCreator())
    }
}