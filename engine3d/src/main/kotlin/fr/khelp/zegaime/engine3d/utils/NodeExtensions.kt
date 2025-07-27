package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.scene.Node

/**
 * The position of the node.
 */
val Node.position: NodePosition
    get() =
        NodePosition(this.x, this.y, this.z,
                     this.angleX, this.angleY, this.angleZ,
                     this.scaleX, this.scaleY, this.scaleZ)

/**
 * Sets the position of the node.
 *
 * @param nodePosition The new position of the node.
 */
fun Node.position(nodePosition: NodePosition)
{
    this.x = nodePosition.x
    this.y = nodePosition.y
    this.z = nodePosition.z

    this.angleX = nodePosition.angleX
    this.angleY = nodePosition.angleY
    this.angleZ = nodePosition.angleZ

    this.scaleX = nodePosition.scaleX
    this.scaleY = nodePosition.scaleY
    this.scaleZ = nodePosition.scaleZ
}
