package fr.khelp.zegaime.sounds

/**
 * An exception that is thrown when a sound-related error occurs.
 *
 * **Creation example:**
 * ```kotlin
 * throw SoundException("Failed to load sound", cause)
 * ```
 *
 * @param message The detail message.
 * @param cause The cause of the exception.
 */
class SoundException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
