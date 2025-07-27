package fr.khelp.zegaime.images.raster

import fr.khelp.zegaime.images.GameImage

/**
 * Represents a raster image.
 *
 * Each raster image type has its own bit resolution and way to parse it.
 */
interface RasterImage
{
    /**
     * Clears the image.
     */
    fun clear()

    /**
     * Returns the width of the image.
     */
    fun width(): Int

    /**
     * Returns the height of the image.
     */
    fun height(): Int

    /**
     * Returns the type of the image.
     *
     * @return The type of the image.
     */
    fun imageType(): RasterImageType

    /**
     * Converts the image to a [GameImage].
     *
     * @return The converted image.
     */
    fun toGameImage(): GameImage
}