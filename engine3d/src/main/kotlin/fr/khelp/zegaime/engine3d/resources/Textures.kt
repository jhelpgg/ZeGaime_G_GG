package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.utils.texture
import fr.khelp.zegaime.images.GameImage

enum class Textures(private val path : String)
{
    BODY_COSTUME("textures/BodyCostume.png"),
    DICE("textures/Dice.png"),
    HAIR("textures/hair.png"),
    EMERALD("textures/emerald.png"),
    ROCK("textures/TextureRock.png"),
    TILE("textures/tile.png")

    ;

    val image : GameImage get() = Resources3D.resources.image(this.path)

    val texture : Texture get() = Resources3D.resources.texture(this.path)
}
