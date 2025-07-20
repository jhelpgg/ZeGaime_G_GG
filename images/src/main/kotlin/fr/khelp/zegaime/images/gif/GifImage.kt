package fr.khelp.zegaime.images.gif

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.drawImage

class GifImage(private val gif : GIF)
{
    private var lastIndex = 0
    val image = GameImage(this.gif.width, this.gif.height)

    init
    {
        this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(0)) }
    }

    fun startAnimation()
    {
        this.lastIndex = 0
        this.gif.startAnimation()
        this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, this.gif.image(0)) }
    }

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