package fr.khelp.zegaime.security.exception

/**
 * The message for a login/password invalid exception.
 */
const val LOGIN_PASSWORD_INVALID_MESSAGE = "Wrong login or password."

/**
 * An exception that is thrown when a login or password is invalid.
 *
 * **Creation example:**
 * ```kotlin
 * throw LoginPasswordInvalidException()
 * ```
 *
 * @constructor Creates a new login/password invalid exception.
 */
class LoginPasswordInvalidException : Exception(LOGIN_PASSWORD_INVALID_MESSAGE)