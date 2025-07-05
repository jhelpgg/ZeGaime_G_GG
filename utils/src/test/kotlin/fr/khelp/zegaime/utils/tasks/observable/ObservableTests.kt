package fr.khelp.zegaime.utils.tasks.observable

import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.status.FutureSucceed
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class ObservableTests
{
    @Test
    fun `register and publish`()
    {
        val observableSource = ObservableSource(0)
        val observable = observableSource.observable
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        observable.register {
            receivedValue.set(it)
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(0, receivedValue.get())

        val latch2 = CountDownLatch(1)
        observableSource.value = 42
        latch2.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(42, receivedValue.get())
    }

    @Test
    fun `unregister`()
    {
        val observableSource = ObservableSource(0)
        val observable = observableSource.observable
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        val unregister = observable.register {
            receivedValue.set(it)
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
        unregister()
        observableSource.value = 42
        val latch2 = CountDownLatch(1)
        latch2.await(200, TimeUnit.MILLISECONDS)
        Assertions.assertEquals(0, receivedValue.get())
    }

    @Test
    fun `then`()
    {
        val observableSource = ObservableSource(0)
        val observable = observableSource.observable
        val receivedValue = AtomicReference<String>()
        val latch = CountDownLatch(1)

        val future = observable.then { "Number: $it" }
        val status =  Assertions.assertInstanceOf(FutureSucceed::class.java, future.waitForCompletion()) as FutureSucceed<Observable<String>>
        val childObservable = status.result

        childObservable.register {
            receivedValue.set(it)
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Number: 0", receivedValue.get())

        val latch2 = CountDownLatch(1)
        observableSource.value = 42
        latch2.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Number: 42", receivedValue.get())
    }

    @Test
    fun `cancel`()
    {
        val observableSource = ObservableSource(0)
        val observable = observableSource.observable
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        val future = observable.then { it * 2 }
        val status =  Assertions.assertInstanceOf(FutureSucceed::class.java, future.waitForCompletion()) as FutureSucceed<Observable<Int>>
        val childObservable = status.result

        childObservable.register {
            receivedValue.set(it)
            latch.countDown()
        }
        latch.await(1, TimeUnit.SECONDS)

        childObservable.cancel()
        observableSource.value = 42
        val latch2 = CountDownLatch(1)
        latch2.await(200, TimeUnit.MILLISECONDS)
        Assertions.assertEquals(0, receivedValue.get())
    }
}
