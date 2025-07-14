package fr.khelp.zegaime.resources.images

/**
 * Represents a thumbnail of an image to be loaded.
 *
 * @property path The path to the image.
 * @property width The width of the thumbnail.
 * @property height The height of the thumbnail.
 * 
 */
data class ImageLoadThumbnail(val path : String, val width : Int, val height : Int) : ImageDescription
