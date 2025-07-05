# Tasks

The `tasks` package provides tools for working with asynchronous tasks.

## `parallel`

The `parallel` extension function allows you to execute a task in a separate thread and get a `Future` to track its result.

### Usage

You can use `parallel` on a lambda with zero, one, or two parameters.

```kotlin
val future1 = { "Hello" }.parallel()
val future2 = { name: String -> "Hello, $name!" }.parallel("World")
val future3 = { a: Int, b: Int -> a + b }.parallel(2, 3)
```

The `Future` allows you to get the result, check the status, and add callbacks.

```kotlin
future1.onSucceed { result ->
    println(result)
}
```

## `Flow`

A `Flow` is a stream of data that can be observed.

### Usage

You can create a `Flow` using a `FlowSource`.

```kotlin
val flowSource = FlowSource<Int>()
val flow = flowSource.flow
```

Then, you can publish values to the flow.

```kotlin
flowSource.publish(42)
```

And register observers to receive the values.

```kotlin.
flow.register { value ->
    println(value)
}
```

You can also chain flows using the `then` function.

```kotlin
flow.then { "Number: $it" }
    .register {
        println(it)
    }
```

## `Observable`

An `Observable` is a value that can be observed for changes.

### Usage

You can create an `Observable` using an `ObservableSource`.

```kotlin
val observableSource = ObservableSource(0)
val observable = observableSource.observable
```

Then, you can change the value.

```kotlin
observableSource.value = 42
```

And register observers to be notified of the changes.

```kotlin
observable.register { value ->
    println(value)
}
```

[Menu](Menu.md)
