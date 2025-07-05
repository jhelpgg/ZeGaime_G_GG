# Regular Expressions

The `regex` package provides a DSL for creating and using regular expressions.

## `RegularExpression`

The `RegularExpression` class is the main entry point for creating regular expressions.

### Creation

You can create a `RegularExpression` from a string, a character, a character array, or a character interval.

```kotlin
val regex1 = "hello".regularExpression
val regex2 = 'a'.regularExpression
val regex3 = charArrayOf('a', 'b', 'c').regularExpression
val regex4 = ('a'..'z').interval.regularExpression
```

You can also use the provided constants for common regular expressions.

```kotlin
val regex = WORD + WHITE_SPACE + DIGIT
```

### Combining expressions

You can combine regular expressions using the `+` and `OR` operators.

```kotlin
val regex = "hello".regularExpression + "world".regularExpression
val regex2 = "hello".regularExpression OR "world".regularExpression
```

### Repetitions

You can specify repetitions using the `zeroOrMore`, `oneOrMore`, `zeroOrOne`, `exactTimes`, `atLeast`, `atMost`, and `between` functions.

```kotlin
val regex = "a".regularExpression.zeroOrMore()
```

### Groups

You can create capturing groups using the `group` function.

```kotlin
val group = "a".regularExpression.group()
val regex = "hello".regularExpression + group
```

### Matching

You can match a string against a regular expression using the `matches` and `matcher` functions.

```kotlin
val matches = regex.matches("helloa")
val matcher = regex.matcher("helloa")
if (matcher.find()) {
    val value = matcher.group(group)
}
```

[Menu](Menu.md)
