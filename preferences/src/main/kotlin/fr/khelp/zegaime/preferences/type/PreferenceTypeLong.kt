package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.long

/**
 * Preference type Boolean
 */
data object PreferenceTypeLong : PreferenceType<Long>
{
    override fun serialize(value : Long) : String =
        value.toString()

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Long =
        serialized.long()
}