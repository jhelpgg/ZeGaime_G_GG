package fr.khelp.zegaime.sounds.other

import fr.khelp.zegaime.sounds.SoundInterface
import fr.khelp.zegaime.sounds.SoundProgress
import fr.khelp.zegaime.sounds.SoundState
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.sleep
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.Clip

/**
 * A generic sound supported natively by the JVM.
 *
 * This class is for internal use of the sound system.
 *
 * @param file The sound file.
 */
internal class SoundOther(file : File) : SoundInterface
{
    /** Total sound length in micro-seconds */
    override val totalSize : Long

    /** Current sound position in micro-seconds */
    override var position : Long
        get() = this.clip.microsecondPosition
        set(value)
        {
            this.clip.microsecondPosition = value
            this.soundProgressSource.value = SoundProgress(value, this.totalSize)
        }

    /** Sound state source */
    private val soundStateSource = ObservableSource<SoundState>(SoundState.NOT_LAUNCHED)

    /** Sound progress source */
    private val soundProgressSource : ObservableSource<SoundProgress>

    /** Current sound state */
    override val soundState : Observable<SoundState> = this.soundStateSource.observable

    /** Current sound progress */
    override val soundProgress : Observable<SoundProgress>

    /** Sound stream */
    private val audioInputStream : AudioInputStream

    /** Sound clip */
    private val clip : Clip

    /** Whether sound playing */
    private var playing = false

    /** Whether sound stopped with [stop] method */
    private var stopWithStop = true

    init
    {
        val (audioInputStream, clip) = createSound(file)
        this.audioInputStream = audioInputStream
        this.clip = clip
        this.totalSize = this.clip.microsecondLength
        this.soundProgressSource = ObservableSource<SoundProgress>(SoundProgress(0L, this.totalSize))
        this.soundProgress = this.soundProgressSource.observable
    }

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

        this.soundStateSource.value = SoundState.DESTROYED
        this.playing = false
        this.clip.stop()
        this.clip.close()
        this.audioInputStream.close()
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

        if (!this.playing)
        {
            this.playing = true
            this.stopWithStop = true
            this.soundStateSource.value = SoundState.PLAYING
            this::playTask.parallel()
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

        this.stopWithStop = true
        this.playing = false
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

        this.stopWithStop = false
        this.playing = false
    }

    /**
     * Sound playing task
     */
    private suspend fun playTask()
    {
        sleep(8)
        this.clip.start()
        sleep(8)

        while (this.playing && this.clip.isRunning)
        {
            this.soundProgressSource.value = SoundProgress(this.position, this.totalSize)
            sleep(128)
        }

        this.clip.stop()

        if (this.playing || this.stopWithStop)
        {
            this.clip.microsecondPosition = 0
        }

        this.playing = false

        if (this.soundStateSource.value != SoundState.DESTROYED)
        {
            if (this.stopWithStop)
            {
                this.soundStateSource.value = SoundState.STOPPED
            }
            else
            {
                this.soundStateSource.value = SoundState.PAUSED
            }
        }
    }
}