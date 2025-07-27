package fr.khelp.zegaime.images.font

/**
 * Represents the possible values for the bold and italic properties of a font.
 */
enum class FontValue
{
    /**
     * Forces the value to be `true` (transforms the font if needed).
     */
    TRUE,

    /**
     * Forces the value to be `false` (transforms the font if needed).
     */
    FALSE,

    /**
     * Uses the value defined in the stream.
     */
    AS_DEFINED
}