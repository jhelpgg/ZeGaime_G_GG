package fr.khelp.zegaime.images.raster

import fr.khelp.zegaime.images.bmp.BitmapHeader

/**
 * Represents the type of a [RasterImage].
 */
enum class RasterImageType
{
    /**
     * Raster image 1 bit / binary image
     */
    IMAGE_BINARY,

    /**
     * Raster image 4 bits
     */
    IMAGE_4_BITS,

    /**
     * Raster image 8 bits
     */
    IMAGE_8_BITS,

    /**
     * Raster image 16 bits
     */
    IMAGE_16_BITS,

    /**
     * Raster image 24 bits
     */
    IMAGE_24_BITS,

    /**
     * Raster image 32 bits
     */
    IMAGE_32_BITS,

    /**
     * JHelp image
     */
    GAME_IMAGE;

    companion object
    {
        /**
         * Returns a raster image type by its number of bits: 1, 4, 8, 16, 24 OR 32.
         *
         * @param bitCount The number of bits.
         * @return The raster image type, or `null` if it is not defined for the given number of bits.
         */
        fun getRasterImageType(bitCount : Int) =
            when (bitCount)
            {
                BitmapHeader.IMAGE_BINARY  -> RasterImageType.IMAGE_BINARY
                BitmapHeader.IMAGE_4_BITS  -> RasterImageType.IMAGE_4_BITS
                BitmapHeader.IMAGE_8_BITS  -> RasterImageType.IMAGE_8_BITS
                BitmapHeader.IMAGE_16_BITS -> RasterImageType.IMAGE_16_BITS
                BitmapHeader.IMAGE_24_BITS -> RasterImageType.IMAGE_24_BITS
                BitmapHeader.IMAGE_32_BITS -> RasterImageType.IMAGE_32_BITS
                else                       -> null
            }
    }
}