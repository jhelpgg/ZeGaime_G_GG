package fr.khelp.zegaime.sounds.mp3

import fr.khelp.zegaime.sounds.SoundException
import fr.khelp.zegaime.sounds.SoundInterface
import fr.khelp.zegaime.sounds.SoundProgress
import fr.khelp.zegaime.sounds.SoundState
import fr.khelp.zegaime.utils.logs.exception
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.parallel
import java.io.File
import javazoom.jl.player.Player

/**
 * A sound that can be read from an MP3 file.
 *
 * @param file The MP3 sound file.
 * 
 */
internal class SoundMP3(file : File) : SoundInterface
{
    /** Control stream to read the file */
    private val controlInputStream = ControlInputStream(file)

    /** Sound state source */
    private val soundStateSource = ObservableSource<SoundState>(SoundState.NOT_LAUNCHED)

    /** Whether sound is playing */
    private var playing = false

    /** Synchronization lock */
    private val lock = Object()

    /** Player for play the MP3 */
    private var player : Player? = null

    /** Position for resume */
    private var resumePosition = 0L

    /** Sound size in bytes */
    override val totalSize : Long = this.controlInputStream.size

    /** Current sound position in bytes */
    override var position : Long
        get() = this.controlInputStream.position
        set(value)
        {
            this.controlInputStream.position = value
        }

    /** Current sound state */
    override val soundState : Observable<SoundState> = this.soundStateSource.observable

    /** Current playing progress */
    override val soundProgress : Observable<SoundProgress> = this.controlInputStream.progressObservable

    /**
     * Destroys the sound to free some memory.
     *
     * The sound can't be used after that.
     */
    override fun destroy()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        this.controlInputStream.close()
        this.player?.close()
        this.player = null

        synchronized(this.lock)
        {
            this.playing = false
        }

        this.soundStateSource.value = SoundState.DESTROYED
    }

    /**
     * Plays the sound.
     */
    override fun play()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        synchronized(this.lock)
        {
            if (this.playing)
            {
                this.controlInputStream.pause = false
                this.soundStateSource.value = SoundState.PLAYING
            }
            else
            {
                this.playing = true

                try
                {
                    this.controlInputStream.resetAtZero()
                    this.position = this.resumePosition
                    this.player = Player(this.controlInputStream)
                }
                catch (exception : Exception)
                {
                    throw SoundException("Playing start failed", exception)
                }

                this.soundStateSource.value = SoundState.PLAYING
                this::taskPlayTheSound.parallel()
            }
        }
    }

    /**
     * Stops the sound.
     */
    override fun stop()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        this.resumePosition = 0L
        this.playEnd()
    }

    /**
     * Pauses the sound.
     */
    override fun pause()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        this.resumePosition = this.position
        this.controlInputStream.pause = true
        this.soundStateSource.value = SoundState.PAUSED
        this.playEnd()
    }

    /**
     * Plays the sound.
     */
    private fun taskPlayTheSound()
    {
        try
        {
            this.player?.play()
        }
        catch (exception : Exception)
        {
            this.soundStateSource.value = SoundState.ERROR
            exception(exception)
            this.controlInputStream.pause = true
            this.destroy()
            return
        }

        this.playEnd()
    }

    /**
     * Finished to play the sound
     */
    private fun playEnd()
    {
        synchronized(this.lock)
        {
            this.playing = false
        }

        this.player?.close()
        this.player = null

        if (!this.controlInputStream.pause)
        {
            this.resumePosition = 0L
            this.soundStateSource.value = SoundState.STOPPED
        }
    }
}
