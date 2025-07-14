package fr.khelp.zegame.video

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.tasks.sleep

class Video internal constructor(val width : Int, val height : Int, val fps : Int, private val images : List<GameImage>)
{
    val totalMilliseconds : Long = (this.images.size * 1_000L) / this.fps
    val image : GameImage = GameImage(this.width, this.height)

    private val progressSource = ObservableSource<VideoProgress>(VideoProgress(0L, this.totalMilliseconds))
    val progress : Observable<VideoProgress> = this.progressSource.observable

    private val playingSource = ObservableSource<Boolean>(false)
    val playing : Observable<Boolean> = this.playingSource.observable

    private var imageIndex = 0
    private val lock = Object()

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