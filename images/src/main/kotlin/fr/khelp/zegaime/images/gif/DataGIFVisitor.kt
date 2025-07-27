package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.images.GameImage

/**
 * Visitor for collecting images from a [DataGIF].
 *
 * @see DataGIF.collectImages
 */
interface DataGIFVisitor
{
    /**
     * Called when the collection of images is finished.
     */
    fun endCollecting()

    /**
     * Called when the next image is computed.
     *
     * @param duration The duration of the image in milliseconds.
     * @param image The computed image.
     */
    fun nextImage(duration: Long, image: GameImage)

    /**
     * Called when the collection of images starts.
     *
     * @param width The width of the images.
     * @param height The height of the images.
     */
    fun startCollecting(width: Int, height: Int)
}