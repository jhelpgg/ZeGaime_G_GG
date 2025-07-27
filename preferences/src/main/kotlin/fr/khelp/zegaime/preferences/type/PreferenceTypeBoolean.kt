package fr.khelp.zegaime.preferences.type

/**
 * Represents the type of a boolean preference.
 */
data object PreferenceTypeBoolean : PreferenceType<Boolean>
{
    /**
     * Serializes a boolean value to a string.
     *
     * @param value The boolean value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value : Boolean) : String
    {
        return if (value) "TRUE" else "FALSE"
    }

    /**
     * Parses a string to a boolean value.
     *
     * @param serialized The string to parse.
     * @return The parsed boolean value.
     * @throws IllegalArgumentException If the string cannot be parsed to a boolean.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Boolean
    {
        val trimmed = serialized.trim()
        return when
        {
            "TRUE".equals(trimmed, ignoreCase = true)  -> true
            "FALSE".equals(trimmed, ignoreCase = true) -> false
            else                                       -> throw IllegalArgumentException("$serialized can't be convert to boolean")
        }
    }
}