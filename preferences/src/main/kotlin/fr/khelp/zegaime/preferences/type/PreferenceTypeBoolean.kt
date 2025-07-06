package fr.khelp.zegaime.preferences.type

/**
 * Preference type Boolean
 */
data object PreferenceTypeBoolean : PreferenceType<Boolean> {
    override fun serialize(value: Boolean): String {
        return if (value) "TRUE" else "FALSE"
    }

    @Throws(IllegalArgumentException::class)
    override fun parse(serialized: String): Boolean {
        val trimmed = serialized.trim()
        return when {
            "TRUE".equals(trimmed, ignoreCase = true) -> true
            "FALSE".equals(trimmed, ignoreCase = true) -> false
            else -> throw IllegalArgumentException("$serialized can't be convert to boolean")
        }
    }
}