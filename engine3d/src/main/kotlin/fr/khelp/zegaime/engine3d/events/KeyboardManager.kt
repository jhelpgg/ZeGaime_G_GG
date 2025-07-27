package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise
import fr.khelp.zegaime.utils.tasks.future.future
import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import org.lwjgl.glfw.GLFW

/**
 * Manages the keyboard events.
 *
 * This class is responsible for detecting the keyboard events and publishing them as a flow of key codes.
 * It also allows to capture the next key typed.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `Window3D` class.
 *
 * **Standard usage:**
 * ```kotlin
 * val keyboardManager = window3D.keyboardManager
 * keyboardManager.keyPressed.observedBy { keyCodes ->
 *     if(KEY_UP in keyCodes) {
 *          // Do up action
 *     }

 *     if(KEY_LEFT in keyCodes) {
 *          // Do left action
 *     }
 *
 *     // ...
 * }
 * ```
 *
 * @property keyPressed A flow that emits the currently pressed key codes.
 * @constructor Creates a new keyboard manager. For internal use only.
 */
class KeyboardManager internal constructor()
{
    private val mutexCapture = Mutex()
    private var nextKeyCode : Promise<Int>? = null

    /**Current active key codes*/
    private val activeKeys = HashSet<Int>()
    private val keyPressedSource = FlowSource<IntArray>()

    /**
     * A flow that emits the currently pressed key codes.
     */
    val keyPressed : Flow<IntArray> = this.keyPressedSource.flow

    /**
     * Capture the next key typed.
     *
     * @return A future that will contain the next key code typed.
     */
    fun captureKeyCode() : Future<Int> =
        this.mutexCapture {
            if (this.nextKeyCode == null)
            {
                this.nextKeyCode = Promise<Int>()
            }

            this.nextKeyCode?.future ?: Exception("Fail to capture key code").future()
        }

    /**
     * Called when a key event occurs.
     *
     * For internal use only.
     *
     * @param keyCode The key code.
     * @param action The action type.
     */
    internal fun keyEvent(keyCode : Int, action : Int)
    {
        if (action == GLFW.GLFW_PRESS)
        {
            val consumed = this.mutexCapture {
                if (this.nextKeyCode != null)
                {
                    this.nextKeyCode?.result(keyCode)
                    this.nextKeyCode = null
                    this.activeKeys.remove(keyCode)
                    true
                }
                else
                {
                    false
                }
            }

            if (consumed)
            {
                return
            }

            synchronized(this.activeKeys) { this.activeKeys.add(keyCode) }
        }
        else if (action == GLFW.GLFW_RELEASE)
        {
            synchronized(this.activeKeys) { this.activeKeys.remove(keyCode) }
        }
    }

    /**
     * Reports the currently pressed keys.
     *
     * For internal use only.
     */
    internal fun reportKeys()
    {
        synchronized(this.activeKeys) { this.keyPressedSource.publish(this.activeKeys.toIntArray()) }
    }
}
