package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeString

class PreferenceString internal constructor(name : String, initialValue : String)
    : Preference<String, PreferenceTypeString>(name, PreferenceTypeString, initialValue)
{
    override fun allowUpdate(currentValue : String, newValue : String) : Boolean =
        currentValue != newValue
}