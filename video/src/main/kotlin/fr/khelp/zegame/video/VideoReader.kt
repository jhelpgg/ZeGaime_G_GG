package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.io.readByteArray
import fr.khelp.zegaime.utils.io.readInt
import java.io.ByteArrayInputStream
import java.io.InputStream

fun videoReader(inputStream : InputStream) : Video
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