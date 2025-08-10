package fr.khelp.zegaime.engine3d.gui.focus

import fr.khelp.zegaime.engine3d.events.KEY_BACKSPACE
import fr.khelp.zegaime.engine3d.events.KEY_DELETE
import fr.khelp.zegaime.engine3d.events.KEY_DOWN
import fr.khelp.zegaime.engine3d.events.KEY_END
import fr.khelp.zegaime.engine3d.events.KEY_ENTER
import fr.khelp.zegaime.engine3d.events.KEY_HOME
import fr.khelp.zegaime.engine3d.events.KEY_LEFT
import fr.khelp.zegaime.engine3d.events.KEY_PAGE_DOWN
import fr.khelp.zegaime.engine3d.events.KEY_PAGE_UP
import fr.khelp.zegaime.engine3d.events.KEY_RIGHT
import fr.khelp.zegaime.engine3d.events.KEY_TAB
import fr.khelp.zegaime.engine3d.events.KEY_UNKNOWN
import fr.khelp.zegaime.engine3d.events.KEY_UP

/**
 * Action keys in text
 *
 * @property keyCode Associated key code
 */
enum class Key(val keyCode : Int)
{
    UNDEFINED(KEY_UNKNOWN),

    UP(KEY_UP),
    DOWN(KEY_DOWN),
    LEFT(KEY_LEFT),
    RIGHT(KEY_RIGHT),

    PAGE_UP(KEY_PAGE_UP),
    PAGE_DOWN(KEY_PAGE_DOWN),

    DELETE(KEY_DELETE),
    BACKSPACE(KEY_BACKSPACE),
    TAB(KEY_TAB),
    ENTER(KEY_ENTER),

    HOME(KEY_HOME),
    END(KEY_END)

    ;

    companion object
    {
        fun fromKeyCode(keyCode : Int) : Key =
            Key.entries.firstOrNull { key -> key.keyCode == keyCode } ?: Key.UNDEFINED
    }
}