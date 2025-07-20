package fr.khelp.zegaime.utils.tasks.future

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.status.FutureCanceled
import fr.khelp.zegaime.utils.tasks.future.status.FutureComputing
import fr.khelp.zegaime.utils.tasks.future.status.FutureFailed
import fr.khelp.zegaime.utils.tasks.future.status.FutureSucceed
import java.util.concurrent.atomic.AtomicInteger

/** Simplify [Future] of [Future] */
val <T : Any> Future<Future<T>>.unwrap : Future<T>
    get()
    {
        val promise = Promise<T>()

        val future = this.afterComplete { future ->
            val status = future.futureStatus.value

            when (status)
            {
                is FutureSucceed   ->
                {
                    val futureResult = status.result.afterComplete { futureResult ->
                        val statusResult = futureResult.futureStatus.value

                        when (statusResult)
                        {
                            is FutureSucceed   ->
                                promise.result(statusResult.result)

                            is FutureFailed    ->
                                promise.failure(statusResult.exception)

                            is FutureCanceled  ->
                                promise.future.cancel(statusResult.reason)

                            is FutureComputing -> throw RuntimeException("Should not reach their !")
                        }
                    }

                    promise.onCancel { reason -> futureResult.cancel(reason) }
                }

                is FutureFailed    ->
                    promise.failure(status.exception)

                is FutureCanceled  ->
                    promise.future.cancel(status.reason)

                is FutureComputing -> throw RuntimeException("Should not reach their !")
            }
        }

        promise.onCancel { reason -> future.cancel(reason) }
        return promise.future
    }

/** Creates a future with the object as results */
val <T : Any> T.future : Future<T>
    get()
    {
        val promise = Promise<T>()
        promise.result(this)
        return promise.future
    }

/**
 * Creates a future on failure
 *
 * @return Future on failure
 */
fun <T : Any> Exception.future() : Future<T>
{
    val promise = Promise<T>()
    promise.failure(this)
    return promise.future
}

/**
 * Creates canceled future
 *
 * @return Canceled future
 */
fun <T : Any> String.future() : Future<T>
{
    val promise = Promise<T>()
    promise.future.cancel(this)
    return promise.future
}

/**
 * Combine two futures to one result
 *
 * @param future Future to combine with
 * @param combination Combination to apply to both futures result to compute the final future result
 *
 * @return Future combination
 */
fun <P1 : Any, P2 : Any, R : Any> Future<P1>.combine(future : Future<P2>, combination : (P1, P2) -> R) : Future<R> =
    this.afterComplete { future1 ->
        val status1 = future1.futureStatus.value

        when (status1)
        {
            is FutureSucceed   ->
                future.afterComplete { future2 ->
                    val status2 = future2.futureStatus.value

                    when (status2)
                    {
                        is FutureSucceed   ->
                            try
                            {
                                combination(status1.result, status2.result).future
                            }
                            catch (exception : Exception)
                            {
                                exception.future<R>()
                            }

                        is FutureFailed    ->
                            status2.exception.future<R>()

                        is FutureCanceled  ->
                            status2.reason.future<R>()

                        is FutureComputing ->
                            throw RuntimeException("Should not reach their !!!")
                    }
                }.unwrap

            is FutureFailed    ->
                status1.exception.future<R>()

            is FutureCanceled  ->
                status1.reason.future<R>()

            is FutureComputing ->
                throw RuntimeException("Should not reach their !!!")
        }
    }.unwrap

/**
 * Create a future result that completes when all given futures completes
 */
fun joinAll(futures : List<Future<*>>) : Future<Unit>
{
    val promise = Promise<Unit>()

    when (futures.size)
    {
        0    -> promise.result(Unit)
        1    -> futures[0].afterComplete { promise.result(Unit) }
        else ->
        {
            val left = AtomicInteger(futures.size)
            val action : () -> Unit = {
                if (left.decrementAndGet() == 0)
                {
                    promise.result(Unit)
                }
            }

            for (future in futures)
            {
                future.afterComplete { action() }
            }
        }
    }

    return promise.future
}

/**
 * Create future result that completes when both given futures completes.
 *
 * The result future will have the futures as result to be able know their status and potential result
 */
fun <R1 : Any, R2 : Any> join(future1 : Future<R1>,
                              future2 : Future<R2>) : Future<Pair<Future<R1>, Future<R2>>> =
    join(TaskContext.INDEPENDENT, future1, future2)

/**
 * Create future result that completes when both given futures completes.
 *
 * The result future will have the futures as result to be able know their status and potential result
 */
fun <R1 : Any, R2 : Any> join(context : TaskContext,
                              future1 : Future<R1>,
                              future2 : Future<R2>) : Future<Pair<Future<R1>, Future<R2>>>
{
    val numberLeft = AtomicInteger(2)
    val promise = Promise<Pair<Future<R1>, Future<R2>>>()
    val task : (Future<*>) -> Unit = { _ ->
        if (numberLeft.decrementAndGet() == 0)
        {
            promise.result(Pair(future1, future2))
        }
    }

    future1.afterComplete(context, task)
    future2.afterComplete(context, task)

    return promise.future
}
