package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.long

/**
 * Represents the type of a long preference.
 */
data object PreferenceTypeLong : PreferenceType<Long>
{
    /**
     * Serializes a long value to a string.
     *
     * @param value The long value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value : Long) : String =
        value.toString()

    /**
     * Parses a string to a long value.
     *
     * @param serialized The string to parse.
     * @return The parsed long value.
     * @throws IllegalArgumentException If the string cannot be parsed to a long.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Long =
        serialized.long()
}