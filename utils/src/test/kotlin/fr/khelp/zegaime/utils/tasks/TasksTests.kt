package fr.khelp.zegaime.utils.tasks

import fr.khelp.zegaime.utils.tasks.future.status.FutureCanceled
import fr.khelp.zegaime.utils.tasks.future.status.FutureFailed
import fr.khelp.zegaime.utils.tasks.future.status.FutureSucceed
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TasksTests
{
    @Test
    fun `parallel no parameter`()
    {
        val task : () -> String = { "Hello" }
        val future = task.parallel()
        val status =
            Assertions.assertInstanceOf(FutureSucceed::class.java, future.waitForCompletion()) as FutureSucceed<String>
        Assertions.assertEquals("Hello", status.result)
    }

    @Test
    fun `parallel one parameter`()
    {
        val task = { name : String -> "Hello, $name!" }
        val future = task.parallel("World")
        val status =
            Assertions.assertInstanceOf(FutureSucceed::class.java, future.waitForCompletion()) as FutureSucceed<String>
        Assertions.assertEquals("Hello, World!", status.result)
    }

    @Test
    fun `parallel two parameters`()
    {
        val task = { a : Int, b : Int -> a + b }
        val future = task.parallel(2, 3)
        val status =
            Assertions.assertInstanceOf(FutureSucceed::class.java, future.waitForCompletion()) as FutureSucceed<Int>
        Assertions.assertEquals(5, status.result)
    }

    @Test
    fun `parallel failure`()
    {
        val task = { throw RuntimeException("Failure") }
        val future = task.parallel()
        val status =
            Assertions.assertInstanceOf(FutureFailed::class.java, future.waitForCompletion()) as FutureFailed<Nothing>
        Assertions.assertEquals("Failure", status.exception.message)
    }

    @Test
    fun `parallel cancellation`()
    {
        val executed = AtomicBoolean(false)
        val task = {
            Thread.sleep(1000)
            executed.set(true)
        }
        val future = task.parallel()
        future.cancel("Test")
        val status =
            Assertions.assertInstanceOf(FutureCanceled::class.java, future.waitForCompletion()) as FutureCanceled<Unit>
        Assertions.assertEquals("Test", status.reason)
        Assertions.assertFalse(executed.get())
    }
}
