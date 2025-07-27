package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.io.readByteArray
import fr.khelp.zegaime.utils.io.readInt
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * Reads a video from an input stream.
 *
 * **Usage example:**
 * ```kotlin
 * val video = videoReader(inputStream)
 * ```
 *
 * @param inputStream The input stream to read the video from.
 * @return The video read.
 */
fun videoReader(inputStream: InputStream): Video
{
    val width = inputStream.readInt()
    val height = inputStream.readInt()
    val fps = inputStream.readInt()
    val images = ArrayList<GameImage>()

    var imageData = inputStream.readByteArray()

    while (imageData.isNotEmpty())
    {
        images.add(GameImage.load(ByteArrayInputStream(imageData)))
        imageData = inputStream.readByteArray()
    }

    return Video(width, height, fps, images)
}