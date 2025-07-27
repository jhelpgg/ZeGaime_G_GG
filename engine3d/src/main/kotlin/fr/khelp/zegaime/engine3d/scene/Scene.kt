package fr.khelp.zegaime.engine3d.scene

import fr.khelp.zegaime.engine3d.render.BLACK
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.scene.prebuilt.FaceUV
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.utils.NodeComparatorOrderZ
import java.util.Stack
import org.lwjgl.opengl.GL11

class Scene
{
    companion object
    {
        const val ROOT_ID = "-<([ROOT])>-"
    }

    private var planeBackground = Plane("PlaneBackground")
    private var lastBackgroundTextureWidth = 0
    private var lastBackgroundTextureHeight = 0
    var backgroundTexture : Texture? = null
    var backgroundColor : Color4f = BLACK
    val root : Node = Node(Scene.ROOT_ID)

    init
    {
        this.root.z = -5f
    }

    fun <N : Node> findById(id : String) : N? = this.root.findById(id)

    internal fun drawBackground(width : Int, height : Int)
    {
        this.backgroundColor.glColor4fBackground()
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        this.backgroundTexture?.let { texture ->
            if (this.lastBackgroundTextureWidth != texture.width || this.lastBackgroundTextureHeight != texture.height)
            {
                this.lastBackgroundTextureWidth = texture.width
                this.lastBackgroundTextureHeight = texture.height
                val faceUV = FaceUV(0f, width.toFloat() / texture.width.toFloat(),
                                    0f, height.toFloat() / texture.height.toFloat())
                this.planeBackground = Plane("PlaneBackground", faceUV)
            }

            this.planeBackground.material.settingAsFor2D()
            this.planeBackground.material.textureDiffuse = texture

            when
            {
                width == height -> Unit
                width > height  -> this.planeBackground.scaleX = width.toFloat() / height.toFloat()
                else            -> this.planeBackground.scaleY = height.toFloat() / width.toFloat()
            }

            this.planeBackground.z = -1.2f
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glPushMatrix()
            this.planeBackground.matrixRootToMe()
            this.planeBackground.renderSpecific()
            GL11.glPopMatrix()
        }
    }

    internal fun render()
    {
        val stack = Stack<Node>()
        stack.push(this.root)
        val nodes = ArrayList<Node>()

        while (!stack.isEmpty())
        {
            val node = stack.pop()

            if (node.visible)
            {
                if (node.countInRender)
                {
                    node.zOrder = node.reverseProjectionWithRoot(node.center).z
                    nodes.add(node)
                }

                for (child in node)
                {
                    stack.push(child)
                }
            }
        }

        nodes.sortWith(NodeComparatorOrderZ)

        for (node in nodes)
        {
            GL11.glPushMatrix()
            node.matrixRootToMe()
            node.renderSpecific()
            GL11.glPopMatrix()
        }
    }
}
