package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.float

/**
 * Preference type Boolean
 */
data object PreferenceTypeFloat : PreferenceType<Float>
{
    override fun serialize(value : Float) : String =
        value.toString()

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Float =
        serialized.float()
}