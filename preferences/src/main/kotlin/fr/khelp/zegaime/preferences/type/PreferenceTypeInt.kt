package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.int

/**
 * Represents the type of an integer preference.
 */
data object PreferenceTypeInt : PreferenceType<Int>
{
    /**
     * Serializes an integer value to a string.
     *
     * @param value The integer value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value: Int): String =
        value.toString()

    /**
     * Parses a string to an integer value.
     *
     * @param serialized The string to parse.
     * @return The parsed integer value.
     * @throws IllegalArgumentException If the string cannot be parsed to an integer.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized: String): Int =
        serialized.int()
}