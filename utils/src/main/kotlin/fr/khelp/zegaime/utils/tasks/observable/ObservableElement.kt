package fr.khelp.zegaime.utils.tasks.observable

import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

/**
 * An element that represents an observer
 * @param coroutineContext Context where observer is trigger
 * @param action Action to play when value changed
 * @param T Type of value observed
 */
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

    /** Observable element ID */
    val id : Int =
        ObservableElement.NEXT_ID.getAndUpdate { current -> if (current < ObservableElement.LIMIT) current + 1 else 0 }
}