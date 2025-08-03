package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.Node
import kotlinx.serialization.Serializable

/**
 * Node limits data for save
 * @param limitMinX Minimum X
 * @param limitMaxX Maximum X
 * @param limitMinY Minimum Y
 * @param limitMaxY Maximum Y
 * @param limitMinZ Minimum Z
 * @param limitMaxZ Maximum Z
 * @param limitMinAngleX Minimum angle X
 * @param limitMaxAngleX Maximum angle X
 * @param limitMinAngleY Minimum angle Y
 * @param limitMaxAngleY Maximum angle Y
 * @param limitMinAngleZ Minimum angle Z
 * @param limitMaxAngleZ Maximum angle Z
 * @param limitMinScaleX Minimum scale X
 * @param limitMaxScaleX Maximum scale X
 * @param limitMinScaleY Minimum scale Y
 * @param limitMaxScaleY Maximum scale Y
 * @param limitMinScaleZ Minimum scale Z
 * @param limitMaxScaleZ Maximum scale Z
 */
@Serializable
data class NodeLimitData(val limitMinX : Float, val limitMaxX : Float,
                         val limitMinY : Float, val limitMaxY : Float,
                         val limitMinZ : Float, val limitMaxZ : Float,
    //
                         val limitMinAngleX : Float, val limitMaxAngleX : Float,
                         val limitMinAngleY : Float, val limitMaxAngleY : Float,
                         val limitMinAngleZ : Float, val limitMaxAngleZ : Float,
    //
                         val limitMinScaleX : Float, val limitMaxScaleX : Float,
                         val limitMinScaleY : Float, val limitMaxScaleY : Float,
                         val limitMinScaleZ : Float, val limitMaxScaleZ : Float)
{
    /**
     * Create a new instance of NodeLimitData from a Node
     * @param node Node to copy limits from
     */
    constructor(node : Node) : this(limitMinX = node.limitMinX, limitMaxX = node.limitMaxX,
                                    limitMinY = node.limitMinY, limitMaxY = node.limitMaxY,
                                    limitMinZ = node.limitMinZ, limitMaxZ = node.limitMaxZ,
        //
                                    limitMinAngleX = node.limitMinAngleX, limitMaxAngleX = node.limitMaxAngleX,
                                    limitMinAngleY = node.limitMinAngleY, limitMaxAngleY = node.limitMaxAngleY,
                                    limitMinAngleZ = node.limitMinAngleZ, limitMaxAngleZ = node.limitMaxAngleZ,
        //
                                    limitMinScaleX = node.limitMinScaleX, limitMaxScaleX = node.limitMaxScaleX,
                                    limitMinScaleY = node.limitMinScaleY, limitMaxScaleY = node.limitMaxScaleY,
                                    limitMinScaleZ = node.limitMinScaleZ, limitMaxScaleZ = node.limitMaxScaleZ)

    /**
     * Set the limits to a node
     * @param node Node to set limits to
     */
    fun setTo(node : Node)
    {
        node.limitX(this.limitMinX, this.limitMaxX)
        node.limitY(this.limitMinY, this.limitMaxY)
        node.limitZ(this.limitMinZ, this.limitMaxZ)
        //
        node.limitAngleX(this.limitMinAngleX, this.limitMaxAngleX)
        node.limitAngleY(this.limitMinAngleY, this.limitMaxAngleY)
        node.limitAngleZ(this.limitMinAngleZ, this.limitMaxAngleZ)
        //
        node.limitScaleX(this.limitMinScaleX, this.limitMaxScaleX)
        node.limitScaleY(this.limitMinScaleY, this.limitMaxScaleY)
        node.limitScaleZ(this.limitMinScaleZ, this.limitMaxScaleZ)
    }
}
