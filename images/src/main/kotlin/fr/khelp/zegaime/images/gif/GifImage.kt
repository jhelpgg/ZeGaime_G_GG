package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.drawImage

/**
 * Represents a GIF image that can be animated.
 *
 * @property gif The GIF to animate.
 * @property image The current image of the animation.
 * @constructor Creates a new GIF image.
 */
class GifImage(private val gif : GIF)
{
    private var lastIndex = 0

    /**
     * The current image of the animation.
     */
    val image = GameImage(this.gif.width, this.gif.height)

    init
    {
        this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(0)) }
    }

    /**
     * Starts the animation.
     */
    fun startAnimation()
    {
        this.lastIndex = 0
        this.gif.startAnimation()
        this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(0)) }
    }

    /**
     * Updates the animation.
     *
     * @return `true` if the animation should continue, `false` otherwise.
     */
    fun update() : Boolean
    {
        if (this.gif.numberOfImage == 1)
        {
            this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(0)) }
            return false
        }

        val currentIndex = this.gif.imageIndexFromStartAnimation()
        val playAgain = currentIndex >= this.lastIndex
        this.lastIndex = currentIndex

        if (playAgain)
        {
            this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(currentIndex)) }
        }
        else
        {
            this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(this.gif.numberOfImage - 1)) }
        }

        return playAgain
    }
}
