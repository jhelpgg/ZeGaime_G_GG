package fr.khelp.zegaime.utils.tasks.future.status

/**
 * Promise status indicates promise canceled
 *
 * @property reason Cancellation reason
 */
data class PromiseCanceled(val reason:String) : PromiseStatus
