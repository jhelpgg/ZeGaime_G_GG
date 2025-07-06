package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.int

/**
 * Preference type Boolean
 */
data object PreferenceTypeInt : PreferenceType<Int>
{
    override fun serialize(value : Int) : String =
        value.toString()

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Int =
        serialized.int()
}