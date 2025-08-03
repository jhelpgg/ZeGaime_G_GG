package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.SphereData
import kotlin.math.max

class SphereCreator(name : String) : NodeWithMaterialCreator(name)
{
    var slice : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    var stack : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    var multiplierU : Float = 1f

    var multiplierV : Float = 1f

    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.SPHERE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 sphereData = SphereData(slice = this.slice, slack = this.stack,
                                         multiplierU = this.multiplierU, multiplierV = this.multiplierV))
}