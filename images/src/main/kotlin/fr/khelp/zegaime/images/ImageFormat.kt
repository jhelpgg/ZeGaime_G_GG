package fr.khelp.zegaime.images

/**
 * Represents the supported image formats.
 *
 * @property formatName The name of the format.
 */
enum class ImageFormat(val formatName : String)
{
    /** Joint Photographic Experts Group format. */
    JPEG("JPEG"),
    /** Portable Network Graphics format. */
    PNG("PNG"),
    /** Graphics Interchange Format. */
    GIF("GIF"),
    /** Bitmap format. */
    BMP("BMP"),
    /** Tagged Image File Format. */
    TIFF("TIFF")
}
