package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Status indicates future as failed
 *
 * @param exception Failure exception
 */
data class FutureFailed<T : Any>(val exception : Exception) : FutureStatus<T>
