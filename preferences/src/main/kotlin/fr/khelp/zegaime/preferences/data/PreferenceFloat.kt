package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeFloat
import fr.khelp.zegaime.utils.math.compare

/**
 * Represents a float preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.float` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.float("myPreference", 0.0f)
 * myPreference.value = 1.0f
 * val currentValue = myPreference.value
 * ```
 *
 * @constructor Creates a new float preference. For internal use only.
 */
class PreferenceFloat internal constructor(name : String, initialValue : Float) :
    Preference<Float, PreferenceTypeFloat>(name, PreferenceTypeFloat, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue : Float, newValue : Float) : Boolean =
        compare(currentValue, newValue) != 0
}