package fr.khelp.zegaime.preferences.type

/**
 * Represents the type of a string preference.
 */
data object PreferenceTypeString : PreferenceType<String>
{
    /**
     * Serializes a string value to a string.
     *
     * @param value The string value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value: String): String =
        value

    /**
     * Parses a string to a string value.
     *
     * @param serialized The string to parse.
     * @return The parsed string value.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized: String): String =
        serialized
}