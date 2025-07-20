package fr.khelp.zegaime.engine3d.gui

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.images.GameImage

class GUI
{
    internal val plane = Plane("GUI")
    private var width = -1
    private var height = -1
    private val image by lazy { GameImage(this.width, this.height) }
    private val texture by lazy { Texture(this.image) }

    internal fun size(width:Int, height:Int)
    {
        this.width = width
        this.height = height

        this.plane.material.settingAsFor2D()
        this.plane.material.textureDiffuse = this.texture

        when
        {
            this.width == this.height -> Unit
            this.width > this.height  -> this.plane.scaleX = this.width.toFloat() / this.height.toFloat()
            else                      -> this.plane.scaleY = this.height.toFloat() / this.width.toFloat()
        }

        this.plane.z = - 1.2f
    }

    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        return false
    }

    internal fun update()
    {
    }
}