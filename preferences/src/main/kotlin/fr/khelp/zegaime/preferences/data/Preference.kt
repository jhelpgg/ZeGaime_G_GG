package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.PreferencesDatabase
import fr.khelp.zegaime.preferences.type.PreferenceType
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Represents a preference to be stored.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.boolean`, `PreferencesDatabase.int`, etc. methods.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.boolean("myPreference", false)
 * myPreference.value = true
 * val currentValue = myPreference.value
 * ```
 *
 * @param T The type of the preference value.
 * @param PT The type of the preference type.
 * @property name The name of the preference.
 * @property type The type of the preference.
 * @property value The current value of the preference.
 * @property valueFlow An observable that emits the value of the preference when it changes.
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
     * Computes whether a current value can be replaced by a new value.
     *
     * @param currentValue The current value to change.
     * @param newValue The new value to replace the current value with.
     * @return `true` if the replacement is allowed, `false` otherwise.
     */
    protected abstract fun allowUpdate(currentValue : T, newValue : T) : Boolean
}
