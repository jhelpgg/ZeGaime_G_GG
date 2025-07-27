package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.sleep

/**
 * Represents a video.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * Use the `videoReader` function.
 *
 * **Standard usage:**
 * ```kotlin
 * val video = videoReader(inputStream)
 * video.play()
 * ```
 *
 * @property width The width of the video.
 * @property height The height of the video.
 * @property fps The frames per second of the video.
 * @property totalMilliseconds The total duration of the video in milliseconds.
 * @property image The current image of the video.
 * @property progress An observable that emits the current progress of the video.
 * @property playing An observable that emits `true` if the video is playing, `false` otherwise.
 * @property positionMilliseconds The current position of the video in milliseconds.
 * @constructor Creates a new video. For internal use only.
 */
class Video internal constructor(val width : Int, val height : Int, val fps : Int, private val images : List<GameImage>)
{
    /**
     * The total duration of the video in milliseconds.
     */
    val totalMilliseconds : Long = (this.images.size * 1_000L) / this.fps

    /**
     * The current image of the video.
     */
    val image : GameImage = GameImage(this.width, this.height)

    private val progressSource = ObservableSource<VideoProgress>(VideoProgress(0L, this.totalMilliseconds))

    /**
     * An observable that emits the current progress of the video.
     */
    val progress : Observable<VideoProgress> = this.progressSource.observable

    private val playingSource = ObservableSource<Boolean>(false)

    /**
     * An observable that emits `true` if the video is playing, `false` otherwise.
     */
    val playing : Observable<Boolean> = this.playingSource.observable

    private var imageIndex = 0
    private val lock = Object()

    /**
     * The current position of the video in milliseconds.
     */
    var positionMilliseconds : Long
        get() = (this.imageIndex * 1_000L) / this.fps
        set(value)
        {
            synchronized(this.lock)
            {
                this.imageIndex = ((value * this.fps) / 1_000L).toInt().coerceIn(0 until this.images.size)
                this.update()
            }
        }

    private lateinit var futurePlaying : Future<Unit>

    init
    {
        synchronized(this.lock) { this.update() }
    }

    /**
     * Plays the video.
     */
    fun play()
    {
        synchronized(this.lock)
        {
            if (this.playingSource.setValueIf(true) { value -> !value })
            {
                this.futurePlaying = this::playTask.parallel()
            }
        }
    }

    /**
     * Pauses the video.
     */
    fun pause()
    {
        synchronized(this.lock)
        {
            if (this.playingSource.setValueIf(false) { value -> value })
            {
                this.futurePlaying.cancel("pause")
            }
        }
    }

    /**
     * Stops the video.
     */
    fun stop()
    {
        synchronized(this.lock)
        {
            if (this.playingSource.setValueIf(false) { value -> value })
            {
                this.futurePlaying.cancel("stop")
                this.imageIndex = 0
                this.update()
            }
        }
    }

    /**
     * Must be called in synchronized lock block
     */
    private fun update()
    {
        this.image.putPixels(0, 0, this.width, this.height, this.images[this.imageIndex].grabPixels())
        this.progressSource.value = VideoProgress(this.positionMilliseconds, this.totalMilliseconds)
    }

    private suspend fun playTask()
    {
        val delayMilliseconds = 1_000L / this.fps

        while (synchronized(this.lock) { this.playingSource.value })
        {
            synchronized(this.lock) { this.update() }
            sleep(delayMilliseconds)

            synchronized(this.lock)
            {
                this.imageIndex++

                if (this.imageIndex >= this.images.size)
                {
                    this.imageIndex = 0
                    this.playingSource.value = false
                }
            }
        }
    }
}