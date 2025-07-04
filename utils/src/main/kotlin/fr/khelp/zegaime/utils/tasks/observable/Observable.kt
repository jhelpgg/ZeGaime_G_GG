package fr.khelp.zegaime.utils.tasks.observable

import fr.khelp.zegaime.utils.collections.maps.IntMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Observable<T : Any> internal constructor(initialValue : T)
{
    var value : T = initialValue
        private set

    private val elements = IntMap<ObservableElement<T>>()

    internal fun publish(value : T) {
        synchronized(this.elements) {
            runBlocking {
                for (element in this@Observable.elements.values())
                {
                    CoroutineScope(element.coroutineContext).launch {
                        element.action(value)
                    }
                }
            }
        }
    }
}