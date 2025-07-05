package fr.khelp.zegaime.utils.injection

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InjectorTests
{
    interface TestInterface
    class TestImplementation1 : TestInterface
    class TestImplementation2 : TestInterface

    val retrieved by injected<TestInterface>()
    val retrieved1 by injected<TestInterface>("impl1")
    val retrieved2 by injected<TestInterface>("impl2")

    @BeforeEach
    fun setup()
    {
        // Clear injections before each test
        val injectedField =
            Class.forName("fr.khelp.zegaime.utils.injection.InjectorKt").getDeclaredField("injected")
        injectedField.isAccessible = true
        val injectedMap = injectedField.get(null) as HashMap<*, *>
        injectedMap.clear()
    }

    @Test
    fun `inject and retrieve`()
    {
        val instance = TestImplementation1()
        inject<TestInterface>(instance)
        Assertions.assertSame(instance, retrieved)
    }

    @Test
    fun `inject and retrieve with qualifier`()
    {
        val instance1 = TestImplementation1()
        val instance2 = TestImplementation2()
        inject<TestInterface>(instance1, "impl1")
        inject<TestInterface>(instance2, "impl2")
        Assertions.assertSame(instance1, retrieved1)
        Assertions.assertSame(instance2, retrieved2)
    }

    @Test
    fun `isInjected`()
    {
        Assertions.assertFalse(isInjected(TestInterface::class.java))
        inject<TestInterface>(TestImplementation1())
        Assertions.assertTrue(isInjected(TestInterface::class.java))
    }

    @Test
    fun `isInjected with qualifier`()
    {
        Assertions.assertFalse(isInjected(TestInterface::class.java, "impl1"))
        inject<TestInterface>(TestImplementation1(), "impl1")
        Assertions.assertTrue(isInjected(TestInterface::class.java, "impl1"))
        Assertions.assertFalse(isInjected(TestInterface::class.java, "impl2"))
    }

    @Test
    fun `injection not found`()
    {
        Assertions.assertThrows(InjectionNotFoundException::class.java) {
            println(retrieved)
        }
    }
}
