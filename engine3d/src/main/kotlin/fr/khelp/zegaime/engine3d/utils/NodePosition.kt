package fr.khelp.zegaime.engine3d.utils

import org.lwjgl.opengl.GL11

/**
 * Represents the position, rotation and scale of a node.
 *
 * @property x The X position.
 * @property y The Y position.
 * @property z The Z position.
 * @property angleX The rotation angle around the X axis.
 * @property angleY The rotation angle around the Y axis.
 * @property angleZ The rotation angle around the Z axis.
 * @property scaleX The scale factor on the X axis.
 * @property scaleY The scale factor on the Y axis.
 * @property scaleZ The scale factor on the Z axis.
 * @constructor Creates a new node position.
 */
data class NodePosition(val x : Float = 0f, val y : Float = 0f, val z : Float = 0f,
                        val angleX : Float = 0f, val angleY : Float = 0f, val angleZ : Float = 0f,
                        val scaleX : Float = 1f, val scaleY : Float = 1f, val scaleZ : Float = 1f)
{
    /**
     * Adds two node positions.
     *
     * @param nodePosition The other node position.
     * @return The sum of the two node positions.
     */
    operator fun plus(nodePosition : NodePosition) : NodePosition =
        NodePosition(x = this.x + nodePosition.x,
                     y = this.y + nodePosition.y,
                     z = this.z + nodePosition.z,

                     angleX = this.angleX + nodePosition.angleX,
                     angleY = this.angleY + nodePosition.angleY,
                     angleZ = this.angleZ + nodePosition.angleZ,

                     scaleX = this.scaleX + nodePosition.scaleX,
                     scaleY = this.scaleY + nodePosition.scaleY,
                     scaleZ = this.scaleZ + nodePosition.scaleZ)

    /**
     * Subtracts two node positions.
     *
     * @param nodePosition The other node position.
     * @return The difference of the two node positions.
     */
    operator fun minus(nodePosition : NodePosition) : NodePosition =
        NodePosition(x = this.x - nodePosition.x,
                     y = this.y - nodePosition.y,
                     z = this.z - nodePosition.z,

                     angleX = this.angleX - nodePosition.angleX,
                     angleY = this.angleY - nodePosition.angleY,
                     angleZ = this.angleZ - nodePosition.angleZ,

                     scaleX = this.scaleX - nodePosition.scaleX,
                     scaleY = this.scaleY - nodePosition.scaleY,
                     scaleZ = this.scaleZ - nodePosition.scaleZ)

    /**
     * Multiplies the node position by a factor.
     *
     * @param times The factor.
     * @return The multiplied node position.
     */
    operator fun times(times : Float) : NodePosition =
        NodePosition(times * this.x,
                     times * this.y,
                     times * this.z,
                     times * this.angleX,
                     times * this.angleY,
                     times * this.angleZ,
                     times * this.scaleX,
                     times * this.scaleY,
                     times * this.scaleZ)

    /**
     * Multiply position by a factor.
     *
     * @param number The factor.
     * @return The multiplied node position.
     */
    operator fun times(number : Number) : NodePosition =
        this.times(number.toFloat())

    /**
     * Locates the node in the scene.
     *
     * For internal use only.
     */
    internal fun locate()
    {
        GL11.glTranslatef(this.x, this.y, this.z)
        GL11.glRotatef(this.angleX, 1f, 0f, 0f)
        GL11.glRotatef(this.angleY, 0f, 1f, 0f)
        GL11.glRotatef(this.angleZ, 0f, 0f, 1f)
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ)
    }
}