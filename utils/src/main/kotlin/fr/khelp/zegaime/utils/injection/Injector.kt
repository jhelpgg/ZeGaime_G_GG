package fr.khelp.zegaime.utils.injection

import kotlin.reflect.KProperty

private val injected = HashMap<String, Any>()

/**
 * Public for reified usage, not use it directly outside this file. Use [injected]
 */
class InjectedInternal<I>(private val className : String, private val qualifier : String)
{
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef : Any, property : KProperty<*>) : I
    {
        return injected["${this.className}:${this.qualifier}"]?.let { instance -> instance as I }
               ?: throw InjectionNotFoundException("${this.className}:${this.qualifier}")
    }
}

/**
 * Public for reified usage, not use it directly outside this file. Recommend to use [inject]
 */
fun <I : Any> injectInternal(clazz : Class<I>, instance : I, qualifier : String)
{
    injected["${clazz.name}:$qualifier"] = instance
}

/**
 * Inject an instance to use it later
 *
 * Usages:
 * ```kotlin
 * import com.example.tools.inject
 *
 * // ...
 * // If one instance is required
 * inject<MyInterface>(MyImplementation())
 * // ...
 * // If several instances are required
 * inject<MyInterface2>(MyImplementation2(), "qualifier1")
 * inject<MyInterface2>(MyImplementation3(), "qualifier2")
 * ````
 *
 * The `qualifier` permits to inject different implementations of the same interface
 */
inline fun <reified I : Any> inject(instance : I, qualifier : String = "")
{
    injectInternal(I::class.java, instance, qualifier)
}

/**
 * Get an injected instance
 *
 * Usages:
 * ```kotlin
 * import com.example.tools.injected
 *
 * // ...
 * class MyClass
 * {
 *    // Retrieve the unique instance of MyInterface
 *    val myInterface by injected<MyInterface>()
 *    // Retrieve the instance of MyInterface2 with qualifier "qualifier1"
 *    val myInterface2 by injected<MyInterface2>("qualifier1")
 *    // Retrieve the instance of MyInterface2 with qualifier "qualifier2"
 *    val myInterface3 by injected<MyInterface2>("qualifier2")
 * // ...
 * @throws InjectionNotFoundException if no instance is found for interface and qualifier
 */
@Throws(InjectionNotFoundException::class)
inline fun <reified I : Any> injected(qualifier : String = "") : InjectedInternal<I> =
    InjectedInternal<I>(I::class.java.name, qualifier)

/**
 * Indicates if an instance is provided for interface and qualifier
 */
fun <I : Any> isInjected(clazz : Class<I>, qualifier : String = "") : Boolean =
    "${clazz.name}:$qualifier" in injected
