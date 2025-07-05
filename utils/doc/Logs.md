# Logs

The `logs` package provides a simple logging mechanism.

## `Log`

The `Log` object provides functions for logging messages at different levels.

### Usage

```kotlin
setLevel(LogLevel.DEBUG)

verbose("This is a verbose message")
debug("This is a debug message")
information("This is an information message")
warning("This is a warning message")
error("This is an error message")
exception(RuntimeException("This is an exception"), "An error occurred")
```

You can also use `trace` to print a message with the current stack trace, and `todo` to mark a piece of code that needs to be implemented.

```kotlin
trace("This is a trace message")
todo("Implement this feature")
```

[Menu](Menu.md)
