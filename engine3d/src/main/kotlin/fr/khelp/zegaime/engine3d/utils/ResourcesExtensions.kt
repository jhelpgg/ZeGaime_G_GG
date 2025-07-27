package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.render.TextureGif
import fr.khelp.zegaime.engine3d.render.TextureVideo
import fr.khelp.zegaime.resources.Resources

private val textures = HashMap<String, Texture>()

/**
 * Gets a texture from the resources.
 *
 * @param path The path to the texture.
 * @return The texture.
 */
fun Resources.texture(path: String): Texture =
    textures.getOrPut(path) { Texture(this.image(path)) }

/**
 * Gets a video texture from the resources.
 *
 * @param path The path to the video.
 * @return The video texture.
 */
fun Resources.textureVideo(path: String): TextureVideo =
    textures.getOrPut(path) { TextureVideo(this.video(path)) } as TextureVideo

/**
 * Gets a GIF texture from the resources.
 *
 * @param path The path to the GIF.
 * @return The GIF texture.
 */
fun Resources.textureGif(path: String): TextureGif =
    textures.getOrPut(path) { TextureGif(this.gif(path)) } as TextureGif