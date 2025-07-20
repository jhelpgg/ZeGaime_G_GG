package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.engine3d.utils.getActionDescription
import fr.khelp.zegaime.engine3d.utils.storeActionDescription
import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.flow.flowJoin
import java.util.TreeSet
import org.lwjgl.glfw.GLFW

class ActionManager internal constructor(keyboardManager : KeyboardManager, joystickManager : JoystickManager)
{
    private val currentActionCodes = TreeSet<ActionCode>()
    private val actionCodesSource = FlowSource<Array<ActionCode>>()
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