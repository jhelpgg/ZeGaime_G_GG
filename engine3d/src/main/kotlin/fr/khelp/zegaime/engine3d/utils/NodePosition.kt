package fr.khelp.zegaime.engine3d.utils

import org.lwjgl.opengl.GL11

data class NodePosition(val x : Float = 0f, val y : Float = 0f, val z : Float = 0f,
                        val angleX : Float = 0f, val angleY : Float = 0f, val angleZ : Float = 0f,
                        val scaleX : Float = 1f, val scaleY : Float = 1f, val scaleZ : Float = 1f)
{
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
     * Multiply position by a factor
     */
    operator fun times(number : Number) : NodePosition =
        this.times(number.toFloat())

    internal fun locate()
    {
        GL11.glTranslatef(this.x, this.y, this.z)
        GL11.glRotatef(this.angleX, 1f, 0f, 0f)
        GL11.glRotatef(this.angleY, 0f, 1f, 0f)
        GL11.glRotatef(this.angleZ, 0f, 0f, 1f)
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ)
    }
}
