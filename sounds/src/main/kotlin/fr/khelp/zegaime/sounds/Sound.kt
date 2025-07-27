package fr.khelp.zegaime.sounds

import fr.khelp.zegaime.utils.tasks.observable.Observable

/**
 * Represents a sound.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `soundFromStream` method.
 *
 * **Standard usage:**
 * ```kotlin
 * val sound = soundFromStream({ myInputStream }, "mySound.wav")
 * sound.play()
 * ```
 *
 * @property soundStateObservable An observable that emits the current state of the sound.
 * @property soundProgressObservable An observable that emits the current progress of the sound.
 * @property totalSize The total size of the sound in bytes.
 * @property position The current position of the sound in bytes.
 * @property destroyOnEnd Indicates if the sound should be destroyed when it reaches the end.
 * @constructor Creates a new sound. For internal use only.
 */
class Sound internal constructor(private val soundInterface : SoundInterface)
{
    /** Current sound state */
    val soundStateObservable : Observable<SoundState> = this.soundInterface.soundState

    /** Current sound progress */
    val soundProgressObservable : Observable<SoundProgress> = this.soundInterface.soundProgress

    /** Sound total size */
    val totalSize : Long = this.soundInterface.totalSize

    /** Sound current position */
    var position : Long
        get() = this.soundInterface.position
        set(value)
        {
            this.soundInterface.position = value
        }

    /** Way to cancel observing state changed */
    private val cancelObserveState = this.soundStateObservable.register(this::soundSateChanged)

    /** Whether if destroy the sound when the end is reach */
    var destroyOnEnd = false

    /** Current loop left */
    private var loop = -1

    /** Whether sound was playing */
    private var wasPlaying = false

    /**
     * Plays the sound.
     */
    fun play()
    {
        this.soundInterface.play()
    }

    /**
     * Loops the sound a number of times.
     *
     * @param loop The number of loops. By default, it loops "infinitely".
     */
    fun loop(loop : Int = Int.MAX_VALUE)
    {
        this.loop = loop
        this.play()
    }

    /**
     * Pauses the sound.
     */
    fun pause()
    {
        this.soundInterface.pause()
    }

    /**
     * Stops the sound and any loops.
     */
    fun stop()
    {
        this.loop = -1
        this.soundInterface.stop()
    }

    /**
     * Destroys the sound to free some memory.
     *
     * The sound can't be used after that.
     */
    fun destroy()
    {
        this.soundInterface.destroy()
        this.cancelObserveState()
    }

    /**
     * Called each time the sound state changes.
     *
     * @param soundState The new sound state.
     */
    private fun soundSateChanged(soundState : SoundState)
    {
        when (soundState)
        {
            SoundState.PAUSED, SoundState.DESTROYED, SoundState.ERROR, SoundState.NOT_LAUNCHED -> return
            SoundState.PLAYING                                                                 ->
            {
                this.wasPlaying = true
                return
            }

            SoundState.STOPPED                                                                 ->
                if (!this.wasPlaying)
                {
                    return
                }
                else
                {
                    this.wasPlaying = false
                }
        }

        if (this.loop > 0)
        {
            this.loop--
            this.play()
            return
        }

        if (this.destroyOnEnd)
        {
            this.destroy()
        }
    }
}