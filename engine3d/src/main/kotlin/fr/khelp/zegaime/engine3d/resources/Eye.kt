package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.images.GameImage

/**
 * Represents an eye texture.
 *
 * @property path The path to the eye texture.
 * @constructor Creates a new eye.
 */
enum class Eye(private val path: String)
{
    /**
     * Blue eye 1.
     */
    BLUE_1("eyes/eyeBlue.png"),
    /**
     * Blue eye 2.
     */
    BLUE_2("eyes/eyeBlue2.png"),
    /**
     * Blue eye 3.
     */
    BLUE_3("eyes/eyeBlue3.png"),
    /**
     * Brown eye 1.
     */
    BROWN_1("eyes/eyeBrown.png"),
    /**
     * Brown eye 2.
     */
    BROWN_2("eyes/eyeBrown2.png"),
    /**
     * Green eye 1.
     */
    GREEN_1("eyes/eyeGreen.png"),
    /**
     * Green eye 2.
     */
    GREEN_2("eyes/eyeGreen2.png"),
    /**
     * Green eye 3.
     */
    GREEN3("eyes/eyeGreen3.png"),
    /**
     * Green eye with shine.
     */
    GREEN_SHINE("eyes/eyeGreenShine.png"),
    /**
     * Light red eye.
     */
    LIGHT_RED("eyes/eyeLightRed.png"),
    /**
     * Pink eye.
     */
    PINK("eyes/eyePink.png"),
    /**
     * Purple eye.
     */
    PURPLE("eyes/eyePurple.png"),
    /**
     * Red eye 1.
     */
    RED_1("eyes/eyeRed.png"),
    /**
     * Red eye 2.
     */
    RED_2("eyes/eyeRed2.png"),
    /**
     * Red eye 3.
     */
    RED_3("eyes/eyeRed3.png"),
    /**
     * Tone blue eye.
     */
    TONE_BLUE("eyes/eyeToneBlue.png"),
    /**
     * Tone red eye.
     */
    TONE_RED("eyes/eyeToneRed.png")

    ;

    /**
     * The game image of the eye.
     */
    val image: GameImage get() = Resources3D.resources.image(this.path)
}