package fr.khelp.zegaime.utils.tasks.timeout.result

/**
 * Result of [fr.khelp.zegaime.utils.tasks.timeout.ResultTimeout] succeed
 *
 * @property result Result computed
 */
data class ResultSucceed<T : Any>(val result : T) : Result<T>
