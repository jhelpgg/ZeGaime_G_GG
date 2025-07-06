package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.double

/**
 * Preference type Boolean
 */
data object PreferenceTypeDouble : PreferenceType<Double>
{
    override fun serialize(value : Double) : String =
        value.toString()

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Double =
        serialized.double()
}