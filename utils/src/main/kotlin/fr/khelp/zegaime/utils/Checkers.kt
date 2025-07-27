package fr.khelp.zegaime.utils

/**
 * Check if an argument respect a condition.
 *
 * If condition failed an [IllegalArgumentException] is throw with given message
 *
 * @param condition The condition to check.
 * @param messageIfFail The message to throw if the condition is not met.
 * @throws IllegalArgumentException if the condition is not met.
 */
@Throws(IllegalArgumentException::class)
inline fun argumentCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw IllegalArgumentException(messageIfFail())
    }
}

/**
 * Check if a state respect a condition.
 *
 * If condition failed an [IllegalStateException] is throw with given message
 *
 * @param condition The condition to check.
 * @param messageIfFail The message to throw if the condition is not met.
 * @throws IllegalStateException if the condition is not met.
 */
@Throws(IllegalStateException::class)
inline fun stateCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw IllegalStateException(messageIfFail())
    }
}

/**
 * Check if a value is not `null`
 *
 * If value is `null` a [NullPointerException] is throw
 *
 * If not `null` it returns the value cast to not `null` version.
 *
 * @param value The value to check.
 * @param messageIfNull The message to throw if the value is null.
 * @return The value if it is not null.
 * @throws NullPointerException if the value is null.
 */
@Throws(NullPointerException::class)
inline fun <T> notNullCheck(value : T?, messageIfNull : () -> String) : T =
    value ?: throw NullPointerException(messageIfNull())

/**
 * Check if a required element exists respect a condition.
 *
 * If condition failed an [NoSuchElementException] is throw with given message
 *
 * @param condition The condition to check.
 * @param messageIfFail The message to throw if the condition is not met.
 * @throws NoSuchElementException if the condition is not met.
 */
@Throws(NoSuchElementException::class)
inline fun elementExistsCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw NoSuchElementException(messageIfFail())
    }
}

/**
 * Asserts that a condition is true.
 *
 * If the condition is false, an [AssertionError] is thrown with the given message.
 *
 * @param condition The condition to check.
 * @param message The message to throw if the condition is not met.
 * @param action The action to execute if the condition is met.
 * @return The result of the action.
 * @throws AssertionError if the condition is not met.
 */
@Throws(AssertionError::class)
inline fun <R> assertion(condition : Boolean, message : String = "Assertion failed", action : () -> R) : R
{
    if (condition.not())
    {
        throw AssertionError(message)
    }

    return action()
}
