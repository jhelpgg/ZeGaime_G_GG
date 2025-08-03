package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.formatk3d.data.NodeData
import fr.khelp.zegaime.formatk3d.data.NodeLimitData
import fr.khelp.zegaime.formatk3d.data.NodePositionData
import fr.khelp.zegaime.formatk3d.data.NodeType

/**
 * Generic node creator
 * @param name Node name
 */
open class NodeCreator(val name : String)
{
    /** Node position */
    var position : NodePositionData = NodePositionData(NodePosition())
    /** Node position and rotation limits */
    var limits : NodeLimitData =
        NodeLimitData(limitMinX = Float.NEGATIVE_INFINITY, limitMaxX = Float.POSITIVE_INFINITY,
                      limitMinY = Float.NEGATIVE_INFINITY, limitMaxY = Float.POSITIVE_INFINITY,
                      limitMinZ = Float.NEGATIVE_INFINITY, limitMaxZ = Float.POSITIVE_INFINITY,
                      limitMinAngleX = Float.NEGATIVE_INFINITY, limitMaxAngleX = Float.POSITIVE_INFINITY,
                      limitMinAngleY = Float.NEGATIVE_INFINITY, limitMaxAngleY = Float.POSITIVE_INFINITY,
                      limitMinAngleZ = Float.NEGATIVE_INFINITY, limitMaxAngleZ = Float.POSITIVE_INFINITY,
                      limitMinScaleX = Float.NEGATIVE_INFINITY, limitMaxScaleX = Float.POSITIVE_INFINITY,
                      limitMinScaleY = Float.NEGATIVE_INFINITY, limitMaxScaleY = Float.POSITIVE_INFINITY,
                      limitMinScaleZ = Float.NEGATIVE_INFINITY, limitMaxScaleZ = Float.POSITIVE_INFINITY)

    /** Children creator */
    protected val nodeChildrenCreator = NodeChildrenCreator()

    /**
     * Define node children
     * @param children Children creation lambda
     */
    fun children(children : NodeChildrenCreator.() -> Unit)
    {
        this.nodeChildrenCreator.nodes.clear()
        this.nodeChildrenCreator.children()
    }

    /**
     * Convert to node data for save
     * @return Node data for save
     */
    open operator fun invoke() : NodeData =
        NodeData(name = this.name, nodeType = NodeType.NODE,
                 positionData = this.position, limitData = this.limits,
                 children = this.nodeChildrenCreator.nodes)
}