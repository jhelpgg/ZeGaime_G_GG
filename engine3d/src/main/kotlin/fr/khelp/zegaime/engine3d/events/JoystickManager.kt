package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.utils.logs.todo
import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise
import fr.khelp.zegaime.utils.tasks.future.future
import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min
import org.lwjgl.glfw.GLFW

/**
 * Manages the joystick events.
 *
 * This class is responsible for detecting the joystick events and publishing them as a flow of joystick codes.
 * It also allows capturing the next joystick event.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage:**
 * ```kotlin
 * val joystickManager = window3D.joystickManager
 * joystickManager.joystickCodes.observedBy { joystickCodes ->
 *     if(JoystickCode.AXIS_1_POSITIVE in joystickCodes) {
 *          // ...
 *     }

 *     if(JoystickCode.BUTTON_6 in joystickCodes) {
 *          // ...
 *     }
 *
 *     // ...
 * }
 * ```
 *
 * @property joystickCodes A flow that emits the currently active joystick codes.
 * @constructor Creates a new joystick manager. For internal use only.
 */
class JoystickManager internal constructor()
{
    private val joystickCodesFlow = FlowSource<List<JoystickCode>>()

    /**
     * A flow that emits the currently active joystick codes.
     */
    val joystickCodes : Flow<List<JoystickCode>> = this.joystickCodesFlow.flow

    private var initialized = false
    private val axisLimits = Array<AxeLimits>(JoystickCode.MAX_AXIS_INDEX + 1) { AxeLimits(0f) }

    /**Current active joystick codes and their status*/
    private val currentJoystickCodes = HashMap<JoystickCode, JoystickStatus>()

    /**Copy of last current active joystick codes and their status*/
    private val currentJoystickCodesCopy = HashMap<JoystickCode, JoystickStatus>()
    private var nextJoystickCode : Promise<JoystickCode>? = null
    private val mutexCapture = Mutex()
    private val canCaptureJoystick = AtomicBoolean(false)

    /**
     * Capture the next joystick event.
     *
     * @return A future that will contain the next joystick event.
     */
    fun captureJoystick() : Future<JoystickCode> =
        this.mutexCapture {
            if (this.nextJoystickCode == null)
            {
                this.nextJoystickCode = Promise<JoystickCode>()
                this.canCaptureJoystick.set(false)

                for ((key, value) in this.currentJoystickCodes)
                {
                    this.currentJoystickCodesCopy[key] = value
                }
            }

            this.nextJoystickCode?.future ?: Exception("Fail to capture joystick code").future()
        }

    /**
     * Initializes the joystick manager.
     *
     * For internal use only.
     */
    internal fun initialize()
    {
        for (joystickCode in JoystickCode.entries)
        {
            this.currentJoystickCodes[joystickCode] = JoystickStatus.RELEASED
        }

        this.computeAxisLimits()
        this.initialized = true
    }

    /**
     * Called when a joystick is connected or disconnected.
     *
     * For internal use only.
     *
     * @param joystickID The ID of the joystick.
     * @param event The event type.
     */
    internal fun joystickConnected(joystickID : Int, event : Int)
    {
        todo("joystickID=", joystickID, "<>", GLFW.GLFW_JOYSTICK_1, " event=", event, "<>", GLFW.GLFW_TRUE)

        if (this.initialized && joystickID == GLFW.GLFW_JOYSTICK_1 && event == GLFW.GLFW_TRUE)
        {
            this.initialize()
        }
    }

    /**
     * Updates the joystick status.
     *
     * For internal use only.
     */
    internal fun updateJoystickStatus()
    {
        // Collect joystick status
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1))
        {
            val axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1)

            if (axes != null)
            {
                val position = axes.position()
                val axesValue = FloatArray(axes.limit() - position)
                axes.get(axesValue)
                axes.position(position)
                val max = min(axesValue.size - 1, JoystickCode.MAX_AXIS_INDEX)

                for (index in 0..max)
                {
                    val axeWay = this.axisLimits[index].way(axesValue[index])

                    if (axeWay == AxeWay.NEGATIVE)
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, true)] = JoystickStatus.RELEASED

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, false)))
                        {
                            return
                        }
                    }
                    else if (axeWay == AxeWay.POSITIVE)
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, false)] = JoystickStatus.RELEASED

                        if (this.pressJoystick(JoystickCode.obtainAxis(index, true)))
                        {
                            return
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, true)] = JoystickStatus.RELEASED
                        this.currentJoystickCodes[JoystickCode.obtainAxis(index, false)] = JoystickStatus.RELEASED
                    }
                }
            }

            val buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1)

            if (buttons != null)
            {
                val position = buttons.position()
                val buttonsStatus = ByteArray(buttons.limit() - position)
                buttons.get(buttonsStatus)
                buttons.position(position)
                val max = min(buttonsStatus.size - 1, JoystickCode.MAX_BUTTON_INDEX)

                for (index in 0..max)
                {
                    if (buttonsStatus[index].toInt() == GLFW.GLFW_PRESS)
                    {
                        if (this.pressJoystick(JoystickCode.obtainButton(index)))
                        {
                            return
                        }
                    }
                    else
                    {
                        this.currentJoystickCodes[JoystickCode.obtainButton(index)] = JoystickStatus.RELEASED
                    }
                }
            }
        }

        val collectJoystickActiveCodes = ArrayList<JoystickCode>()

        for ((code, status) in this.currentJoystickCodes)
        {
            if (status != JoystickStatus.RELEASED)
            {
                collectJoystickActiveCodes.add(code)
            }
        }

        this.joystickCodesFlow.publish(collectJoystickActiveCodes)
    }

    private fun computeAxisLimits()
    {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1))
        {
            val axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1)

            if (axes != null)
            {
                val position = axes.position()
                val axesValue = FloatArray(axes.limit() - position)
                axes.get(axesValue)
                axes.position(position)
                val max = min(axesValue.size - 1, JoystickCode.MAX_AXIS_INDEX)

                for (index in 0..max)
                {
                    this.axisLimits[index] = AxeLimits(axesValue[index])
                }
            }
        }
    }

    /**
     * Press a joystick input
     *
     * @param joystickCode Joystick code
     * @return Indicates if event is consumed
     */
    private fun pressJoystick(joystickCode : JoystickCode) : Boolean
    {
        val consumed = this.mutexCapture {
            if (this.nextJoystickCode != null)
            {
                if (this.canCaptureJoystick.compareAndSet(true, false))
                {
                    this.nextJoystickCode?.result(joystickCode)
                    this.nextJoystickCode = null

                    for ((key, value) in this.currentJoystickCodesCopy)
                    {
                        this.currentJoystickCodes[key] = value
                    }
                }
                else
                {
                    this.canCaptureJoystick.set(this.currentJoystickCodes.values.all { it === JoystickStatus.RELEASED })
                }

                true
            }
            else
            {
                false
            }
        }

        if (consumed)
        {
            return true
        }

        this.currentJoystickCodes[joystickCode] = this.currentJoystickCodes[joystickCode]!!.press()
        return false
    }
}
