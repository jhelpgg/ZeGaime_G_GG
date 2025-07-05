# Injection

The `injection` package provides a simple dependency injection mechanism.

## `inject` and `injected`

The `inject` function allows you to register an instance of a class, and the `injected` delegate allows you to retrieve it later.

### Usage

First, you need to inject an instance of a class.

```kotlin
interface MyService
class MyServiceImpl : MyService

inject<MyService>(MyServiceImpl())
```

Then, you can retrieve the instance using the `injected` delegate.

```kotlin
class MyClass
{
    val myService by injected<MyService>()
}
```

You can also use qualifiers to inject multiple instances of the same class.

```kotlin
inject<MyService>(MyServiceImpl(), "impl1")
inject<MyService>(MyServiceImpl(), "impl2")

class MyClass
{
    val myService1 by injected<MyService>("impl1")
    val myService2 by injected<MyService>("impl2")
}
```

[Menu](Menu.md)
