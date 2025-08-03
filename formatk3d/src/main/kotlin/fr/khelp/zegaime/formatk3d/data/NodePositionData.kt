package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.utils.NodePosition
import kotlinx.serialization.Serializable

/**
 * Node position data for save
 * @param x X position
 * @param y Y position
 * @param z Z position
 * @param angleX X angle
 * @param angleY Y angle
 * @param angleZ Z angle
 * @param scaleX X scale
 * @param scaleY Y scale
 * @param scaleZ Z scale
 */
@Serializable
data class NodePositionData(val x : Float, val y : Float, val z : Float,
                            val angleX : Float, val angleY : Float, val angleZ : Float,
                            val scaleX : Float, val scaleY : Float, val scaleZ : Float)
{
    /**
     * Create a new instance of NodePositionData from a NodePosition
     * @param nodePosition NodePosition to copy
     */
    constructor(nodePosition : NodePosition) :
            this(x = nodePosition.x, y = nodePosition.y, z = nodePosition.z,
                 angleX = nodePosition.angleX, angleY = nodePosition.angleY, angleZ = nodePosition.angleZ,
                 scaleX = nodePosition.scaleX, scaleY = nodePosition.scaleY, scaleZ = nodePosition.scaleZ)

    /**
     * The NodePosition
     */
    val nodePosition : NodePosition by lazy {
        NodePosition(x = this.x, y = this.y, z = this.z,
                     angleX = this.angleX, angleY = this.angleY, angleZ = this.angleZ,
                     scaleX = this.scaleX, scaleY = this.scaleY, scaleZ = this.scaleZ)
    }
}