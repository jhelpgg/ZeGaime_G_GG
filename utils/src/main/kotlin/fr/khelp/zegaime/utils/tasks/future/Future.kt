package fr.khelp.zegaime.utils.tasks.future

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.status.FutureCanceled
import fr.khelp.zegaime.utils.tasks.future.status.FutureComputing
import fr.khelp.zegaime.utils.tasks.future.status.FutureFailed
import fr.khelp.zegaime.utils.tasks.future.status.FutureStatus
import fr.khelp.zegaime.utils.tasks.future.status.FutureSucceed
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Future of a result provide by a [Promise]
 *
 * @property promise Promise source
 */
class Future<T : Any> internal constructor(private val promise : Promise<T>)
{
    /** Future status observable source */
    private val futuresStatusSource = ObservableSource<FutureStatus<T>>(FutureComputing())

    /** Future status */
    val futureStatus : Observable<FutureStatus<T>> = this.futuresStatusSource.observable

    /** Listeners of future events */
    private val listeners = ArrayList<Pair<CoroutineContext, () -> Unit>>()

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterSucceed(taskContext : TaskContext, action : (T) -> R) : Future<R>
    {
        val promise = Promise<R>()

        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        val statusResult = this.futuresStatusSource.value

                        when (statusResult)
                        {
                            is FutureSucceed   ->
                                try
                                {
                                    promise.result(action(statusResult.result))
                                }
                                catch (exception : Exception)
                                {
                                    promise.failure(exception)
                                }

                            is FutureFailed    -> promise.failure(statusResult.exception)
                            is FutureCanceled  -> promise.future.cancel(statusResult.reason)
                            is FutureComputing -> throw RuntimeException("Should not reach their")
                        }
                    })

                is FutureCanceled  ->
                    promise.future.cancel(status.reason)

                is FutureFailed    ->
                    promise.failure(status.exception)

                is FutureSucceed   ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            promise.result(action(status.result))
                        }
                        catch (exception : Exception)
                        {
                            promise.failure(exception)
                        }
                    }
            }
        }

        promise.onCancel { reason -> this.cancel(reason) }
        return promise.future
    }

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterSucceed(action : (T) -> R) : Future<R> =
        this.afterSucceed(TaskContext.INDEPENDENT, action)

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterSucceedUnwrap(taskContext : TaskContext, action : (T) -> Future<R>) : Future<R> =
        this.afterSucceed(taskContext, action).unwrap

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterSucceedUnwrap(action : (T) -> Future<R>) : Future<R> =
        this.afterSucceed(action).unwrap

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     */
    fun onSucceed(taskContext : TaskContext, action : (T) -> Unit)
    {
        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        val statusResult = this.futuresStatusSource.value

                        when (statusResult)
                        {
                            is FutureSucceed   ->
                                try
                                {
                                    action(statusResult.result)
                                }
                                catch (_ : Exception)
                                {
                                }

                            is FutureFailed    -> Unit
                            is FutureCanceled  -> Unit
                            is FutureComputing -> throw RuntimeException("Should not reach their")
                        }
                    })

                is FutureCanceled  -> Unit

                is FutureFailed    -> Unit

                is FutureSucceed   ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            action(status.result)
                        }
                        catch (_ : Exception)
                        {
                        }
                    }
            }
        }
    }

    /**
     * Play an action after the result succeed
     *
     * * If the future already success, action is played as soon as possible
     * * If the future completes because of failure or cancellation, action is not called
     *
     * @param action Action to do
     */
    fun onSucceed(action : (T) -> Unit)
    {
        this.onSucceed(TaskContext.INDEPENDENT, action)
    }

    /**
     * Play an action after the result failed
     *
     * * If the future already failed, action is played as soon as possible
     * * If the future completes because of success or cancellation, action is not called
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     */
    fun onFailed(taskContext : TaskContext, action : (Exception) -> Unit)
    {
        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        val statusResult = this.futuresStatusSource.value

                        when (statusResult)
                        {
                            is FutureSucceed   -> Unit
                            is FutureFailed    ->
                                try
                                {
                                    action(statusResult.exception)
                                }
                                catch (_ : Exception)
                                {
                                }

                            is FutureCanceled  -> Unit
                            is FutureComputing -> throw RuntimeException("Should not reach their")
                        }
                    })

                is FutureCanceled  -> Unit

                is FutureFailed    ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            action(status.exception)
                        }
                        catch (_ : Exception)
                        {
                        }
                    }

                is FutureSucceed   -> Unit
            }
        }
    }

    /**
     * Play an action after the result failed
     *
     * * If the future already failed, action is played as soon as possible
     * * If the future completes because of success or cancellation, action is not called
     *
     * @param action Action to do
     */
    fun onFailed(action : (Exception) -> Unit)
    {
        this.onFailed(TaskContext.INDEPENDENT, action)
    }

    /**
     * Play an action after future is canceled
     *
     * * If the future is already canceled, action is played as soon as possible
     * * If the future completes because of success or failed, action is not called
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     */
    fun onCanceled(taskContext : TaskContext, action : (String) -> Unit)
    {
        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        val statusResult = this.futuresStatusSource.value

                        when (statusResult)
                        {
                            is FutureSucceed   -> Unit
                            is FutureFailed    -> Unit
                            is FutureCanceled  ->
                                try
                                {
                                    action(statusResult.reason)
                                }
                                catch (_ : Exception)
                                {
                                }

                            is FutureComputing -> throw RuntimeException("Should not reach their")
                        }
                    })

                is FutureCanceled  ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            action(status.reason)
                        }
                        catch (_ : Exception)
                        {
                        }
                    }

                is FutureFailed    -> Unit

                is FutureSucceed   -> Unit
            }
        }
    }

    /**
     * Play an action after future is canceled
     *
     * * If the future is already canceled, action is played as soon as possible
     * * If the future completes because of success or failed, action is not called
     *
     * @param action Action to do
     */
    fun onCanceled(action : (String) -> Unit)
    {
        this.onCanceled(TaskContext.INDEPENDENT, action)
    }

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterComplete(taskContext : TaskContext, action : (Future<T>) -> R) : Future<R>
    {
        val promise = Promise<R>()

        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        try
                        {
                            promise.result(action(this))
                        }
                        catch (exception : Exception)
                        {
                            promise.failure(exception)
                        }
                    })

                else               ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            promise.result(action(this@Future))
                        }
                        catch (exception : Exception)
                        {
                            promise.failure(exception)
                        }
                    }
            }
        }

        promise.onCancel { reason -> this.cancel(reason) }
        return promise.future
    }

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterComplete(action : (Future<T>) -> R) : Future<R> =
        this.afterComplete(TaskContext.INDEPENDENT, action)

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterCompleteUnwrap(taskContext : TaskContext, action : (Future<T>) -> Future<R>) : Future<R> =
        this.afterComplete(taskContext, action).unwrap

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param action Action to do
     *
     * @return Future to track the result combination of this future result where applied the action
     */
    fun <R : Any> afterCompleteUnwrap(action : (Future<T>) -> Future<R>) : Future<R> =
        this.afterComplete(action).unwrap

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param taskContext Task context to use to play the action
     * @param action Action to do
     */
    fun onComplete(taskContext : TaskContext, action : (Future<T>) -> Unit)
    {
        synchronized(this.listeners)
        {
            val status = this.futuresStatusSource.value

            when (status)
            {
                is FutureComputing ->
                    this.listeners.add(taskContext.coroutineContext to {
                        try
                        {
                            action(this)
                        }
                        catch (_ : Exception)
                        {
                        }
                    })

                else               ->
                    CoroutineScope(taskContext.coroutineContext).launch {
                        try
                        {
                            action(this@Future)
                        }
                        catch (_ : Exception)
                        {
                        }
                    }
            }
        }
    }

    /**
     * Play an action after the result complete
     *
     * * If the future is already complete, action is played as soon as possible
     *
     * @param action Action to do
     */
    fun onComplete(action : (Future<T>) -> Unit)
    {
        this.onComplete(TaskContext.INDEPENDENT, action)
    }

    /**
     * Cancel the future
     *
     * Does nothing if the future is already complete
     *
     * @param reason Cancellation reason
     */
    fun cancel(reason : String)
    {
        synchronized(this.listeners)
        {
            if (this.futuresStatusSource.value is FutureComputing)
            {
                this.promise.cancel(reason)
                this.onComplete(FutureCanceled<T>(reason))
            }
        }
    }

    /**
     * Called when the result is known
     *
     * @param value Computed result
     */
    internal fun result(value : T)
    {
        synchronized(this.listeners)
        {
            if (this.futuresStatusSource.value is FutureComputing)
            {
                this.onComplete(FutureSucceed<T>(value))
            }
        }
    }

    /**
     * Called when computing failed
     *
     * @param exception Exception that produced/explains the failure
     */
    internal fun failure(exception : Exception)
    {
        synchronized(this.listeners)
        {
            if (this.futuresStatusSource.value is FutureComputing)
            {
                this.onComplete(FutureFailed<T>(exception))
            }
        }
    }

    /**
     * Called when future completes
     *
     * @param futureStatus Completion status
     */
    private fun onComplete(futureStatus : FutureStatus<T>)
    {
        this.futuresStatusSource.value = futureStatus

        for ((coroutineContext, action) in this.listeners)
        {
            CoroutineScope(coroutineContext).launch { action() }
        }

        this.listeners.clear()
    }
}