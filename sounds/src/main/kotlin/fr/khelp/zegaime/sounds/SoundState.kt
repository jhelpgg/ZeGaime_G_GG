package fr.khelp.zegaime.sounds

/**
 * Sound possible state
 */
enum class SoundState
{
    /** Indicates that sound never launched */
    NOT_LAUNCHED,

    /** Indicates sound is stopped */
    STOPPED,

    /** Indicates sound is playing */
    PLAYING,

    /** Indicates sound is in pause */
    PAUSED,

    /** Indicates sound is on error */
    ERROR,

    /** Indicates sound is destroyed and can't be used */
    DESTROYED
}
