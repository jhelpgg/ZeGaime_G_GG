package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.preferences.PreferencesDatabase

internal class ActionDescription(val actionCode : ActionCode,
                                 var joystickCode : JoystickCode = actionCode.defaultJoystickCode,
                                 var keyCode : Int = actionCode.defaultKeyCode)
