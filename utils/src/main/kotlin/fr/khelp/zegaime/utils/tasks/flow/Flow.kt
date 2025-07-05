package fr.khelp.zegaime.utils.tasks.flow

import fr.khelp.zegaime.utils.collections.maps.IntMap
import fr.khelp.zegaime.utils.tasks.TaskContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Flow of information
 *
 * When something produce a flow of data in separate thread:
 * - Creates privately a [FlowSource] and expose its [Flow] via [FlowSource.flow]
 * - [FlowSource.publish] permits to emit data to those that register to [Flow] via [Flow.register] or [Flow.then]
 *
 * To have an instance, use [FlowSource]
 */
class Flow<T : Any> internal constructor()
{
    /** Registered listeners */
    private val elements = IntMap<FlowElement<T>>()

    /** Parent flow, if their one */
    private var flowParent : Flow<*>? = null

    /** Id in parent if [flowParent] not `null` */
    private var idInParent = -1

    /**
     * Creates a flow that transforms this flow result each time its emit and emits the results
     *
     * @param action Action transform the flow results
     *
     * @return Flow result
     */
    fun <R : Any> then(action : (T) -> R) : Flow<R>
    {
        return then(TaskContext.INDEPENDENT, action)
    }

    /**
     * Creates a flow that transforms this flow result each time its emit and emits the results
     *
     * @param taskContext Coroutine context where play the action
     * @param action Action transform the flow results
     *
     * @return Flow result
     */
    fun <R : Any> then(taskContext : TaskContext, action : (T) -> R) : Flow<R>
    {
        val flowSource = FlowSource<R>()
        val flow = flowSource.flow
        val flowElement =
            FlowElement<T>(taskContext.coroutineContext) { value -> flowSource.publish(action(value)) }
        flow.flowParent = this
        flow.idInParent = flowElement.id
        synchronized(this.elements) { this.elements[flowElement.id] = flowElement }
        return flow
    }

    /**
     * Registers an action to do when a value emits
     *
     * @param action Action to do when value emits
     *
     * @return A lambda to call for unregistering the action
     */
    fun register(action : (T) -> Unit) : () -> Unit
    {
        return register(TaskContext.INDEPENDENT, action)
    }

    /**
     * Registers an action to do when a value emits
     *
     * @param taskContext Coroutine context where play the action
     * @param action Action to do when value emits
     *
     * @return A lambda to call for unregistering the action
     */
    fun register(taskContext : TaskContext, action : (T) -> Unit) : () -> Unit
    {
        val flowElement = FlowElement<T>(taskContext.coroutineContext, action)
        synchronized(this.elements) { this.elements[flowElement.id] = flowElement }
        return { cancel(flowElement.id) }
    }

    /**
     * Cancels the link to it flow parent
     *
     * Do something only if flow was created via [then].
     *
     * Beware is children produced via [then] will also no more receive values
     */
    fun cancel()
    {
        this.flowParent?.cancel(this.idInParent)
        this.flowParent = null
        this.idInParent = -1
    }

    /**
     * Publishes a value to listeners
     *
     * @param value Value to publish
     */
    internal fun publish(value : T)
    {
        synchronized(this.elements) {
            runBlocking {
                for (element in this@Flow.elements.values())
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