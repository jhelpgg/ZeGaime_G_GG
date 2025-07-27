package fr.khelp.zegaime.resources.images

/**
 * Represents an image to be loaded.
 *
 * This class is for internal use of the resources system.
 *
 * @property path The path to the image.
 * @constructor Creates a new image load description.
 */
data class ImageLoad(val path: String) : ImageDescription