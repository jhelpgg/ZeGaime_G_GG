package fr.khelp.zegaime.preferences.type

/**
 * Represents the type of a preference.
 *
 * **Important**: All implementations **MUST** be singletons defined by `object`.
 *
 * @param T The type of the preference value.
 */
sealed interface PreferenceType<T : Any>
{
    /**
     * Serializes a value to a string.
     *
     * The result must be reversible by [parse] to get the original value.
     *
     * @param value The value to serialize.
     * @return The serialized version.
     */
    fun serialize(value: T): String

    /**
     * Parses a string serialized by [serialize] to retrieve the original value.
     *
     * @param serialized The serialized value.
     * @return The original value.
     * @throws IllegalArgumentException If the serialized value cannot be parsed.
     */
    @Throws(IllegalArgumentException::class)
    fun parse(serialized: String): T
}