package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.utils.NodePosition
import kotlinx.serialization.Serializable

@Serializable
data class NodePositionData(val x : Float, val y : Float, val z : Float,
                            val angleX : Float, val angleY : Float, val angleZ : Float,
                            val scaleX : Float, val scaleY : Float, val scaleZ : Float)
{
    constructor(nodePosition : NodePosition) :
            this(x = nodePosition.x, y = nodePosition.y, z = nodePosition.z,
                 angleX = nodePosition.angleX, angleY = nodePosition.angleY, angleZ = nodePosition.angleZ,
                 scaleX = nodePosition.scaleX, scaleY = nodePosition.scaleY, scaleZ = nodePosition.scaleZ)

    val nodePosition : NodePosition by lazy {
        NodePosition(x = this.x, y = this.y, z = this.z,
                     angleX = this.angleX, angleY = this.angleY, angleZ = this.angleZ,
                     scaleX = this.scaleX, scaleY = this.scaleY, scaleZ = this.scaleZ)
    }
}