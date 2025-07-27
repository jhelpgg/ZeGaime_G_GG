package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegaime.engine3d.utils.transferByte
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.extensions.rgba
import java.util.concurrent.atomic.AtomicBoolean
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack

/**
 * Represents a texture.
 *
 * A texture is an image that can be applied to a 3D object.
 *
 * **Creation example**
 * ```kotlin
 * val gameImage = GameImage.load("myImage.png")
 * val texture = Texture(gameImage)
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * myMaterial.textureDiffuse = texture
 * ```
 *
 * @property gameImage The game image of the texture.
 * @property width The width of the texture.
 * @property height The height of the texture.
 * @constructor Creates a new texture.
 */
open class Texture(val gameImage : GameImage)
{
    private val needRefresh = AtomicBoolean(true)
    private var videoMemoryId : Int = -1

    /**
     * The width of the texture.
     */
    val width : Int = this.gameImage.width

    /**
     * The height of the texture.
     */
    val height : Int = this.gameImage.height

    init
    {
        this.gameImage.refreshFlow.register { this.needRefresh.set(true) }
    }

    /**
     * Binds the texture to the current OpenGL context.
     *
     * For internal use only.
     */
    internal fun bind()
    {
        // If no video memory ID, create it
        if (this.videoMemoryId < 0)
        {
            val stack = MemoryStack.stackPush()
            val textureID = stack.mallocInt(1)
            GL11.glGenTextures(textureID)
            this.videoMemoryId = textureID.get()
            MemoryStack.stackPop()
        }

        // If the texture need to be refresh
        if (this.needRefresh.compareAndSet(true, false))
        {
            // Push pixels in video memory
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.videoMemoryId)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                              this.width, this.height,
                              0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                              transferByte(this.gameImage.grabPixels().rgba))
        }

        // Draw the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.videoMemoryId)
    }
}