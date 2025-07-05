# Collections

The `collections` package provides a set of classes and extensions to work with collections.

## `SortedArray`

A sorted array that keeps its elements sorted using a `Comparator`.

It can be configured to accept unique elements only.

### Creation

To create a `SortedArray`, you need to provide a `Comparator`. You can also specify if you want unique elements only.

```kotlin
val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator(), unique = true)
```

### Adding elements

You can add elements to the `SortedArray` using the `+=` operator.

```kotlin
sortedArray += 5
sortedArray += 2
sortedArray += 5 // This will be ignored if unique is true
```

### Removing elements

You can remove elements using the `-=` operator.

```kotlin
sortedArray -= 2
```

## `IntMap`

A map that uses an `Int` as key. It is based on `SortedArray` for performance.

### Creation

To create an `IntMap`, you don't need to provide any parameters.

```kotlin
val intMap = IntMap<String>()
```

### Adding and updating elements

You can add or update elements using the `[]` operator.

```kotlin
intMap[5] = "five"
intMap[2] = "two"
intMap[5] = "new_five"
```

### Removing elements

You can remove elements using the `remove` method.

```kotlin
intMap.remove(2)
```

[Menu](Menu.md)
