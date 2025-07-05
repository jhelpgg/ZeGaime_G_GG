# Comparators

The `comparators` package provides a `Comparator` for `Comparable` objects.

## `ComparableNaturalOrderComparator`

A `Comparator` that uses the natural order of `Comparable` objects.

### Usage

You can use this comparator with `SortedArray` or any other class that requires a `Comparator`.

```kotlin
val sortedArray = SortedArray<Int>(ComparableNaturalOrderComparator())
```

[Menu](Menu.md)
