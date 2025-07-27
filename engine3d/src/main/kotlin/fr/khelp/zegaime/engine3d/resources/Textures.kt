package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.engine3d.render.Texture
import fr.khelp.zegaime.engine3d.utils.texture
import fr.khelp.zegaime.images.GameImage

/**
 * Represents a texture resource.
 *
 * @property path The path to the texture.
 * @constructor Creates a new texture resource.
 */
enum class Textures(private val path : String)
{
    /**
     * Body costume texture.
     */
    BODY_COSTUME("textures/BodyCostume.png"),

    /**
     * Dice texture.
     */
    DICE("textures/Dice.png"),

    /**
     * Hair texture.
     */
    HAIR("textures/hair.png"),

    /**
     * Emerald texture.
     */
    EMERALD("textures/emerald.png"),

    /**
     * Rock texture.
     */
    ROCK("textures/TextureRock.png"),

    /**
     * Tile texture.
     */
    TILE("textures/tile.png")

    ;

    /**
     * The game image of the texture.
     */
    val image : GameImage get() = Resources3D.resources.image(this.path)

    /**
     * The texture instance.
     */
    val texture : Texture get() = Resources3D.resources.texture(this.path)
}