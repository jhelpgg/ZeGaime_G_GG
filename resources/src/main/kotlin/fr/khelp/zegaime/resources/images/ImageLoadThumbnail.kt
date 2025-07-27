package fr.khelp.zegaime.resources.images

/**
 * Represents a thumbnail of an image to be loaded.
 *
 * This class is for internal use of the resources system.
 *
 * @property path The path to the image.
 * @property width The width of the thumbnail.
 * @property height The height of the thumbnail.
 * @constructor Creates a new image load thumbnail description.
 */
data class ImageLoadThumbnail(val path : String, val width : Int, val height : Int) : ImageDescription