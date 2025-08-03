package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.scene.Node
import kotlinx.serialization.Serializable

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
