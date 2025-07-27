package fr.khelp.zegaime.utils.tasks.timeout.result

/**
 * Result of [fr.khelp.zegaime.utils.tasks.timeout.ResultTimeout] failed
 *
 * @property exception Failure reason
 */
data class ResultFailed<T : Any>(val exception : Exception) : Result<T>
