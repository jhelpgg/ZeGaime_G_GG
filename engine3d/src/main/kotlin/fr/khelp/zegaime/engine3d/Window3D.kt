package fr.khelp.zegaime.engine3d

import fr.khelp.zegaime.engine3d.events.ActionManager
import fr.khelp.zegaime.engine3d.events.JoystickManager
import fr.khelp.zegaime.engine3d.events.KeyboardManager
import fr.khelp.zegaime.engine3d.events.MouseManager
import fr.khelp.zegaime.engine3d.events.MouseStatus
import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.engine3d.particles.ParticleManager
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.scene.NodeWithMaterial
import fr.khelp.zegaime.engine3d.scene.Scene
import fr.khelp.zegaime.engine3d.utils.TEMPORARY_FLOAT_BUFFER
import fr.khelp.zegaime.engine3d.utils.gluPerspective
import fr.khelp.zegaime.preferences.PreferencesDatabase
import fr.khelp.zegaime.utils.logs.information
import fr.khelp.zegaime.utils.logs.verbose
import fr.khelp.zegaime.utils.logs.warning
import fr.khelp.zegaime.utils.tasks.delay
import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.synchro.Locker
import java.util.Optional
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

/**
 * Window to show 3D
 */
class Window3D private constructor()
{
    companion object
    {
        /**
         * Creates a Widow 3D
         *
         * @param x Position X on screen
         * @param y Position Y on screen
         * @param requestWidth Requested width
         * @param requestHeight Requested height
         * @param title Window title
         * @param decorated Whether window have native border or not
         * @param maximized Whether window should take the maximum size. If `true` [requestWidth] and [requestHeight] are ignored
         * @param atTop Whether the window is over all-other windows on screen
         */
        internal fun createWindow3D(x : Int, y : Int,
                                    requestWidth : Int, requestHeight : Int, title : String,
                                    decorated : Boolean, maximized : Boolean, atTop : Boolean) : Window3D
        {
            val window3D = Window3D()

            ({
                var width = requestWidth
                var height = requestHeight

                // Setup the error callback.
                GLFWErrorCallback.create(DebugGLFErrorCallback).set()

                // Initialize GLFW. Most GLFW functions will not work before doing this.
                if (!GLFW.glfwInit())
                {
                    throw Window3DCantBeCreatedException("Initialization of GLFW failed")
                }

                // Configure GLFW
                GLFW.glfwDefaultWindowHints()
                // the window not show for the moment, need some initialization to do before
                GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)

                GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, if (decorated) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)
                GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)
                GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, if (maximized) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)
                GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, if (atTop) GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)

                // Create the window
                val window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)

                if (window == MemoryUtil.NULL)
                {
                    throw Window3DCantBeCreatedException("Failed to create the GLFW window")
                }

                window3D.windowId = window
                // Register UI events
                GLFW.glfwSetKeyCallback(window) { _, key, _, action, _ ->
                    window3D.keyboardManager.keyEvent(key, action)
                }

                GLFW.glfwSetCursorEnterCallback(window) { _, entered ->
                    window3D.mouseManager.mouseEntered(entered)
                }

                GLFW.glfwSetMouseButtonCallback(window) { _, button, action, modifiers ->
                    window3D.mouseManager.mouseButton(button, action, modifiers)
                }

                GLFW.glfwSetCursorPosCallback(window) { _, cursorX, cursorY ->
                    window3D.mouseManager.mousePosition(cursorX, cursorY)
                }

                GLFW.glfwSetJoystickCallback { joystickID, event ->
                    window3D.joystickManager.joystickConnected(joystickID,
                                                               event)
                }

                GLFW.glfwSetWindowCloseCallback(window) { window3D.closeWindow() }

                // Get the thread stack and push a new frame

                var stack = MemoryStack.stackPush()
                var pWidth = stack.mallocInt(1)
                var pHeight = stack.mallocInt(1)

                // Get the window size passed to GLFW.glfwCreateWindow
                GLFW.glfwGetWindowSize(window, pWidth, pHeight)

                // Get the resolution of the primary monitor
                val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

                if (videoMode != null)
                {
                    // Center the window
                    GLFW.glfwSetWindowPos(
                        window,
                        if (x >= 0) x + (width - pWidth.get(0)) / 2 else (videoMode.width() - pWidth.get(0)) / 2,
                        if (y >= 0) y + (height - pHeight.get(0)) / 2 else (videoMode.height() - pHeight.get(0)) / 2)
                }
                else
                {
                    warning("No video mode !")
                }

                MemoryStack.stackPop()

                // Make the window visible
                GLFW.glfwShowWindow(window)

                // Get the thread stack and compute window size

                stack = MemoryStack.stackPush()
                pWidth = stack.mallocInt(1)
                pHeight = stack.mallocInt(1)

                // Get the real window size
                GLFW.glfwGetWindowSize(window, pWidth, pHeight)
                width = pWidth.get()
                height = pHeight.get()
                MemoryStack.stackPop()

                window3D.width = width
                window3D.height = height

                window3D.render3D()
            }).parallel()

            window3D.readyLocker.lock()
            return window3D
        }
    }

    var width : Int = 0
        private set

    var height : Int = 0
        private set

    val gui = GUI()
    val keyboardManager : KeyboardManager = KeyboardManager()
    val mouseManager : MouseManager = MouseManager(this.gui)
    val joystickManager : JoystickManager = JoystickManager()
    val actionManager : ActionManager = ActionManager(this.keyboardManager, this.joystickManager)
    val particleManager = ParticleManager()
    val scene : Scene = Scene()
    var canCloseNow : () -> Boolean = { true }

    private var windowId = 0L
    private val readyLocker = Locker()
    private val waitingCloseLock = Object()
    private var nodeDetect : Node? = null
    private val nodePickedFlowData = FlowSource<Optional<Node>>()
    val nodePickedFlow : Flow<Optional<Node>> = this.nodePickedFlowData.flow

    fun closeWindow()
    {
        if (!this.canCloseNow())
        {
            // Avoid the closing
            GLFW.glfwSetWindowShouldClose(this.windowId, false)
            return
        }

        PreferencesDatabase.close()
        GLFW.glfwSetWindowShouldClose(this.windowId, true)
        delay(128L) { synchronized(this.waitingCloseLock) { this.waitingCloseLock.notifyAll() } }
    }

    internal fun waitWindowClose()
    {
        synchronized(this.waitingCloseLock) { this.waitingCloseLock.wait() }
    }

    private fun render3D()
    {
        this.mouseManager.width = this.width
        this.mouseManager.height = this.height
        this.gui.size(this.width, this.height)
        this.joystickManager.initialize()

        this.initialize3D()
        this.readyLocker.unlock()

        while (!GLFW.glfwWindowShouldClose(this.windowId))
        {
            this.renderLoop()

            // swap the color buffers
            GLFW.glfwSwapBuffers(this.windowId)

            // Poll for window events. The key callback will only be invoked during this call.
            GLFW.glfwPollEvents()
            this.joystickManager.updateJoystickStatus()
            this.keyboardManager.reportKeys()
            this.actionManager.report()
        }

        Callbacks.glfwFreeCallbacks(this.windowId)
        GLFW.glfwDestroyWindow(this.windowId)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
        verbose("Good bye !")
    }

    private fun initialize3D()
    {
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(this.windowId)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        glCapabilities(GL.createCapabilities())

        information("VERSION OPEN GL = ", versionOpenGL)

        // TODO : Lights
        /*
        TEMPORARY_INT_BUFFER.rewind()
        GL11.glGetIntegerv(GL11.GL_MAX_LIGHTS, TEMPORARY_INT_BUFFER)
        TEMPORARY_INT_BUFFER.rewind()
        this.lights = Lights(TEMPORARY_INT_BUFFER.get())
         */

        // *************************
        // *** Initialize OpenGL ***
        // *************************
        // Alpha enable
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        // Set alpha precision
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f)
        // Material can be colored
        GL11.glEnable(GL11.GL_COLOR_MATERIAL)
        // For performance disable texture, we enable them only on need
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        // Way to compute alpha
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        // We accept blending
        GL11.glEnable(GL11.GL_BLEND)
        // Fix the view port
        GL11.glViewport(0, 0, this.width, this.height)
        // Normalization is enabled
        GL11.glEnable(GL11.GL_NORMALIZE)
        // Fix the view port. Yes again, I don't know why, but it works better on
        // doing that
        GL11.glViewport(0, 0, this.width, this.height)

        // Set the "3D feeling".
        // That is to say how the 3D looks like
        // Here we want just see the depth, but not have fish eye effect
        GL11.glMatrixMode(GL11.GL_PROJECTION)
        GL11.glLoadIdentity()
        val ratio = this.width.toFloat() / this.height.toFloat()
        gluPerspective(45.0, ratio.toDouble(), 0.1, 5000.0)
        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glLoadIdentity()

        // Initialize background
        GL11.glClearColor(1f, 1f, 1f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)

        // Enable see and hide face
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_FRONT)

        // Light base adjustment for smooth effect
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, GL11.GL_TRUE)
        GL11.glShadeModel(GL11.GL_SMOOTH)
        GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR)
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, 1)

        // Enable lights and default light
        GL11.glEnable(GL11.GL_LIGHTING)
    }

    private fun renderLoop()
    {
        this.renderPicking()
        this.renderScene()
    }

    /**
     * Render the scene on picking mode
     */
    private fun renderPicking()
    {
        // Prepare for "picking rendering"
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glClearColor(1f, 1f, 1f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glPushMatrix()

        // Render the scene in picking mode
        this.scene.root.renderTheNodePicking()
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_LIGHTING)

        val previousNode = this.nodeDetect

        // If detection point is on the screen
        if (this.mouseManager.mouseStatus != MouseStatus.OUTSIDE
            && this.mouseManager.mouseX >= 0 && this.mouseManager.mouseX < this.width
            && this.mouseManager.mouseY >= 0 && this.mouseManager.mouseY < this.height)
        {
            // Compute pick color and node pick
            this.nodeDetect = this.scene.root
                .pickingNode(this.pickColor(this.mouseManager.mouseX, this.mouseManager.mouseY))
        }
        else
        {
            this.nodeDetect = null
        }

        val actualNode = this.nodeDetect

        if (previousNode != actualNode)
        {
            if (previousNode is NodeWithMaterial)
            {
                previousNode.selected = false
            }

            if (actualNode is NodeWithMaterial)
            {
                actualNode.selected = true
            }

            this.nodePickedFlowData.publish(Optional.ofNullable(actualNode))
        }
    }

    private fun pickColor(x : Int, y : Int) : Color4f
    {
        // Get picking color
        TEMPORARY_FLOAT_BUFFER.rewind()
        GL11.glReadPixels(x, this.height - y, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, TEMPORARY_FLOAT_BUFFER)
        TEMPORARY_FLOAT_BUFFER.rewind()

        // Convert in RGB value
        val red = TEMPORARY_FLOAT_BUFFER.get()
        val green = TEMPORARY_FLOAT_BUFFER.get()
        val blue = TEMPORARY_FLOAT_BUFFER.get()
        TEMPORARY_FLOAT_BUFFER.rewind()

        return Color4f(red, green, blue)
    }

    private fun renderScene()
    {
        try
        {
            // TODO
            // Draw the background and clear Z-Buffer
            this.scene.drawBackground(this.width, this.height)
            GL11.glEnable(GL11.GL_DEPTH_TEST)

            // Render the scene
            GL11.glPushMatrix()
            this.scene.render()
            GL11.glPopMatrix()

            this.particleManager.draw()

            // Draw 2D objects over 3D
            if(this.gui.visible)
            {
                this.gui.update()
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glPushMatrix()
                this.gui.plane.matrixRootToMe()
                this.gui.plane.renderSpecific()
                GL11.glPopMatrix()
            }
        }
        catch (ignored : Exception)
        {
        }
        catch (ignored : Error)
        {
        }
    }
}