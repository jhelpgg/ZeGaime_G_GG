package fr.khelp.zegaime.utils.tasks.observable

import fr.khelp.zegaime.utils.collections.maps.IntMap
import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.parallel
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Observable of a value
 *
 * It is a value that can be observed. When the value changed, observers are alerted
 *
 * To create an instance, use [ObservableSource]
 * @param T Type of value observed
 * @property value Current value
 */
class Observable<T : Any> internal constructor(initialValue : T)
{
    /** Current value */
    var value : T = initialValue
        private set

    /** List of registered observers */
    private val elements = IntMap<ObservableElement<T>>()

    /** Parent observable, if any */
    private var observableParent : Observable<*>? = null

    /** Id in parent if [observableParent] not `null` */
    private var idInParent = -1

    /**
     * Create an observable that its value is a transformation of this observable value.
     *
     * The transformation is done in given [CoroutineContext]
     * @param taskContext Context where transformation is done
     * @param action Transformation action
     * @return Future that will contains the created observable
     */
    fun <R : Any> then(taskContext : TaskContext, action : (T) -> R) : Future<Observable<R>> =
        {
            val source = ObservableSource<R>(action(this@Observable.value))
            val observableElement =
                ObservableElement<T>(taskContext.coroutineContext) { value -> source.value = action(value) }
            synchronized(this@Observable.elements) {
                this@Observable.elements[observableElement.id] = observableElement
            }
            source.observable.observableParent = this@Observable
            source.observable.idInParent = observableElement.id
            source.observable
        }.parallel(taskContext)

    /**
     * Create an observable that its value is a transformation of this observable value
     * @param action Transformation action
     * @return Future that will contains the created observable
     */
    fun <R : Any> then(action : (T) -> R) : Future<Observable<R>> =
        this.then(TaskContext.INDEPENDENT, action)

    /**
     * Register an observer of value changes
     *
     * The action will be trigger in the given context
     * @param taskContext Context where action is executed
     * @param action Action to execute when value changed
     * @return A function to call to unregister the observer
     */
    fun register(taskContext : TaskContext, action : (T) -> Unit) : () -> Unit
    {
        val observableElement = ObservableElement<T>(taskContext.coroutineContext, action)
        synchronized(this@Observable.elements) {
            this@Observable.elements[observableElement.id] = observableElement
        }

        CoroutineScope(taskContext.coroutineContext).launch {
            action(this@Observable.value)
        }

        return { cancel(observableElement.id) }
    }

    /**
     * Register an observer of value changes
     * @param action Action to execute when value changed
     * @return A function to call to unregister the observer
     */
    fun register(action : (T) -> Unit) : () -> Unit =
        this.register(TaskContext.INDEPENDENT, action)

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

    /**
     * Publish a new value to observers
     * @param value New value
     */
    internal fun publish(value : T)
    {
        synchronized(this.elements) {
            this.value = value

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
     * Cancels a children observable
     *
     * @param id Observable ID to cancel
     */
    private fun cancel(id : Int)
    {
        synchronized(this.elements) {
            this.elements.remove(id)
        }
    }
}
