package fr.khelp.zegaime.preferences.type

/**
 * Preference type
 *
 * **Important**: All implementations **MUST** be singleton defined by `object`
 */
sealed interface PreferenceType<T : Any> {
    /**
     * Serialized a value to String
     *
     * The result must be reverse by [parse] to get the original value
     *
     * @param value Value to parse
     *
     * @return Serialized version
     */
    fun serialize(value: T): String

    /**
     * Parse a String serialized by [serialize] to retrieve the original value
     *
     * @param serialized Serialized value
     *
     * @return Original value
     *
     * @throws IllegalArgumentException If the serialized can't be parsed
     */
    @Throws(IllegalArgumentException::class)
    fun parse(serialized: String): T
}