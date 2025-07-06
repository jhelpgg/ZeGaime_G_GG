package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeLong

class PreferenceLong internal constructor(name : String, initialValue : Long)
    : Preference<Long, PreferenceTypeLong>(name, PreferenceTypeLong, initialValue)
{
    override fun allowUpdate(currentValue : Long, newValue : Long) : Boolean =
        currentValue != newValue
}