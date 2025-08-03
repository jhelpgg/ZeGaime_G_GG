package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.utils.texture
import fr.khelp.zegaime.engine3d.utils.textureGif
import fr.khelp.zegaime.engine3d.utils.textureVideo
import fr.khelp.zegaime.resources.Resources
import kotlinx.serialization.Serializable

/**
 * Data for store texture information
 *
 * @property relativePath Texture relative path
 * @property textureType Texture type
 */
@Serializable
data class TextureData(val relativePath : String, val textureType : TextureType)
{
    /**
     * Obtains texture from resources
     *
     * @param resources Resources where get the texture
     *
     * @return Got texture
     */
    fun texture(resources : Resources) : Texture =
        when (this.textureType)
        {
            TextureType.IMAGE -> resources.texture(this.relativePath)

            TextureType.GIF   -> resources.textureGif(this.relativePath)

            TextureType.VIDEO -> resources.textureVideo(this.relativePath)
        }
}
