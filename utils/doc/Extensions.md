# Extensions

The `extensions` package provides a set of useful extensions for various data types.

## `StringExtensions`

Provides extensions for the `String` class, like `utf8`, `base64`, `regularExpression`, `toLocale`, and more.

### Usage

```kotlin
val utf8 = "hello".utf8
val base64 = "hello".base64
val regex = "hello".regularExpression
val locale = "en_US".toLocale()
```

## `ArrayExtensions`

Provides extensions for arrays, like `string`, `same`, `merge`, and `transform`.

### Usage

```kotlin
val array = arrayOf(1, 2, 3)
val str = array.string()
val same = array.same(arrayOf(1, 2, 3))
val merged = array.merge(arrayOf(3, 4, 5))
val transformed = array.transformArray { it * 2 }
```

## Other extensions

The `extensions` package also provides extensions for `BooleanArray`, `ByteArray`, `CharArray`, `DoubleArray`, `FloatArray`, `IntArray`, `LongArray`, `ShortArray`, `Calendar`, and `StringBuilder`.

[Menu](Menu.md)
