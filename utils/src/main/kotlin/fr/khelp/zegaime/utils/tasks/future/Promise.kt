package fr.khelp.zegaime.utils.tasks.future

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.status.PromiseCanceled
import fr.khelp.zegaime.utils.tasks.future.status.PromiseComplete
import fr.khelp.zegaime.utils.tasks.future.status.PromiseComputing
import fr.khelp.zegaime.utils.tasks.future.status.PromiseStatus
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Promise of a future result
 */
class Promise<T : Any>
{
    /** Future result */
    val future : Future<T> = Future<T>(this)

    /** Promise status observable source */
    private val promiseStatusSource = ObservableSource<PromiseStatus>(PromiseComputing)

    /** Promise status */
    val promiseStatus : Observable<PromiseStatus> = this.promiseStatusSource.observable

    /** Promise listeners */
    private val listeners = ArrayList<Pair<CoroutineContext, () -> Unit>>()

    /**
     * Publish the result
     *
     * @param value Result to publish
     */
    fun result(value : T)
    {
        synchronized(this.listeners)
        {
            if (this.promiseStatusSource.value == PromiseComputing)
            {
                this.future.result(value)
                this.complete(PromiseComplete)
            }
        }
    }

    /**
     * Publish the failure
     *
     * @param exception Failure exception
     */
    fun failure(exception : Exception)
    {
        synchronized(this.listeners)
        {
            if (this.promiseStatusSource.value == PromiseComputing)
            {
                this.future.failure(exception)
                this.complete(PromiseComplete)
            }
        }
    }

    /**
     * Register a listeners called when promise completes, but not canceled
     *
     * @param taskContext Task context where play the action
     * @param action Action to play
     */
    fun onComplete(taskContext : TaskContext, action : () -> Unit)
    {
        synchronized(this.listeners)
        {
            when (this.promiseStatusSource.value)
            {
                PromiseComplete    -> CoroutineScope(taskContext.coroutineContext).launch { action() }
                is PromiseCanceled -> Unit
                PromiseComputing   -> this.listeners.add(taskContext.coroutineContext to {
                    if (this.promiseStatusSource.value == PromiseComplete)
                    {
                        action()
                    }
                })
            }
        }
    }

    /**
     * Register a listeners called when promise completes, but not canceled
     *
     * @param action Action to play
     */
    fun onComplete(action : () -> Unit)
    {
        this.onComplete(TaskContext.INDEPENDENT, action)
    }

    /**
     * Register a listeners called when promise canceled
     *
     * @param taskContext Task context where play the action
     * @param action Action to play
     */
    fun onCancel(taskContext : TaskContext, action : (reason : String) -> Unit)
    {
        synchronized(this.listeners)
        {
            val status = this.promiseStatusSource.value

            when (status)
            {
                PromiseComplete    -> Unit
                is PromiseCanceled -> CoroutineScope(taskContext.coroutineContext).launch { action(status.reason) }
                PromiseComputing   -> this.listeners.add(taskContext.coroutineContext to {
                    val statusComplete = this.promiseStatusSource.value

                    if (statusComplete is PromiseCanceled)
                    {
                        action(statusComplete.reason)
                    }
                })
            }
        }
    }

    /**
     * Register a listeners called when promise canceled
     *
     * @param action Action to play
     */
    fun onCancel(action : (reason : String) -> Unit)
    {
        this.onCancel(TaskContext.INDEPENDENT, action)
    }

    /**
     * Called when promise is canceled
     *
     * @param reason Cancellation reason
     */
    internal fun cancel(reason : String)
    {
        synchronized(this.listeners)
        {
            if (this.promiseStatusSource.value == PromiseComputing)
            {
                this.complete(PromiseCanceled(reason))
            }
        }
    }

    /**
     * Called when promise completes
     *
     * @param status Completion status
     */
    private fun complete(status : PromiseStatus)
    {
        this.promiseStatusSource.value = status

        for ((coroutineContext, action) in this.listeners)
        {
            CoroutineScope(coroutineContext).launch { action() }
        }

        this.listeners.clear()
    }
}