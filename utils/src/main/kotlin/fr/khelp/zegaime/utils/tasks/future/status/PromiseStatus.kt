package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Promise status
 *
 *  | Implementation | Description |
 *  |-----|-----|
 *  | [PromiseComputing] | Initial status until promise complete |
 *  | [PromiseComplete] | Status when complete, not canceled |
 *  | [PromiseCanceled] | Status when complete due a cancellation |
 */
sealed interface PromiseStatus
