package fr.khelp.zegaime.engine3d.resources

import fr.khelp.zegaime.images.GameImage

/**
 * Represents a mouth texture.
 *
 * @property path The path to the mouth texture.
 * @constructor Creates a new mouth.
 */
enum class Mouth(private val path : String)
{
    /**
     * Annoyed mouth 1.
     */
    ANNOY_1("mouth/annoy1.png"),

    /**
     * Annoyed mouth 2.
     */
    ANNOY_2("mouth/annoy2.png"),

    /**
     * Annoyed mouth 3.
     */
    ANNOY_3("mouth/annoy3.png"),

    /**
     * Big annoyed mouth 1.
     */
    ANNOY_BIG_1("mouth/annoyBig1.png"),

    /**
     * Big annoyed mouth 2.
     */
    ANNOY_BIG_2("mouth/annoyBig2.png"),

    /**
     * Big annoyed mouth 3.
     */
    ANNOY_BIG_3("mouth/annoyBig3.png"),

    /**
     * Semi-annoyed mouth 1.
     */
    ANNOY_SEMI_1("mouth/annoySemi1.png"),

    /**
     * Semi-annoyed mouth 2.
     */
    ANNOY_SEMI_2("mouth/annoySemi2.png"),

    /**
     * Semi-annoyed mouth 3.
     */
    ANNOY_SEMI_3("mouth/annoySemi3.png"),

    /**
     * Sad mouth 1.
     */
    SAD_1("mouth/sad1.png"),

    /**
     * Sad mouth 2.
     */
    SAD_2("mouth/sad2.png"),

    /**
     * Sad mouth 3.
     */
    SAD_3("mouth/sad3.png"),

    /**
     * Serious mouth 1.
     */
    SERIOUS_1("mouth/serious1.png"),

    /**
     * Serious mouth 2.
     */
    SERIOUS_2("mouth/serious2.png"),

    /**
     * Serious mouth 3.
     */
    SERIOUS_3("mouth/serious3.png"),

    /**
     * Smile mouth 1.
     */
    SMILE_1("mouth/smile1.png"),

    /**
     * Smile mouth 2.
     */
    SMILE_2("mouth/smile2.png"),

    /**
     * Smile mouth 3.
     */
    SMILE_3("mouth/smile3.png"),

    /**
     * Big smile mouth 1.
     */
    SMILE_BIG_1("mouth/smileBig1.png"),

    /**
     * Big smile mouth 2.
     */
    SMILE_BIG_2("mouth/smileBig2.png"),

    /**
     * Big smile mouth 3.
     */
    SMILE_BIG_3("mouth/smileBig3.png"),

    /**
     * Other smile mouth 1.
     */
    SMILE_OTHER_1("mouth/smileOther1.png"),

    /**
     * Other smile mouth 2.
     */
    SMILE_OTHER_2("mouth/smileOther2.png"),

    /**
     * Other smile mouth 3.
     */
    SMILE_OTHER_3("mouth/smileOther3.png"),

    /**
     * Whisper mouth 1.
     */
    WHISPER_1("mouth/whisper1.png"),

    /**
     * Whisper mouth 2.
     */
    WHISPER_2("mouth/whisper2.png"),

    /**
     * Whisper mouth 3.
     */
    WHISPER_3("mouth/whisper3.png")

    ;

    /**
     * The game image of the mouth.
     */
    val image : GameImage get() = Resources3D.resources.image(this.path)
}