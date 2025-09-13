package fr.khelp.zegaime.game.textures

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.rgb
import fr.khelp.zegaime.images.drawText
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.images.setColor

/** Image with number */
val numberedImage : GameImage by lazy {
    val image = GameImage(512, 512)
    val font = JHelpFont("Monospaced", 12)

    image.draw { graphics2D ->
        for (y in 0 until 8)
        {
            for (x in 0 until 8)
            {
                graphics2D.setColor(rgb(x * 32, 128, y * 32))
                graphics2D.fillRect(x * 64, y * 64, 64, 64)

                graphics2D.setColor(WHITE)
                graphics2D.font = font.font
                graphics2D.drawText(x * 64, y * 64 + 32, "${x * 64 + 32}, ${y * 64 + 32}", TextAlignment.CENTER)

                graphics2D.setColor(BLACK)
                graphics2D.drawRect(x * 64, y * 64, 64, 64)
            }
        }
    }

    image
}

/** Texture with number */
val numberedTexture : Texture by lazy {
    Texture(numberedImage)
}