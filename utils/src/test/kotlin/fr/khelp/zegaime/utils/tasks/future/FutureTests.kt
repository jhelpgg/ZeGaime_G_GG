package fr.khelp.zegaime.utils.tasks.future

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FutureTests
{
    @Test
    fun onSucceed()
    {
        val promise = Promise<String>()
        val future = promise.future
        val latch = CountDownLatch(1)
        val result = AtomicReference<String>()

        future.onSucceed {
            result.set(it)
            latch.countDown()
        }

        promise.result("Success")
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Success", result.get())
    }

    @Test
    fun onFailed()
    {
        val promise = Promise<String>()
        val future = promise.future
        val latch = CountDownLatch(1)
        val error = AtomicReference<Exception>()

        future.onFailed {
            error.set(it)
            latch.countDown()
        }

        val exception = RuntimeException("Failure")
        promise.failure(exception)
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertSame(exception, error.get())
    }

    @Test
    fun onCanceled()
    {
        val promise = Promise<String>()
        val future = promise.future
        val latch = CountDownLatch(1)
        val reason = AtomicReference<String>()

        future.onCanceled {
            reason.set(it)
            latch.countDown()
        }

        future.cancel("Canceled")
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Canceled", reason.get())
    }

    @Test
    fun afterSucceed()
    {
        val promise = Promise<String>()
        val future = promise.future
        val latch = CountDownLatch(1)
        val result = AtomicReference<Int>()

        future.afterSucceed { it.length }
            .onSucceed {
                result.set(it)
                latch.countDown()
            }

        promise.result("Success")
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(7, result.get())
    }

    @Test
    fun afterComplete()
    {
        val promise = Promise<String>()
        val future = promise.future
        val latch = CountDownLatch(1)
        val completed = AtomicBoolean(false)

        future.afterComplete {
            completed.set(true)
            latch.countDown()
        }

        promise.result("Success")
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertTrue(completed.get())
    }

    @Test
    fun unwrap()
    {
        val promise1 = Promise<String>()
        val future1 = promise1.future
        val promise2 = Promise<Future<String>>()
        val future2 = promise2.future
        val latch = CountDownLatch(1)
        val result = AtomicReference<String>()

        future2.unwrap
            .onSucceed {
                result.set(it)
                latch.countDown()
            }

        promise2.result(future1)
        promise1.result("Unwrapped")
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Unwrapped", result.get())
    }
}
