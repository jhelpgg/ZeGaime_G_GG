package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeFloat
import fr.khelp.zegaime.utils.math.compare

class PreferenceFloat internal constructor(name : String, initialValue : Float)
    : Preference<Float, PreferenceTypeFloat>(name, PreferenceTypeFloat, initialValue)
{
    override fun allowUpdate(currentValue : Float, newValue : Float) : Boolean =
        compare(currentValue, newValue) != 0
}