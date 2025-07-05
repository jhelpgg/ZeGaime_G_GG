package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Status indicates future is canceled
 *
 * @param reason Cancel reason
 */
data class FutureCanceled<T : Any>(val reason : String) : FutureStatus<T>
