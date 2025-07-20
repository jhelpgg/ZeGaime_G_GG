package fr.khelp.zegaime.engine3d.render

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.images.gif.GIF
import fr.khelp.zegaime.images.gif.GifImage

class TextureGif private constructor(private val gifImage : GifImage) : Texture(gifImage.image),
                                                                        Animation
{
    constructor(gif : GIF) : this(GifImage(gif))

    override fun initialization()
    {
        this.gifImage.startAnimation()
    }

    override fun animate(millisecondsSinceStarted : Long) : Boolean =
        this.gifImage.update()
}