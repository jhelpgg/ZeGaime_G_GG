package fr.khelp.zegaime.sounds

/**
 * Represents the possible states of a sound.
 */
enum class SoundState
{
    /** Indicates that the sound has never been launched. */
    NOT_LAUNCHED,

    /** Indicates that the sound is stopped. */
    STOPPED,

    /** Indicates that the sound is playing. */
    PLAYING,

    /** Indicates that the sound is paused. */
    PAUSED,

    /** Indicates that the sound is in an error state. */
    ERROR,

    /** Indicates that the sound is destroyed and can't be used. */
    DESTROYED
}