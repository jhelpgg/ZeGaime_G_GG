package fr.khelp.zegaime.utils.tasks.flow

import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import java.util.concurrent.atomic.AtomicReference

fun <T1 : Any, T2 : Any, R : Any> flowJoin(firstFlow : Flow<T1>,
                                           secondFlow : Flow<T2>,
                                           combiner : (T1, T2) -> R) : Flow<R>
{
    val mutex = Mutex()
    val combination = FlowSource<R>()
    val firstResult = AtomicReference<T1>(null)
    val secondResult = AtomicReference<T2>(null)

    val combine : () -> Unit =
        {
            mutex {
                val first = firstResult.get()
                val second = secondResult.get()

                if (first != null && second != null)
                {
                    combination.publish(combiner(first, second))
                }
            }
        }

    firstFlow.register { value ->
        firstResult.set(value)
        combine()
    }

    secondFlow.register { value ->
        secondResult.set(value)
        combine()
    }

    return combination.flow
}