package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeInt

/**
 * Represents a int preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.int` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.int("myPreference", 0)
 * myPreference.value = 1
 * val currentValue = myPreference.value
 * ```
 */
class PreferenceInt internal constructor(name : String, initialValue : Int)
    : Preference<Int, PreferenceTypeInt>(name, PreferenceTypeInt, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue : Int, newValue : Int) : Boolean =
        currentValue != newValue
}