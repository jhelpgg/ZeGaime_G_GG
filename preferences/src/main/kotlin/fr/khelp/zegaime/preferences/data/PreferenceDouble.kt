package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeDouble
import fr.khelp.zegaime.utils.math.compare

/**
 * Represents a double preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.double` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.double("myPreference", 0.0)
 * myPreference.value = 1.0
 * val currentValue = myPreference.value
 * ```
 *
 * @constructor Creates a new double preference. For internal use only.
 */
class PreferenceDouble internal constructor(name: String, initialValue: Double) :
    Preference<Double, PreferenceTypeDouble>(name, PreferenceTypeDouble, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue: Double, newValue: Double): Boolean =
        compare(currentValue, newValue) != 0
}