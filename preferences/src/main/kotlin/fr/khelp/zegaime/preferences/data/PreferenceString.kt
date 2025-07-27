package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeString

/**
 * Represents a string preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.string` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.string("myPreference", "")
 * myPreference.value = "hello"
 * val currentValue = myPreference.value
 * ```
 *
 * @constructor Creates a new string preference. For internal use only.
 */
class PreferenceString internal constructor(name: String, initialValue: String) :
    Preference<String, PreferenceTypeString>(name, PreferenceTypeString, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue: String, newValue: String): Boolean =
        currentValue != newValue
}