package fr.khelp.zegaime.engine3d.events

/**
 * Describes an action, its mapping to a joystick and a key.
 *
 * This class is for internal use of the event system.
 *
 * @property actionCode The action code.
 * @property joystickCode The joystick code mapped to the action.
 * @property keyCode The key code mapped to the action.
 * @constructor Creates a new action description.
 */
internal class ActionDescription(val actionCode : ActionCode,
                                 var joystickCode : JoystickCode = actionCode.defaultJoystickCode,
                                 var keyCode : Int = actionCode.defaultKeyCode)