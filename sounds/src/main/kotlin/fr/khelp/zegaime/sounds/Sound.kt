package fr.khelp.zegaime.sounds

import fr.khelp.zegaime.utils.tasks.observable.Observable

/**
 * Represents a sound
 *
 * @property soundInterface Sound interface instance to use
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
     * Play the sound
     */
    fun play()
    {
        this.soundInterface.play()
    }

    /**
     * Loop the sound a number of time
     *
     * @param loop Number of loops. By default, "infinite" loops
     */
    fun loop(loop : Int = Int.MAX_VALUE)
    {
        this.loop = loop
        this.play()
    }

    /**
     * Pause the sound
     */
    fun pause()
    {
        this.soundInterface.pause()
    }

    /**
     * Stop the sound and loops
     */
    fun stop()
    {
        this.loop = -1
        this.soundInterface.stop()
    }

    /**
     * Destroy the sound to free some memory
     *
     * The sound can't be used after that
     */
    fun destroy()
    {
        this.soundInterface.destroy()
        this.cancelObserveState()
    }

    /**
     * Called each time sound state changed
     *
     * @param soundState New sound state
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
