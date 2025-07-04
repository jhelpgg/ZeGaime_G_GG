package fr.khelp.zegaime.utils.tasks.observable

import fr.khelp.zegaime.utils.collections.maps.IntMap
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Observable<T : Any> internal constructor(initialValue : T)
{
    var value : T = initialValue
        private set

    private val elements = IntMap<ObservableElement<T>>()
    private var observableParent : Observable<*>? = null

    /** Id in parent if [observableParent] not `null` */
    private var idInParent = -1

    fun <R : Any> then(coroutineContext : CoroutineContext, action : (T) -> R) : Deferred<Observable<R>> =
        CoroutineScope(coroutineContext).async {
            val source = ObservableSource<R>(action(this@Observable.value))
            val observableElement = ObservableElement<T>(coroutineContext) { value -> source.value = action(value) }
            synchronized(this@Observable.elements) {
                this@Observable.elements[observableElement.id] = observableElement
            }
            source.observable.observableParent = this@Observable
            source.observable.idInParent = observableElement.id
            source.observable
        }

    fun <R : Any> then(action : (T) -> R) : Deferred<Observable<R>> =
        this.then(Dispatchers.Default, action)

    fun register(coroutineContext : CoroutineContext, action : (T) -> Unit) : () -> Unit
    {
        val observableElement = ObservableElement<T>(coroutineContext, action)
        synchronized(this@Observable.elements) {
            this@Observable.elements[observableElement.id] = observableElement
        }

        CoroutineScope(coroutineContext).launch {
            action(this@Observable.value)
        }

        return { cancel(observableElement.id) }
    }

    fun register(action : (T) -> Unit) : () -> Unit  =
        this.register(Dispatchers.Default, action)

    /**
     * Cancels the link to it flow parent
     *
     * Do something only if flow was created via [then].
     *
     * Beware is children produced via [then] will also no more receive values
     */
    fun cancel()
    {
        this.observableParent?.cancel(this.idInParent)
        this.observableParent = null
        this.idInParent = -1
    }

    internal fun publish(value : T)
    {
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

    /**
     * Cancels a children flow
     *
     * @param id Flow ID to cancel
     */
    private fun cancel(id : Int)
    {
        synchronized(this.elements) {
            this.elements.remove(id)
        }
    }
}