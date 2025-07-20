package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.utils.tasks.flow.Flow
import fr.khelp.zegaime.utils.tasks.flow.FlowSource
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise
import fr.khelp.zegaime.utils.tasks.future.future
import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import org.lwjgl.glfw.GLFW

class KeyboardManager internal constructor()
{
    private val mutexCapture = Mutex()
    private var nextKeyCode : Promise<Int>? = null

    /**Current active key codes*/
    private val activeKeys = HashSet<Int>()
    private val keyPressedSource = FlowSource<IntArray>()
    val keyPressed : Flow<IntArray> = this.keyPressedSource.flow

    /**
     * Capture the next key typed
     *
     * @return Future that will contains the next key code typed
     */
    fun captureKeyCode() : Future<Int> =
        this.mutexCapture {
            if (this.nextKeyCode == null)
            {
                this.nextKeyCode = Promise<Int>()
            }

            this.nextKeyCode?.future ?: Exception("Fail to capture key code").future()
        }

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

    internal fun reportKeys()
    {
        synchronized(this.activeKeys) { this.keyPressedSource.publish(this.activeKeys.toIntArray()) }
    }
}