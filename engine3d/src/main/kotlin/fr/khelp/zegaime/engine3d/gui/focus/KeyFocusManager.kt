package fr.khelp.zegaime.engine3d.gui.focus

import fr.khelp.zegaime.engine3d.events.KeyboardManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Manages the key focusable components
 *
 * @property keyboardManager Key board manager to receive keys from it
 */
internal class KeyFocusManager(private val keyboardManager : KeyboardManager)
{
    companion object
    {
        /** Minimum time, in milliseconds, between special keys repeat */
        private const val DELAY_TREATMENT = 128L
    }

    /** Current key focusable component */
    private var keyFocusable : KeyFocusable? = null

    /** Whether focus currently manage */
    private val focusManaged = AtomicBoolean(false)

    /** Callback to invoke for cancel the focus management */
    private var cancelFocusManagement : () -> Unit = {}

    /** Last time a special key was treated */
    private var lastTimeKeyTreated = 0L

    /**
     * Makes a component key focusable gain the key focus
     *
     * @param keyFocusable Component that gains the focus
     */
    fun gainFocus(keyFocusable : KeyFocusable)
    {
        this.keyFocusable = keyFocusable

        if (this.focusManaged.compareAndSet(false, true))
        {
            this.cancelFocusManagement = this.keyboardManager.keyPressed.register(this::keysReceived)
        }
    }

    /**
     * Called when all component key focusable lost focus
     */
    fun lostFocus()
    {
        this.keyFocusable?.onFocusLost()
        this.keyFocusable = null

        if (this.focusManaged.compareAndSet(true, false))
        {
            this.cancelFocusManagement()
        }
    }

    /**
     * Called when a character is typed on keyboard
     *
     * @param character Character typed
     */
    fun receiveCharacter(character : Char)
    {
        this.keyFocusable?.receiveCharacter(character)
    }

    /**
     * Called to report active keys
     *
     * @param keys Active keys list
     */
    private fun keysReceived(keys : IntArray)
    {
        if (System.currentTimeMillis() - this.lastTimeKeyTreated < KeyFocusManager.DELAY_TREATMENT)
        {
            return
        }

        for (keyCode in keys)
        {
            val key = Key.fromKeyCode(keyCode)

            if (key != Key.UNDEFINED)
            {
                this.keyFocusable?.receiveKey(key)
                this.lastTimeKeyTreated = System.currentTimeMillis()
            }
        }
    }
}