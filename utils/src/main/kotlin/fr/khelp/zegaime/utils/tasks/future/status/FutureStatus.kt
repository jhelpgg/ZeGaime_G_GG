package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Future status
 *
 *  | Implementation | Description |
 *  |-----|-----|
 *  | [FutureComputing] | Initial status and still until future complete |
 *  | [FutureSucceed] | When future completes with success |
 *  | [FutureFailed] | When future completes with failure |
 *  | [FutureCanceled] | When future completes due a cancellation |
 */
sealed interface FutureStatus<T : Any>

