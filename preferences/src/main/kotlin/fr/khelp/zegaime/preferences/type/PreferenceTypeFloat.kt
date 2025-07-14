package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.float

/**
 * Represents the type of a float preference.
 * 
 */
data object PreferenceTypeFloat : PreferenceType<Float>
{
    /**
     * Serializes a float value to a string.
     *
     * @param value The float value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value : Float) : String =
        value.toString()

    /**
     * Parses a string to a float value.
     *
     * @param serialized The string to parse.
     * @return The parsed float value.
     * @throws IllegalArgumentException If the string cannot be parsed to a float.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized : String) : Float =
        serialized.float()
}
