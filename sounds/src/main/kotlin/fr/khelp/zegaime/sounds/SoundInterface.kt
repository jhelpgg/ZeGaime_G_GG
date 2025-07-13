package fr.khelp.zegaime.sounds

import fr.khelp.zegaime.utils.tasks.observable.Observable

/**
 * A sound.
 *
 * Sound position represents a part of the sound, not a duration.
 *
 * Each part have same duration, so if you want to reach the middle of sound, use middle position, ...
 */
internal interface SoundInterface
{
    /** Sound total size */
    val totalSize : Long

    /** Sound position */
    var position : Long

    /** Sound current state */
    val soundState : Observable<SoundState>

    /** Sound current progress */
    val soundProgress : Observable<SoundProgress>

    /**
     * Properly destroy the sound.
     *
     * Free memory and thread associated with the sound playing.
     *
     * Can't use the sound after this call
     */
    fun destroy()

    /**
     * Play the sound.
     *
     * Launch the playing and return immediately.
     */
    fun play()

    /**
     * Stop the sound
     */
    fun stop()

    /**
     * Pause the sound
     */
    fun pause()
}