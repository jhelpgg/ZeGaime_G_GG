package fr.khelp.zegaime.utils.tasks

import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise
import kotlin.math.max
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Play a task in a separate thread
 *
 * @param taskContext Task context to use for the task. By default, an independent thread
 * @return Future to track, the task result
 */
fun <R : Any> (() -> R).parallel(taskContext : TaskContext = TaskContext.INDEPENDENT) : Future<R>
{
    val promise = Promise<R>()

    val job = CoroutineScope(taskContext.coroutineContext).launch {
        try
        {
            promise.result(this@parallel())
        }
        catch (exception : Exception)
        {
            promise.failure(exception)
        }
    }

    promise.onCancel { reason -> job.cancel(reason) }
    return promise.future
}

/**
 * Play a task in a separate thread
 *
 * @param parameter Parameter to give at the task
 * @param taskContext Task context to use for the task. By default, an independent thread
 * @return Future to track, the task result
 */
fun <P : Any, R : Any> ((P) -> R).parallel(parameter : P,
                                           taskContext : TaskContext = TaskContext.INDEPENDENT) : Future<R> =
    { this(parameter) }.parallel(taskContext)

/**
 * Play a task in a separate thread
 *
 * @param parameter1 First parameter to give at the task
 * @param parameter2 Second parameter to give at the task
 * @param taskContext Task context to use for the task. By default, an independent thread
 * @return Future to track, the task result
 */
fun <P1 : Any, P2 : Any, R : Any> ((P1, P2) -> R).parallel(parameter1 : P1, parameter2 : P2,
                                                           taskContext : TaskContext = TaskContext.INDEPENDENT) : Future<R> =
    { this(parameter1, parameter2) }.parallel(taskContext)

fun <R : Any> delay(time : Long, taskContext : TaskContext, action : () -> R) : Future<R>
{
    val promise = Promise<R>()

    val job = CoroutineScope(taskContext.coroutineContext).launch {
        kotlinx.coroutines.delay(max(1L, time))

        try
        {
            promise.result(action())
        }
        catch (exception : Exception)
        {
            promise.failure(exception)
        }
    }

    promise.onCancel { reason -> job.cancel(reason) }
    return promise.future
}

fun <R : Any> delay(time : Long, action : () -> R) : Future<R> =
    delay(time, TaskContext.INDEPENDENT, action)
