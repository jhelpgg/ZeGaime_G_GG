package fr.khelp.zegaime.engine3d.events

/**
 * Represents the state of the mouse at a given time.
 *
 * @property mouseStatus The status of the mouse.
 * @property x The X position of the mouse.
 * @property y The Y position of the mouse.
 * @property leftButtonDown Indicates if the left mouse button is down.
 * @property middleButtonDown Indicates if the middle mouse button is down.
 * @property rightButtonDown Indicates if the right mouse button is down.
 * @property shiftDown Indicates if the shift key is down.
 * @property controlDown Indicates if the control key is down.
 * @property altDown Indicates if the alt key is down.
 * @property clicked Indicates if the mouse was clicked.
 * @constructor Creates a new mouse state.
 */
data class MouseState(val mouseStatus : MouseStatus,
                      val x : Int, val y : Int,
                      val leftButtonDown : Boolean, val middleButtonDown : Boolean, val rightButtonDown : Boolean,
                      val shiftDown : Boolean, val controlDown : Boolean, val altDown : Boolean,
                      val clicked : Boolean)