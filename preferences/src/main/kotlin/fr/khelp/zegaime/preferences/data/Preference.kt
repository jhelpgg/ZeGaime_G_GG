package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.PreferencesDatabase
import fr.khelp.zegaime.preferences.type.PreferenceType
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Represents a preference to store
 *
 * @property name Preference name
 * @property type Preference type
 * @param initialValue Preference initial value
 */
sealed class Preference<T : Any, PT : PreferenceType<T>> protected constructor(val name : String,
                                                                               internal val type : PT,
                                                                               initialValue : T)
{
    /** Preference value flow mutable version */
    private val valueFlowMutable = ObservableSource<T>(initialValue)

    /** Preference value flow read only version */
    val valueFlow : Observable<T> = valueFlowMutable.observable

    /** Current preference value */
    var value : T = initialValue
        set(value)
        {
            if (allowUpdate(field, value))
            {
                field = value
                this.valueFlowMutable.value = value
                PreferencesDatabase.update(this)
            }
        }

    /**
     * Computes whether if a current value can be replaced by aa new value
     *
     * @param currentValue Current value to change
     * @param newValue New value for replace the current value
     *
     * @return Whether the replacement is allowed
     */
    protected abstract fun allowUpdate(currentValue : T, newValue : T) : Boolean
}