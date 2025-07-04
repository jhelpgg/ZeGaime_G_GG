package fr.khelp.zegaime.utils.tasks.observable

/**
 * Source of observable.
 *
 * It is the entry point to create and manage an [Observable]
 * @param initialValue Observable initial value
 * @param T Type of value observed
 * @property observable The created observable
 * @property value The current value in observable
 */
class ObservableSource<T : Any>(initialValue : T)
{
    /** The created observable */
    val observable : Observable<T> = Observable<T>(initialValue)

    /** The current value in observable */
    var value : T
        get() = this.observable.value
        set(value)
        {
            this.observable.publish(value)
        }
}