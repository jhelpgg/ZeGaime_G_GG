package fr.khelp.zegaime.engine3d

import fr.khelp.zegaime.engine3d.events.ActionCode
import fr.khelp.zegaime.engine3d.render.GREEN
import fr.khelp.zegaime.engine3d.render.RED
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.resources.WARNING_IMAGE_64
import fr.khelp.zegaime.utils.logs.debug
import kotlin.math.max
import org.lwjgl.glfw.GLFW

/**
 * Creates a window 3D decorated
 *
 * @param width Initial width
 * @param height Initial height
 * @param title Widow title
 * @param windowCreator Fill the window
 */
fun window3D(width : Int, height : Int, title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(-1, -1, width, height, title,
                                           decorated = true, maximized = false, atTop = false)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

fun main()
{
    window3DFull("Test") {
        val plane = Plane("plane")
        plane.material.colorDiffuse = GREEN
        plane.material.textureDiffuse = Texture(WARNING_IMAGE_64)
        plane.z = -5f
        this.scene.root.addChild(plane)
    }
}

/**
 * Creates a full-screen window 3D
 *
 * @param title Widow title
 * @param windowCreator Fill the window
 */
fun window3DFull(title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(-1, -1, 800, 600, title,
                                           decorated = false, maximized = true, atTop = false)
    windowCreator(window3D)
    window3D.waitWindowClose()
}

/**
 * Creates a fix size window 3D not decorated
 *
 * @param x X position on screen
 * @param y Y position on screen
 * @param width Widow width
 * @param height Widow height
 * @param title Widow title
 * @param windowCreator Fill the window
 */
fun window3DFix(x : Int, y : Int, width : Int, height : Int, title : String, windowCreator : Window3D.() -> Unit)
{
    val window3D = Window3D.createWindow3D(max(0, x), max(0, y), width, height, title,
                                           decorated = false, maximized = false, atTop = true)
    windowCreator(window3D)
    window3D.waitWindowClose()
}
