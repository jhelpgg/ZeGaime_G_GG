package fr.khelp.zegaime.engine3d.gui

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.images.GameImage

/**
 * Represents the graphical user interface of the 3D window.
 *
 * The GUI is a plane that is always facing the camera.
 * It can be used to display 2D elements on top of the 3D scene.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage:**
 * ```kotlin
 * val gui = window3D.gui
 * // ...
 * ```
 *
 * @constructor Creates a new GUI. For internal use only.
 */
class GUI
{
    /**
     * The plane that represents the GUI.
     */
    internal val plane = Plane("GUI")
    private var width = -1
    private var height = -1
    private val image by lazy { GameImage(this.width, this.height) }
    private val texture by lazy { Texture(this.image) }

    /**
     * Sets the size of the GUI.
     *
     * For internal use only.
     *
     * @param width The width of the GUI.
     * @param height The height of the GUI.
     */
    internal fun size(width: Int, height: Int)
    {
        this.width = width
        this.height = height

        this.plane.material.settingAsFor2D()
        this.plane.material.textureDiffuse = this.texture

        when
        {
            this.width == this.height -> Unit
            this.width > this.height -> this.plane.scaleX = this.width.toFloat() / this.height.toFloat()
            else -> this.plane.scaleY = this.height.toFloat() / this.width.toFloat()
        }

        this.plane.z = -1.2f
    }

    /**
     * Called when a mouse event occurs.
     *
     * For internal use only.
     *
     * @param mouseState The state of the mouse.
     * @return `true` if the event was consumed by the GUI, `false` otherwise.
     */
    internal fun mouseState(mouseState: MouseState): Boolean
    {
        return false
    }

    /**
     * Updates the GUI.
     *
     * For internal use only.
     */
    internal fun update()
    {
    }
}
