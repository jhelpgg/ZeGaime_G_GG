package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegame.video.Video

/**
 * Represents a video texture.
 *
 * This texture is animated and can be played using the `Video` class.
 *
 * **Creation example:**
 * ```kotlin
 * val video = Video("myVideo.mp4")
 * val textureVideo = TextureVideo(video)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * myMaterial.textureDiffuse = textureVideo
 * video.play()
 * ```
 *
 * @property video The video instance.
 * @constructor Creates a new video texture.
 */
class TextureVideo(val video: Video) : Texture(video.image)