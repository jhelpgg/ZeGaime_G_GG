package fr.khelp.zegaime.utils.tasks.observable

class ObservableSource<T : Any>(initialValue : T)
{
    val observable : Observable<T> = Observable<T>(initialValue)

    var value : T
        get() = this.observable.value
        set(value)
        {
            this.observable.publish(value)
        }
}