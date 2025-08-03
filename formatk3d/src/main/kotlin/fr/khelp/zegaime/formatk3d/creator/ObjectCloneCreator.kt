package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeType
import fr.khelp.zegaime.formatk3d.data.ObjectCloneData

/**
 * Object clone creator
 * @param name Clone name
 */
class ObjectCloneCreator(name : String) : NodeWithMaterialCreator(name)
{
    /** Object name to clone */
    var objectReference : String = ""

    /**
     * Convert to node data for save
     * @return Node data for save
     */
    override fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.CLONE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes,
                 nodeWithMaterialData = this.nodeWithMaterialData,
                 objectCloneData = ObjectCloneData(this.objectReference))
}