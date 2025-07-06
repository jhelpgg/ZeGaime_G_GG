package fr.khelp.zegaime.preferences.type

/**
 * Preference type Boolean
 */
data object PreferenceTypeString : PreferenceType<String>
{
    override fun serialize(value : String) : String =
        value

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : String =
        serialized
}