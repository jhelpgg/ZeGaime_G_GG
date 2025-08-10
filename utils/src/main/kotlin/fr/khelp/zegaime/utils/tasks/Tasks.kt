package fr.khelp.zegaime.utils.tasks

import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise
import kotlin.math.max
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
 * @param taskContext Task context to use for the task. By default, an independent thread
 * @return Future to track, the task result
 */
fun <R : Any> (suspend () -> R).parallel(taskContext : TaskContext = TaskContext.INDEPENDENT) : Future<R>
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

suspend fun sleep(delayMilliseconds : Long)
{
    delay(delayMilliseconds)
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

/**
 * Launch a task after a delay
 *
 * @param time Delay in milliseconds
 * @param taskContext Context to launch the task
 * @param action Task to play
 * @return Future to track the task
 */
fun <R : Any> delay(time : Long, taskContext : TaskContext, action : suspend () -> R) : Future<R>
{
    val promise = Promise<R>()

    val job = CoroutineScope(taskContext.coroutineContext).launch {
        delay(max(1L, time))

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

/**
 * Launch a task after a delay
 *
 * @param time Delay in milliseconds
 * @param action Task to play
 * @return Future to track the task
 */
fun <R : Any> delay(time : Long, action : suspend () -> R) : Future<R> =
    delay(time, TaskContext.INDEPENDENT, action)

/**
 * Repeat an action in loop until it canceled.
 *
 * @param delayBeforeLoop Delay in milliseconds before launch the loop
 * @param timeBetweenEachLoop Time in milliseconds between each repeat
 * @param taskContext Task context where runs the action
 * @param action Action to repeat
 *
 * @return Action to call for cancel the repeats
 */
fun repeat(delayBeforeLoop : Long,
           timeBetweenEachLoop : Long,
           taskContext : TaskContext,
           action : suspend () -> Unit) : () -> Unit
{
    val job = CoroutineScope(taskContext.coroutineContext).launch {
        delay(max(1L, delayBeforeLoop))

        while (isActive)
        {
            action()
            delay(max(1L, timeBetweenEachLoop))
        }
    }

    return job::cancel
}

/**
 * Repeat an action in loop until it canceled.
 *
 * @param delayBeforeLoop Delay in milliseconds before launch the loop
 * @param timeBetweenEachLoop Time in milliseconds between each repeat
 * @param action Action to repeat
 *
 * @return Action to call for cancel the repeats
 */
fun repeat(delayBeforeLoop : Long, timeBetweenEachLoop : Long, action : suspend () -> Unit) : () -> Unit =
    repeat(delayBeforeLoop, timeBetweenEachLoop, TaskContext.INDEPENDENT, action)

/**
 * Repeat an action in loop until it canceled.
 *
 * @param timeBetweenEachLoop Time in milliseconds between each repeat
 * @param taskContext Task context where runs the action
 * @param action Action to repeat
 *
 * @return Action to call for cancel the repeats
 */
fun repeat(timeBetweenEachLoop : Long, taskContext : TaskContext, action : suspend () -> Unit) : () -> Unit =
    repeat(0L, timeBetweenEachLoop, taskContext, action)

/**
 * Repeat an action in loop until it canceled.
 *
 * @param timeBetweenEachLoop Time in milliseconds between each repeat
 * @param action Action to repeat
 *
 * @return Action to call for cancel the repeats
 */
fun repeat(timeBetweenEachLoop : Long, action : suspend () -> Unit) : () -> Unit =
    repeat(0L, timeBetweenEachLoop, TaskContext.INDEPENDENT, action)
