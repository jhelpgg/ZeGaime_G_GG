package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeLong

/**
 * Represents a long preference.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `PreferencesDatabase.long` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val myPreference = PreferencesDatabase.long("myPreference", 0L)
 * myPreference.value = 1L
 * val currentValue = myPreference.value
 * ```
 */
class PreferenceLong internal constructor(name : String, initialValue : Long)
    : Preference<Long, PreferenceTypeLong>(name, PreferenceTypeLong, initialValue)
{
    /**
     * Returns `true` if the new value is different from the current value.
     */
    override fun allowUpdate(currentValue : Long, newValue : Long) : Boolean =
        currentValue != newValue
}
