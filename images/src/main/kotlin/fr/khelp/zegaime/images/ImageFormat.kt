package fr.khelp.zegaime.images

/**
 * Represents the supported image formats.
 *
 * @property formatName The name of the format.
 */
enum class ImageFormat(val formatName : String)
{
    /** Joint Photographic Experts Group format. */
    JPEG("JPG"),

    /** Portable Network Graphics format. */
    PNG("PNG"),
}
