package fr.khelp.zegaime.utils.tasks.dispatchers

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

class DispatcherSwingTests
{
    @Test
    fun `dispatch on Swing thread`()
    {
        val latch = CountDownLatch(1)
        val runnable = Runnable {
            Assertions.assertTrue(SwingUtilities.isEventDispatchThread())
            latch.countDown()
        }
        DispatcherSwing.dispatch(EmptyCoroutineContext, runnable)
        latch.await(1, TimeUnit.SECONDS)
    }
}

object EmptyCoroutineContext : kotlin.coroutines.CoroutineContext
{
    override fun <R> fold(initial: R, operation: (R, kotlin.coroutines.CoroutineContext.Element) -> R): R = initial
    override fun <E : kotlin.coroutines.CoroutineContext.Element> get(key: kotlin.coroutines.CoroutineContext.Key<E>): E? = null
    override fun minusKey(key: kotlin.coroutines.CoroutineContext.Key<*>): kotlin.coroutines.CoroutineContext = this
}
