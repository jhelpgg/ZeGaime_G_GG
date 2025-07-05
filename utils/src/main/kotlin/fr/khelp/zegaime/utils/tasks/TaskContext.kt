package fr.khelp.zegaime.utils.tasks

import fr.khelp.zegaime.utils.tasks.dispatchers.DispatcherSwing
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

/**
 * A task context
 */
enum class TaskContext(internal val coroutineContext : CoroutineContext)
{
    /** Play in the main thread */
    MAIN(Dispatchers.Main),

    /** Play in an independent thread */
    INDEPENDENT(Dispatchers.Default),

    /** Play in a thread dedicated for file/database interaction */
    FILE(Dispatchers.IO),

    /** Play in UI thread */
    UI(DispatcherSwing)
}