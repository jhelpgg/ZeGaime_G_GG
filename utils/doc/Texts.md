# Texts

The `texts` package provides utilities for working with text.

## `StringCutter`

The `StringCutter` class allows you to cut a string by a specific character.

### Usage

```kotlin
val cutter = StringCutter("a,b,c", ',')
val a = cutter.next() // "a"
val b = cutter.next() // "b"
val c = cutter.next() // "c"
```

## `StringExtractor`

The `StringExtractor` class is a more advanced string parser that can handle separators, string delimiters, and escape characters.

### Usage

```kotlin
val extractor = StringExtractor("Hello world ! 'This is a phrase'")
val hello = extractor.next() // "Hello"
val world = extractor.next() // "world"
val exclamation = extractor.next() // "!"
val phrase = extractor.next() // "This is a phrase"
```

## `replaceHoles` and `replacePlurals`

The `replaceHoles` and `replacePlurals` functions allow you to format strings with placeholders.

### Usage

```kotlin
val text1 = replaceHoles("Hello {0}, welcome to {1}", "John", "New York")
// "Hello John, welcome to New York"

val text2 = replacePlurals("I have {0} cat{0|1|s}", 1)
// "I have 1 cat"

val text3 = replacePlurals("I have {0} cat{0|1|s}", 2)
// "I have 2 cats"
```

[Menu](Menu.md)
