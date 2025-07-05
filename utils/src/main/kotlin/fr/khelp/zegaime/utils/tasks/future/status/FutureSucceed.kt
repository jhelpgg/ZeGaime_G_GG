package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Status indicates future succeed
 *
 * @param result Future result
 */
data class FutureSucceed<T : Any>(val result : T) : FutureStatus<T>
