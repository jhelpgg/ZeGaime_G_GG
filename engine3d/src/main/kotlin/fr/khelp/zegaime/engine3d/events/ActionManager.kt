package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.engine3d.utils.getActionDescription
import fr.khelp.zegaime.engine3d.utils.storeActionDescription
import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.flow.flowJoin
import java.util.TreeSet
import org.lwjgl.glfw.GLFW

/**
 * Manages the actions and their mapping to keys and joystick buttons.
 *
 * This class is responsible for detecting which actions are currently active based on the pressed keys and joystick buttons.
 * It also allows to change the mapping of the actions.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage:**
 * ```kotlin
 * val actionManager = window3D.actionManager
 * actionManager.actionCodes.observedBy { actionCodes ->
 *     if(ActionCode.ACTION_UP in actionCodes) {
 *          // Do up action
 *     }

 *     if(ActionCode.ACTION_DOWN in actionCodes) {
 *          // Do down action
 *     }
 *
 *     // ...
 * }
 * ```
 *
 * @property actionCodes A flow that emits the currently active action codes.
 * @constructor Creates a new action manager. For internal use only.
 */
class ActionManager internal constructor(keyboardManager : KeyboardManager, joystickManager : JoystickManager)
{
    private val currentActionCodes = TreeSet<ActionCode>()
    private val actionCodesSource = FlowSource<Array<ActionCode>>()

    /**
     * A flow that emits the currently active action codes.
     */
    val actionCodes : Flow<Array<ActionCode>> = this.actionCodesSource.flow

    private val actionAssociations = ArrayList<ActionDescription>()

    init
    {
        flowJoin(keyboardManager.keyPressed, joystickManager.joystickCodes, this::keyAndJoystick)

        for (actionCode in ActionCode.entries)
        {
            this.actionAssociations.add(getActionDescription(actionCode))
        }
    }

    /**
     * Associates an action code with a joystick code.
     *
     * @param actionCode The action code to associate.
     * @param joystickCode The joystick code to associate with the action.
     * @return The old joystick code associated with the action.
     */
    fun associate(actionCode : ActionCode, joystickCode : JoystickCode) : JoystickCode
    {
        synchronized(this.actionAssociations)
        {
            for (actionDescription in this.actionAssociations)
            {
                if (actionDescription.actionCode == actionCode)
                {
                    val oldJoystickCode = actionDescription.joystickCode

                    if (oldJoystickCode != joystickCode)
                    {
                        actionDescription.joystickCode = joystickCode
                        storeActionDescription(actionDescription)
                    }

                    return oldJoystickCode
                }
            }

            return JoystickCode.NONE
        }
    }

    /**
     * Associates an action code with a key code.
     *
     * @param actionCode The action code to associate.
     * @param keyCode The key code to associate with the action.
     * @return The old key code associated with the action.
     */
    fun associate(actionCode : ActionCode, keyCode : Int) : Int
    {
        synchronized(this.actionAssociations)
        {
            for (actionDescription in this.actionAssociations)
            {
                if (actionDescription.actionCode == actionCode)
                {
                    val oldKeyCode = actionDescription.keyCode

                    if (oldKeyCode != keyCode)
                    {
                        actionDescription.keyCode = keyCode
                        storeActionDescription(actionDescription)
                    }

                    return oldKeyCode
                }
            }

            return GLFW.GLFW_KEY_UNKNOWN
        }
    }

    /**
     * Returns the joystick code associated with the given action code.
     *
     * @param actionCode The action code.
     * @return The joystick code associated with the action.
     */
    fun joystickAssociation(actionCode : ActionCode) : JoystickCode
    {
        synchronized(this.actionAssociations)
        {
            for (actionDescription in this.actionAssociations)
            {
                if (actionDescription.actionCode == actionCode)
                {
                    return actionDescription.joystickCode
                }
            }

            return JoystickCode.NONE
        }
    }

    /**
     * Returns the key code associated with the given action code.
     *
     * @param actionCode The action code.
     * @return The key code associated with the action.
     */
    fun keyAssociation(actionCode : ActionCode) : Int
    {
        synchronized(this.actionAssociations)
        {
            for (actionDescription in this.actionAssociations)
            {
                if (actionDescription.actionCode == actionCode)
                {
                    return actionDescription.keyCode
                }
            }

            return GLFW.GLFW_KEY_UNKNOWN
        }
    }

    /**
     * Reports the currently active action codes.
     *
     * For internal use only.
     */
    internal fun report()
    {
        synchronized(this.actionAssociations)
        {
            this.actionCodesSource.publish(this.currentActionCodes.toTypedArray())
        }
    }

    private fun keyAndJoystick(keys : IntArray, joystickCodes : List<JoystickCode>)
    {
        synchronized(this.actionAssociations)
        {
            this.currentActionCodes.clear()

            for (key in keys)
            {
                val actionCode =
                    this.actionAssociations.firstOrNull { actionDescription -> actionDescription.keyCode == key }?.actionCode
                    ?: continue
                this.currentActionCodes.add(actionCode)
            }

            for (joystickCode in joystickCodes)
            {
                val actionCode =
                    this.actionAssociations.firstOrNull { actionDescription -> actionDescription.joystickCode == joystickCode }?.actionCode
                    ?: continue
                this.currentActionCodes.add(actionCode)
            }
        }
    }
}
