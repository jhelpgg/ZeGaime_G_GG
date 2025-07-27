package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.utils.tasks.delay
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import org.lwjgl.glfw.GLFW

/**
 * Manages the mouse events.
 *
 * This class is responsible for detecting the mouse events and publishing them as a flow of mouse states.
 *
 * **Creation example**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage**
 * ```kotlin
 * val mouseManager = window3D.mouseManager
 * mouseManager.mouseStateObservable.observedBy { mouseState ->
 *     // ...
 * }
 * ```
 *
 * @property mouseX The current X position of the mouse.
 * @property mouseY The current Y position of the mouse.
 * @property leftButtonDown Indicates if the left mouse button is down.
 * @property middleButtonDown Indicates if the middle mouse button is down.
 * @property rightButtonDown Indicates if the right mouse button is down.
 * @property insideWindow3D Indicates if the mouse is inside the 3D window.
 * @property control Indicates if the control key is down.
 * @property shift Indicates if the shift key is down.
 * @property alt Indicates if the alt key is down.
 * @property mouseStatus The current status of the mouse.
 * @property mouseStateObservable A flow that emits the mouse state changes.
 * @constructor Creates a new mouse manager. For internal use only.
 */
class MouseManager internal constructor(private val gui : GUI)
{
    companion object
    {
        private const val STAY_TIMEOUT = 256L
        private const val CLICK_TIME = 256L
    }

    /**
     * The current X position of the mouse.
     */
    var mouseX : Int = -1
        internal set

    /**
     * The current Y position of the mouse.
     */
    var mouseY : Int = -1
        internal set

    /**
     * Indicates if the left mouse button is down.
     */
    var leftButtonDown : Boolean = false
        internal set

    /**
     * Indicates if the middle mouse button is down.
     */
    var middleButtonDown : Boolean = false
        internal set

    /**
     * Indicates if the right mouse button is down.
     */
    var rightButtonDown : Boolean = false
        internal set

    /**
     * Indicates if the mouse is inside the 3D window.
     */
    var insideWindow3D : Boolean = false
        internal set

    /**
     * Indicates if the control key is down.
     */
    var control : Boolean = false
        internal set

    /**
     * Indicates if the shift key is down.
     */
    var shift : Boolean = false
        private set

    /**
     * Indicates if the alt key is down.
     */
    var alt : Boolean = false
        private set

    internal var width = -1
    internal var height = -1

    private var clicked = false
    private var lastLeftDownTime = 0L

    /**
     * The current status of the mouse.
     */
    var mouseStatus = MouseStatus.STAY
        private set
    private var stayTimeOut : Future<Unit>? = null
    private val mouseStateObservableData = ObservableSource<MouseState>(MouseState(MouseStatus.STAY, -1, -1,
                                                                                   leftButtonDown = false,
                                                                                   middleButtonDown = false,
                                                                                   rightButtonDown = false,
                                                                                   shiftDown = false,
                                                                                   controlDown = false,
                                                                                   altDown = false,
                                                                                   clicked = false))

    /**
     * A flow that emits the mouse state changes.
     */
    val mouseStateObservable : Observable<MouseState> = this.mouseStateObservableData.observable

    /**
     * Called when the mouse enters or leaves the 3D window.
     *
     * For internal use only.
     *
     * @param entered `true` if the mouse entered the window, `false` otherwise.
     */
    internal fun mouseEntered(entered : Boolean)
    {
        if (this.insideWindow3D != entered)
        {
            this.insideWindow3D = entered

            if (entered)
            {
                this.pushMouseMoveOrDrag()
            }
            else
            {
                this.pushMouseState(MouseStatus.OUTSIDE)
            }
        }
    }

    /**
     * Called when a mouse button event occurs.
     *
     * For internal use only.
     *
     * @param button The mouse button.
     * @param action The action type.
     * @param modifiers The modifiers.
     */
    internal fun mouseButton(button : Int, action : Int, modifiers : Int)
    {
        this.shift = (modifiers and GLFW.GLFW_MOD_SHIFT) != 0
        this.control = (modifiers and GLFW.GLFW_MOD_CONTROL) != 0
        this.alt = (modifiers and GLFW.GLFW_MOD_ALT) != 0

        when (button)
        {
            GLFW.GLFW_MOUSE_BUTTON_LEFT   ->
            {
                this.leftButtonDown = action == GLFW.GLFW_PRESS

                if (this.mouseStatus != MouseStatus.OUTSIDE)
                {
                    if (this.leftButtonDown)
                    {
                        this.lastLeftDownTime = System.currentTimeMillis()
                    }
                    else if (System.currentTimeMillis() - this.lastLeftDownTime <= MouseManager.CLICK_TIME)
                    {
                        this.clicked = true
                    }
                }
            }

            GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> this.middleButtonDown = action == GLFW.GLFW_PRESS
            GLFW.GLFW_MOUSE_BUTTON_RIGHT  -> this.rightButtonDown = action == GLFW.GLFW_PRESS
        }

        if (this.mouseStatus == MouseStatus.MOVE && (this.leftButtonDown || this.middleButtonDown || this.rightButtonDown))
        {
            this.mouseStatus = MouseStatus.DRAG
        }
        else if (mouseStatus == MouseStatus.DRAG && !this.leftButtonDown && !this.middleButtonDown && !this.rightButtonDown)
        {
            this.mouseStatus = MouseStatus.MOVE
        }

        this.pushMouseState(this.mouseStatus)
    }

    /**
     * Called when the mouse position changes.
     *
     * For internal use only.
     *
     * @param cursorX The new X position of the mouse.
     * @param cursorY The new Y position of the mouse.
     */
    internal fun mousePosition(cursorX : Double, cursorY : Double)
    {
        this.mouseX = cursorX.toInt()
        this.mouseY = cursorY.toInt()
        this.pushMouseMoveOrDrag()
    }

    private fun pushMouseMoveOrDrag()
    {
        val status =
            if (this.mouseX < 0 || this.mouseX >= this.width || this.mouseY < 0 || this.mouseY >= this.height)
            {
                MouseStatus.OUTSIDE
            }
            else if (this.leftButtonDown || this.middleButtonDown || this.rightButtonDown)
            {
                MouseStatus.DRAG
            }
            else
            {
                MouseStatus.MOVE
            }

        this.pushMouseState(status)
    }

    private fun pushMouseState(mouseStatus : MouseStatus)
    {
        this.mouseStatus = mouseStatus
        val mouseState = MouseState(mouseStatus,
                                    this.mouseX, this.mouseY,
                                    this.leftButtonDown, this.middleButtonDown, this.rightButtonDown,
                                    this.shift, this.control, this.alt,
                                    this.clicked)


        if (!this.gui.mouseState(mouseState))
        {
            this.mouseStateObservableData.value = mouseState
        }

        if (mouseStatus == MouseStatus.MOVE || mouseStatus == MouseStatus.DRAG || this.clicked)
        {
            this.stayTimeOut?.cancel("Launch again")
            this.stayTimeOut =
                delay(MouseManager.STAY_TIMEOUT) {
                    this.clicked = false
                    this.pushMouseState(MouseStatus.STAY)
                }
        }

        this.clicked = false
    }
}
