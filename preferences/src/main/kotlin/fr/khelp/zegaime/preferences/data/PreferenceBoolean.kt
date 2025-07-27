package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeBoolean

/**
 * Represents a boolean preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.boolean` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.boolean("myPreference", false)
 * myPreference.value = true
 * val currentValue = myPreference.value
 * ```
 *
 * @constructor Creates a new boolean preference. For internal use only.
 */
class PreferenceBoolean internal constructor(name : String, initialValue : Boolean) :
    Preference<Boolean, PreferenceTypeBoolean>(name, PreferenceTypeBoolean, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue : Boolean, newValue : Boolean) : Boolean =
        currentValue != newValue
}