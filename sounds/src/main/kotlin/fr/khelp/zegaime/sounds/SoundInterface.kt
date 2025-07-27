package fr.khelp.zegaime.sounds

import fr.khelp.zegaime.utils.tasks.observable.Observable

/**
 * Represents a sound.
 *
 * The sound position represents a part of the sound, not a duration.
 * Each part has the same duration, so to reach the middle of the sound, use the middle position.
 *
 * This interface is for internal use of the sound system.
 */
internal interface SoundInterface
{
    /** The total size of the sound. */
    val totalSize : Long

    /** The current position of the sound. */
    var position : Long

    /** The current state of the sound. */
    val soundState : Observable<SoundState>

    /** The current progress of the sound. */
    val soundProgress : Observable<SoundProgress>

    /**
     * Properly destroys the sound.
     *
     * This frees the memory and thread associated with playing the sound.
     * The sound cannot be used after this call.
     */
    fun destroy()

    /**
     * Plays the sound.
     *
     * This launches the playback and returns immediately.
     */
    fun play()

    /**
     * Stops the sound.
     */
    fun stop()

    /**
     * Pauses the sound.
     */
    fun pause()
}