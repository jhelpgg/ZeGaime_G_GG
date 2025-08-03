package fr.khelp.zegaime.engine3d.format

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.utils.texture
import fr.khelp.zegaime.engine3d.utils.textureGif
import fr.khelp.zegaime.engine3d.utils.textureVideo
import fr.khelp.zegaime.resources.Resources

data class TextureDescription(val type : TextureType, val name : String)
{
    fun fromResources(resources : Resources) : Texture =
        when (this.type)
        {
            TextureType.IMAGE -> resources.texture(FormatK3D.textureRelativePath(this.name))

            TextureType.GIF   -> resources.textureGif(FormatK3D.textureRelativePath(this.name))

            TextureType.VIDEO -> resources.textureVideo(FormatK3D.textureRelativePath(this.name))
        }
}
