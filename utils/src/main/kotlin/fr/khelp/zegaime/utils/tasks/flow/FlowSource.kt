package fr.khelp.zegaime.utils.tasks.flow

/**
 * Source of flow
 *
 * When something produce a flow of data in separate thread:
 * - Creates privately a [FlowSource] and expose its [Flow] via [FlowSource.flow]
 * - [FlowSource.publish] permits to emit data to those that register to [Flow] via [Flow.register] or [Flow.then]
 */
class FlowSource<T : Any>
{
    /** Flow to expose */
    val flow = Flow<T>()

    /**
     * Publish a value in the flow
     *
     * @param value Value to publish
     */
    fun publish(value : T)
    {
        this.flow.publish(value)
    }
}