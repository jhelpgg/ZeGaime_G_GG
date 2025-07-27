package fr.khelp.zegaime.preferences.type

import fr.khelp.zegaime.utils.extensions.double

/**
 * Represents the type of a double preference.
 */
data object PreferenceTypeDouble : PreferenceType<Double>
{
    /**
     * Serializes a double value to a string.
     *
     * @param value The double value to serialize.
     * @return The serialized string.
     */
    override fun serialize(value: Double): String =
        value.toString()

    /**
     * Parses a string to a double value.
     *
     * @param serialized The string to parse.
     * @return The parsed double value.
     * @throws IllegalArgumentException If the string cannot be parsed to a double.
     */
    @Throws(IllegalArgumentException::class)
    override fun parse(serialized: String): Double =
        serialized.double()
}