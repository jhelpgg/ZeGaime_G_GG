package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.ImageFormat
import fr.khelp.zegaime.utils.io.createDirectory
import fr.khelp.zegaime.utils.io.createFile
import java.io.File
import java.io.FileOutputStream

class TexturesK3D(private val directory : File)
{
    init
    {
        if (!this.directory.createDirectory())
        {
            throw IllegalArgumentException("Can't create/obtain directory : ${this.directory.absolutePath}")
        }
    }

    private val textures = HashMap<String, TextureDescription>()

    fun storeImage(name : String, imageFile : File)
    {
        imageFile.copyTo(File(this.directory, name), overwrite = true)
        this.textures[name] = TextureDescription(TextureType.IMAGE, name)
    }

    fun storeImage(name : String, gameImage : GameImage)
    {
        val file = File(this.directory, name)
        file.createFile()
        gameImage.save(FileOutputStream(file), ImageFormat.JPEG)
        this.textures[name] = TextureDescription(TextureType.IMAGE, name)
    }

    fun storeGif(name : String, gifFile : File)
    {
        gifFile.copyTo(File(this.directory, name), overwrite = true)
        this.textures[name] = TextureDescription(TextureType.GIF, name)
    }

    fun storeVideo(name : String, videoFile : File)
    {
        videoFile.copyTo(File(this.directory, name), overwrite = true)
        this.textures[name] = TextureDescription(TextureType.VIDEO, name)
    }

    @Throws(TextureNotFoundException::class)
    fun obtainTextureDescription(name : String) : TextureDescription =
        this.textures[name] ?: throw TextureNotFoundException(name)
}