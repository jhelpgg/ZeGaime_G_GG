package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.render.TextureGif
import fr.khelp.zegaime.engine3d.render.TextureVideo
import fr.khelp.zegaime.resources.Resources

private val textures = HashMap<String, Texture>()

fun Resources.texture(path : String) : Texture =
    textures.getOrPut(path) { Texture(this.image(path)) }

fun Resources.textureVideo(path : String) : TextureVideo =
    textures.getOrPut(path) { TextureVideo(this.video(path)) } as TextureVideo

fun Resources.textureGif(path : String) : TextureGif =
    textures.getOrPut(path) { TextureGif(this.gif(path)) } as TextureGif
