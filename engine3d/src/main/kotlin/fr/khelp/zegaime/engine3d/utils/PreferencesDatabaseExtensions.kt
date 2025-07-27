package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.events.ActionCode
import fr.khelp.zegaime.engine3d.events.ActionDescription
import fr.khelp.zegaime.engine3d.events.JoystickCode
import fr.khelp.zegaime.engine3d.events.JoystickInputType
import fr.khelp.zegaime.preferences.PreferencesDatabase

/**
 * Gets a joystick input type from the preferences.
 *
 * @param name The name of the preference.
 * @return The joystick input type.
 */
fun getJoystickInputType(name: String): JoystickInputType =
    try
    {
        JoystickInputType.valueOf(PreferencesDatabase.getString(name).value)
    }
    catch (_: Exception)
    {
        JoystickInputType.NONE
    }

/**
 * Stores a joystick input type in the preferences.
 *
 * @param name The name of the preference.
 * @param joystickInputType The joystick input type to store.
 */
fun storeJoystickInputType(name: String, joystickInputType: JoystickInputType)
{
    PreferencesDatabase.getString(name).value = joystickInputType.name
}

/**
 * Gets a joystick code from the preferences.
 *
 * @param name The name of the preference.
 * @return The joystick code.
 */
fun getJoystickCode(name: String): JoystickCode
{
    val index = PreferencesDatabase.getInt("${name}_index").value
    val type = getJoystickInputType("${name}_type")

    return when (type)
    {
        JoystickInputType.AXIS_NEGATIVE -> JoystickCode.obtainAxis(index = index, positive = false)

        JoystickInputType.AXIS_POSITIVE -> JoystickCode.obtainAxis(index = index, positive = true)

        JoystickInputType.BUTTON -> JoystickCode.obtainButton(index)

        JoystickInputType.NONE -> JoystickCode.NONE
    }
}

/**
 * Stores a joystick code in the preferences.
 *
 * @param name The name of the preference.
 * @param joystickCode The joystick code to store.
 */
fun storeJoystickCode(name: String, joystickCode: JoystickCode)
{
    PreferencesDatabase.getInt("${name}_index").value = joystickCode.index
    storeJoystickInputType("${name}_type", joystickCode.joystickInputType)
}

/**
 * Gets an action description from the preferences.
 *
 * For internal use only.
 *
 * @param actionCode The action code.
 * @return The action description.
 */
internal fun getActionDescription(actionCode: ActionCode): ActionDescription
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

/**
 * Stores an action description in the preferences.
 *
 * For internal use only.
 *
 * @param actionDescription The action description to store.
 */
internal fun storeActionDescription(actionDescription: ActionDescription)
{
    storeJoystickCode("${actionDescription.actionCode.preferenceKey}_joystick", actionDescription.joystickCode)
    PreferencesDatabase.getInt("${actionDescription.actionCode.preferenceKey}_key").value = actionDescription.keyCode
}
