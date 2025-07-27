package fr.khelp.zegaime.sounds.midi

import fr.khelp.zegaime.sounds.SoundException
import fr.khelp.zegaime.sounds.SoundInterface
import fr.khelp.zegaime.sounds.SoundProgress
import fr.khelp.zegaime.sounds.SoundState
import fr.khelp.zegaime.utils.tasks.delay
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.parallel
import java.io.File
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequencer

/**
 * A sound that can be played from a MIDI file.
 *
 * This class is for internal use of the sound system.
 *
 * @param file The MIDI sound file.
 */
internal class SoundMidi(file : File) : SoundInterface
{
    /** Sequencer used for plays the sound */
    private val sequencer : Sequencer

    /** Sound total size in micro-seconds */
    override val totalSize : Long
        get() = this.sequencer.microsecondLength

    /** Current position in sound in micro-seconds */
    override var position : Long
        get() = this.sequencer.microsecondPosition
        set(value)
        {
            val position = value.coerceIn(0L, this.totalSize)
            this.sequencer.microsecondPosition = position
            this.soundProgressSource.value = SoundProgress(position, this.totalSize)
        }

    /** Sound current state source */
    private val soundStateSource = ObservableSource<SoundState>(SoundState.NOT_LAUNCHED)

    /** Sound current progress source */
    private val soundProgressSource : ObservableSource<SoundProgress>

    /** Sound current state */
    override val soundState : Observable<SoundState> = this.soundStateSource.observable

    /** Sound current progress */
    override val soundProgress : Observable<SoundProgress>

    /** Whether sound still playing */
    private var playing = false

    /** Synchronization lock */
    private val lock = Object()

    init
    {
        try
        {
            this.sequencer = MidiSystem.getSequencer()
            this.sequencer.sequence = MidiSystem.getSequence(file)
            this.sequencer.open()
            this.soundProgressSource = ObservableSource<SoundProgress>(SoundProgress(0L, this.totalSize))
            this.soundProgress = this.soundProgressSource.observable
        }
        catch (exception : Exception)
        {
            throw SoundException("Failed to create sequencer", exception)
        }
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

        synchronized(this.lock)
        {
            this.playing = false
        }

        this.soundStateSource.value = SoundState.DESTROYED
        this.sequencer.stop()
        this.sequencer.close()
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

        this.sequencer.start()

        synchronized(this.lock)
        {
            if (!this.playing)
            {
                this.playing = true
                this.soundStateSource.value = SoundState.PLAYING
                this::waitEndRunningTask.parallel()
            }
        }
    }

    /**
     * Stops the sound.
     *
     * The sound will start from the beginning if played again.
     */
    override fun stop()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        this.sequencer.stop()
        this.position = 0L
    }

    /**
     * Pauses the sound.
     *
     * The sound will start where it paused if started again.
     */
    override fun pause()
    {
        if (this.soundStateSource.value == SoundState.DESTROYED)
        {
            return
        }

        this.soundStateSource.value = SoundState.PAUSED
        this.sequencer.stop()
    }

    /**
     * Awaits the end of the sound playing.
     */
    private fun waitEndRunningTask()
    {
        this.soundProgressSource.value = SoundProgress(this.position, this.totalSize)

        if (this.sequencer.isRunning)
        {
            delay(128) { this.waitEndRunningTask() }
            return
        }

        synchronized(this.lock)
        {
            this.playing = false
        }

        if (this.soundStateSource.value == SoundState.PLAYING)
        {
            this.soundStateSource.value = SoundState.STOPPED
        }
    }
}