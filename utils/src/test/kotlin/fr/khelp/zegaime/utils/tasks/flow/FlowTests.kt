package fr.khelp.zegaime.utils.tasks.flow

import fr.khelp.zegaime.utils.tasks.TaskContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class FlowTests
{
    @Test
    fun `register and publish`()
    {
        val flowSource = FlowSource<Int>()
        val flow = flowSource.flow
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        flow.register {
            receivedValue.set(it)
            latch.countDown()
        }

        flowSource.publish(42)
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(42, receivedValue.get())
    }

    @Test
    fun `unregister`()
    {
        val flowSource = FlowSource<Int>()
        val flow = flowSource.flow
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        val unregister = flow.register {
            receivedValue.set(it)
        }

        unregister()
        flowSource.publish(42)
        latch.await(200, TimeUnit.MILLISECONDS)
        Assertions.assertEquals(0, receivedValue.get())
    }

    @Test
    fun `then`()
    {
        val flowSource = FlowSource<Int>()
        val flow = flowSource.flow
        val receivedValue = AtomicReference<String>()
        val latch = CountDownLatch(1)

        flow.then { "Number: $it" }
            .register {
                receivedValue.set(it)
                latch.countDown()
            }

        flowSource.publish(42)
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals("Number: 42", receivedValue.get())
    }

    @Test
    fun `cancel`()
    {
        val flowSource = FlowSource<Int>()
        val flow = flowSource.flow
        val receivedValue = AtomicInteger()
        val latch = CountDownLatch(1)

        val childFlow = flow.then { it * 2 }
        childFlow.register {
            receivedValue.set(it)
        }

        childFlow.cancel()
        flowSource.publish(42)
        latch.await(200, TimeUnit.MILLISECONDS)
        Assertions.assertEquals(0, receivedValue.get())
    }

    @Test
    fun `multiple subscribers`()
    {
        val flowSource = FlowSource<Int>()
        val flow = flowSource.flow
        val receivedValue1 = AtomicInteger()
        val receivedValue2 = AtomicInteger()
        val latch = CountDownLatch(2)

        flow.register {
            receivedValue1.set(it)
            latch.countDown()
        }

        flow.register {
            receivedValue2.set(it)
            latch.countDown()
        }

        flowSource.publish(42)
        latch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(42, receivedValue1.get())
        Assertions.assertEquals(42, receivedValue2.get())
    }
}
