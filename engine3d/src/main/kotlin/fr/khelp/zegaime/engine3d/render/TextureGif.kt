package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.images.gif.GIF
import fr.khelp.zegaime.images.gif.GifImage

/**
 * Represents a GIF texture.
 *
 * This texture is animated and can be played using the `AnimationManager`.
 *
 * **Creation example**
 * ```kotlin
 * val resources : Resources = ...
 * val gif = resources.gif("myGif.gif")
 * val textureGif = TextureGif(gif)
 * ```
 *
 * **Standard usage**
 * ```kotlin
 * myMaterial.textureDiffuse = textureGif
 * AnimationManager.play(textureGif)
 * ```
 *
 * @constructor Creates a new GIF texture.
 */
class TextureGif private constructor(private val gifImage : GifImage) : Texture(gifImage.image),
                                                                        Animation
{
    /**
     * Creates a new GIF texture from a GIF instance.
     *
     * @param gif The GIF instance.
     */
    constructor(gif : GIF) : this(GifImage(gif))

    /**
     * Called when the animation is initialized.
     *
     * For internal use only.
     */
    override fun initialization()
    {
        this.gifImage.startAnimation()
    }

    /**
     * Called at each frame of the animation.
     *
     * For internal use only.
     *
     * @param millisecondsSinceStarted The number of milliseconds since the animation started.
     * @return `true` if the animation should continue, `false` otherwise.
     */
    override fun animate(millisecondsSinceStarted : Long) : Boolean =
        this.gifImage.update()
}
