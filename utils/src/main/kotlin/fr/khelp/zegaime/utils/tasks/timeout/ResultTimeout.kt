package fr.khelp.zegaime.utils.tasks.timeout

import fr.khelp.zegaime.utils.tasks.timeout.result.Result
import fr.khelp.zegaime.utils.tasks.timeout.result.ResultComputing
import fr.khelp.zegaime.utils.tasks.timeout.result.ResultFailed
import fr.khelp.zegaime.utils.tasks.timeout.result.ResultIdle
import fr.khelp.zegaime.utils.tasks.timeout.result.ResultSucceed
import fr.khelp.zegaime.utils.tasks.timeout.result.ResultTimeoutExpired
import kotlin.math.max
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Result with timeout
 */
class ResultTimeout<T : Any>
{
    /** Lock for make some threads await */
    private val lock = Object()

    /** Job link yo timeout task */
    private lateinit var jobTimeout : Job

    /** Current result */
    private var result : Result<T> = ResultIdle<T>()

    /**
     * Awaits the result is success, failure or timeout.
     *
     * The caller thread block until the result is success, failure or timeout.
     *
     * @param timeout Timeout in milliseconds
     *
     * @return The result success, failure or timeout
     */
    fun await(timeout : Long = Long.MAX_VALUE) : Result<T> =
        synchronized(this.lock)
        {
            if (this.result is ResultIdle)
            {
                this.result = ResultComputing()
                this.jobTimeout = CoroutineScope(Dispatchers.Default).launch {
                    delay(max(1L, timeout))
                    this@ResultTimeout.complete(ResultTimeoutExpired<T>())
                }
            }

            while (this.result is ResultComputing)
            {
                this.lock.wait()
            }

            this.result
        }

    /**
     * Push a result
     */
    fun result(result : T)
    {
        this.complete(ResultSucceed(result))
    }

    /**
     * Push a failure
     */
    fun fail(exception : Exception)
    {
        this.complete(ResultFailed(exception))
    }

    /**
     * Called when the result complete, that is to say, the result is success, failure or timeout
     *
     * All sleeping threads will be free
     *
     * @param result Result complete state
     */
    private fun complete(result : Result<T>)
    {
        synchronized(this.lock)
        {
            if (this.result is ResultComputing)
            {
                this.jobTimeout.cancel()
                this.result = result
                this.lock.notifyAll()
            }
        }
    }
}