package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeBoolean

class PreferenceBoolean internal constructor(name : String, initialValue : Boolean)
    : Preference<Boolean, PreferenceTypeBoolean>(name, PreferenceTypeBoolean, initialValue)
{
    override fun allowUpdate(currentValue : Boolean, newValue : Boolean) : Boolean =
        currentValue != newValue
}