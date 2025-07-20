package fr.khelp.zegaime.engine3d

/**
 * Exception happens if the window creation failed
 *
 * @param message Error description message
 * @param cause Cause of the exception
 */
class Window3DCantBeCreatedException(message : String, cause : Throwable = Throwable())
    : RuntimeException(message, cause)