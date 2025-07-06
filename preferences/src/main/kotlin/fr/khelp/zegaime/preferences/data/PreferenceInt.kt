package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeInt

class PreferenceInt internal constructor(name : String, initialValue : Int)
    : Preference<Int, PreferenceTypeInt>(name, PreferenceTypeInt, initialValue)
{
    override fun allowUpdate(currentValue : Int, newValue : Int) : Boolean =
        currentValue != newValue
}