package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.utils.tasks.delay
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import org.lwjgl.glfw.GLFW

class MouseManager internal constructor(private val gui : GUI)
{
    companion object
    {
        private const val STAY_TIMEOUT = 256L
        private const val CLICK_TIME = 256L
    }

    var mouseX : Int = -1
        internal set
    var mouseY : Int = -1
        internal set
    var leftButtonDown : Boolean = false
        internal set
    var middleButtonDown : Boolean = false
        internal set
    var rightButtonDown : Boolean = false
        internal set
    var insideWindow3D : Boolean = false
        internal set
    var control : Boolean = false
        internal set
    var shift : Boolean = false
        private set
    var alt : Boolean = false
        private set

    internal var width = -1
    internal var height = -1

    private var clicked = false
    private var lastLeftDownTime = 0L
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
    val mouseStateObservable : Observable<MouseState> = this.mouseStateObservableData.observable

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