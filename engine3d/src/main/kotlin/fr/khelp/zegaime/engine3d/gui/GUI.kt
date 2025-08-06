package fr.khelp.zegaime.engine3d.gui

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentPanel
import fr.khelp.zegaime.engine3d.gui.component.GUIDialog
import fr.khelp.zegaime.engine3d.gui.dialogs.DialogColorChooser
import fr.khelp.zegaime.engine3d.gui.dialogs.DialogMessage
import fr.khelp.zegaime.engine3d.gui.dsl.GUIMenuBarCreator
import fr.khelp.zegaime.engine3d.gui.layout.GUIConstraints
import fr.khelp.zegaime.engine3d.gui.layout.GUILayout
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import fr.khelp.zegaime.engine3d.gui.menu.GUIMenuBar
import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.TRANSPARENT
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.invert
import fr.khelp.zegaime.images.color.semiVisible
import fr.khelp.zegaime.images.font.DEFAULT_FONT
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.resources.defaultTexts
import fr.khelp.zegaime.utils.tasks.synchro.Mutex

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
class GUI internal constructor()
{
    var layout : GUILayout<*> = GUIAbsoluteLayout()
        set(value)
        {
            field = value
            this.completeLayout()
        }

    val dialogMessage = DialogMessage(this)

    var visible = true

    /**
     * The plane that represents the GUI.
     */
    internal val plane = Plane("GUI")

    private var width = -1
    private var height = -1
    private val image by lazy { GameImage(this.width, this.height) }
    private val texture by lazy { Texture(this.image) }
    private val mutexDialog = Mutex()
    private val dialogs = ArrayList<GUIDialog<*, *>>()
    private var menuBarPanel : GUIComponentPanel<*, *>? = null
    private var menuBarY = 0
    private var completeLayout : GUILayout<*> = GUIAbsoluteLayout()

    fun <C : GUIConstraints, L : GUILayout<C>> createDialog(layout : L) : GUIDialog<C, L> =
        GUIDialog<C, L>(GUIComponentPanel<C, L>(layout), this)

    fun dialogColorChooser() : DialogColorChooser = DialogColorChooser(this)

    fun menuBar(resourcesText : ResourcesText = defaultTexts,
                textColor : Color = WHITE,
                textBackColor : Color = textColor.invert,
                font : JHelpFont = DEFAULT_FONT,
                backgroundColor : Color = Green.GREEN_0050.color.semiVisible,
                creator : GUIMenuBarCreator.() -> Unit)
    {
        val menuBar = GUIMenuBar(resourcesText, font, textColor, textBackColor, backgroundColor)
        val menuCreator = GUIMenuBarCreator(menuBar)
        creator(menuCreator)
        val (panel, height) = menuBar.panel(this.width)
        this.menuBarPanel = panel
        this.menuBarY = height
        this.completeLayout()
    }

    /**
     * Sets the size of the GUI.
     *
     * For internal use only.
     *
     * @param width The width of the GUI.
     * @param height The height of the GUI.
     */
    internal fun size(width : Int, height : Int)
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
    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        if(!this.visible)
        {
            return false
        }

        var panelNullable : GUIComponentPanel<*, *>? = null

        this.mutexDialog {
            if (this.dialogs.isNotEmpty())
            {
                panelNullable = this.dialogs[this.dialogs.size - 1].panel
            }
        }

        val panel = panelNullable

        if (panel != null)
        {
            val state = MouseState(mouseState.mouseStatus,
                                   mouseState.x - panel.x - panel.margin.left,
                                   mouseState.y - panel.y - panel.margin.top,
                                   mouseState.leftButtonDown, mouseState.middleButtonDown, mouseState.rightButtonDown,
                                   mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                   mouseState.clicked)
            return panel.mouseState(state)
        }

        return this.completeLayout.mouseState(mouseState)
    }

    /**
     * Updates the GUI.
     *
     * For internal use only.
     */
    internal fun update()
    {
        this.completeLayout.layout(this.width, this.height)
        this.image.clear(TRANSPARENT)
        this.image.draw { graphics2D ->
            for (component in completeLayout.components())
            {
                if (component.visible)
                {
                    val clip = graphics2D.clip
                    val transform = graphics2D.transform
                    graphics2D.clipRect(component.x, component.y, component.width, component.height)
                    graphics2D.translate(component.x, component.y)
                    component.draw(graphics2D)
                    graphics2D.clip = clip
                    graphics2D.transform = transform
                }
            }

            this.mutexDialog {
                for (dialog in this.dialogs)
                {
                    val panel = dialog.panel
                    val size = panel.preferredSize()
                    panel.x = (this.width - size.width) / 2
                    panel.y = (this.height - size.height) / 2
                    panel.width = size.width
                    panel.height = size.height
                    val clip = graphics2D.clip
                    val transform = graphics2D.transform
                    graphics2D.clipRect(panel.x, panel.y, panel.width, panel.height)
                    graphics2D.translate(panel.x, panel.y)
                    panel.draw(graphics2D)
                    graphics2D.clip = clip
                    graphics2D.transform = transform
                }
            }
        }
    }

    internal fun <C : GUIConstraints, L : GUILayout<C>> show(dialog : GUIDialog<C, L>)
    {
        this.mutexDialog { this.dialogs.add(dialog) }
    }

    internal fun <C : GUIConstraints, L : GUILayout<C>> hide(dialog : GUIDialog<C, L>)
    {
        this.mutexDialog { this.dialogs.remove(dialog) }
    }

    private fun completeLayout()
    {
        val menuBar = this.menuBarPanel

        if (menuBar == null)
        {
            this.completeLayout = this.layout
        }
        else
        {
            val completeLayout = GUIAbsoluteLayout()
            completeLayout.add(GUIComponentPanel(this.layout),
                               GUIAbsoluteConstraint(0, this.menuBarY, this.width, this.height - this.menuBarY))
            completeLayout.add(menuBar,
                               GUIAbsoluteConstraint(0, 0, this.width, this.height))
            this.completeLayout = completeLayout
        }

        this.update()
    }
}
