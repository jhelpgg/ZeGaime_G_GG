package fr.khelp.zegaime.preferences.data

import fr.khelp.zegaime.preferences.type.PreferenceTypeDouble
import fr.khelp.zegaime.utils.math.compare

class PreferenceDouble internal constructor(name : String, initialValue : Double)
    : Preference<Double, PreferenceTypeDouble>(name, PreferenceTypeDouble, initialValue)
{
    override fun allowUpdate(currentValue : Double, newValue : Double) : Boolean =
        compare(currentValue, newValue) != 0
}