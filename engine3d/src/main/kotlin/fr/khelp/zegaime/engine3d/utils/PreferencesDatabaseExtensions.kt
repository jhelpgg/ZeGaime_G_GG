package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.events.ActionCode
import fr.khelp.zegaime.engine3d.events.ActionDescription
import fr.khelp.zegaime.engine3d.events.JoystickCode
import fr.khelp.zegaime.engine3d.events.JoystickInputType
import fr.khelp.zegaime.preferences.PreferencesDatabase

fun getJoystickInputType(name : String) : JoystickInputType =
    try
    {
        JoystickInputType.valueOf(PreferencesDatabase.getString(name).value)
    }
    catch (_ : Exception)
    {
        JoystickInputType.NONE
    }

fun storeJoystickInputType(name : String, joystickInputType : JoystickInputType)
{
    PreferencesDatabase.getString(name).value = joystickInputType.name
}

fun getJoystickCode(name : String) : JoystickCode
{
    val index = PreferencesDatabase.getInt("${name}_index").value
    val type = getJoystickInputType("${name}_type")

    return when (type)
    {
        JoystickInputType.AXIS_NEGATIVE -> JoystickCode.obtainAxis(index = index, positive = false)

        JoystickInputType.AXIS_POSITIVE -> JoystickCode.obtainAxis(index = index, positive = true)

        JoystickInputType.BUTTON        -> JoystickCode.obtainButton(index)

        JoystickInputType.NONE          -> JoystickCode.NONE
    }
}

fun storeJoystickCode(name : String, joystickCode : JoystickCode)
{
    PreferencesDatabase.getInt("${name}_index").value = joystickCode.index
    storeJoystickInputType("${name}_type", joystickCode.joystickInputType)
}

internal fun getActionDescription(actionCode : ActionCode) : ActionDescription
{
    val joystickCode = getJoystickCode("${actionCode.preferenceKey}_joystick")
    val keyCode = PreferencesDatabase.getInt("${actionCode.preferenceKey}_key").value

    return if (joystickCode == JoystickCode.NONE && keyCode == 0)
    {
        ActionDescription(actionCode)
    }
    else
    {
        ActionDescription(actionCode, joystickCode, keyCode)
    }
}

internal fun storeActionDescription(actionDescription : ActionDescription)
{
    storeJoystickCode("${actionDescription.actionCode.preferenceKey}_joystick", actionDescription.joystickCode)
    PreferencesDatabase.getInt("${actionDescription.actionCode.preferenceKey}_key").value = actionDescription.keyCode
}