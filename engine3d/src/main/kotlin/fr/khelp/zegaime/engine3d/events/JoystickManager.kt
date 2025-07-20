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

class JoystickManager internal constructor()
{
    private val joystickCodesFlow = FlowSource<List<JoystickCode>>()
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
     * Capture the next joystick event
     *
     * @return Future that will contains the next joystick event
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

    internal fun initialize()
    {
        for (joystickCode in JoystickCode.entries)
        {
            this.currentJoystickCodes[joystickCode] = JoystickStatus.RELEASED
        }

        this.computeAxisLimits()
        this.initialized = true
    }

    internal fun joystickConnected(joystickID : Int, event : Int)
    {
        todo("joystickID=", joystickID, "<>", GLFW.GLFW_JOYSTICK_1, " event=", event, "<>", GLFW.GLFW_TRUE)

        if (this.initialized && joystickID == GLFW.GLFW_JOYSTICK_1 && event == GLFW.GLFW_TRUE)
        {
            this.initialize()
        }
    }

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