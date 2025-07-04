package fr.khelp.zegaime.utils.tasks.observable

import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

internal class ObservableElement<T : Any>(internal val coroutineContext : CoroutineContext,
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
        ObservableElement.NEXT_ID.getAndUpdate { current -> if (current < ObservableElement.LIMIT) current + 1 else 0 }
}