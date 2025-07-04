package fr.khelp.zegaime.utils.tasks.flow

import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

/**
 * Flow element to store a listener and the coroutine context where play it
 *
 * @property coroutineContext Coroutine context where play the listener
 * @property action Stored listener
 */
internal class FlowElement<T : Any>(internal val coroutineContext : CoroutineContext,
                                    internal val action : (T) -> Unit)
{
    companion object
    {
        /** ID limit before loop */
        private const val LIMIT = Int.MAX_VALUE - 1

        /** Next ID number */
        private val NEXT_ID = AtomicInteger(0)
    }

    /** Flow element ID */
    val id : Int =
        FlowElement.NEXT_ID.getAndUpdate { current -> if (current < FlowElement.LIMIT) current + 1 else 0 }
}
