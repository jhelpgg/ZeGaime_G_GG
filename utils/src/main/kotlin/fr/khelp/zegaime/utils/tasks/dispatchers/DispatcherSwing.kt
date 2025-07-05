package fr.khelp.zegaime.utils.tasks.dispatchers

import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable

/**
 * Dispatcher to play thing in Swing UI context
 */
object DispatcherSwing : CoroutineDispatcher()
{
    override fun dispatch(context : CoroutineContext, block : Runnable)
    {
        SwingUtilities.invokeLater(block)
    }
}