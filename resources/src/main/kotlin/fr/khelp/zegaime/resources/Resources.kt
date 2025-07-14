package fr.khelp.zegaime.resources

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.pcx.PCX
import fr.khelp.zegaime.resources.images.ImageDescription
import fr.khelp.zegaime.resources.images.ImageLoad
import fr.khelp.zegaime.resources.images.ImageLoadThumbnail
import fr.khelp.zegaime.sounds.Sound
import fr.khelp.zegaime.sounds.SoundState
import fr.khelp.zegaime.sounds.soundFromStream
import fr.khelp.zegaime.utils.collections.cache.Cache
import fr.khelp.zegaime.utils.extensions.allCharactersExcludeThis
import fr.khelp.zegaime.utils.extensions.regularExpression
import fr.khelp.zegaime.utils.source.ReferenceSource
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegame.video.Video
import fr.khelp.zegame.video.videoReader
import java.io.InputStream
import java.net.URL
import java.util.Locale

/**
 * Access to resources
 *
 * @property source Source of the ressource
 */
class Resources(private val source : ReferenceSource)
{
    companion object
    {
        /** Language source, to change the current language or listen language changes */
        val languageObservableData = ObservableSource<Locale>(Locale.getDefault())

        /** Ressource link to capture */
        @JvmStatic
        private val resourcePathGroup = '"'.allCharactersExcludeThis.oneOrMore().group()

        /** Resource link regular expression */
        @JvmStatic
        private val resourcesReferenceRegex = "\"resources:/".regularExpression + Resources.resourcePathGroup + '"'
    }

    /** Resource texts cache */
    private val resourcesTexts = HashMap<String, ResourcesText>()

    /** Images cache */
    private val imagesCache = Cache<ImageDescription, GameImage>(128) { imageDescription ->
        when (imageDescription)
        {
            is ImageLoad          ->
                if (imageDescription.path.endsWith(".pcx", ignoreCase = true))
                {
                    PCX(this.inputStream(imageDescription.path)).createImage()
                }
                else
                {
                    GameImage.load(this.inputStream(imageDescription.path))
                }

            is ImageLoadThumbnail ->
                if (imageDescription.path.endsWith(".pcx", ignoreCase = true))
                {
                    PCX(this.inputStream(imageDescription.path))
                        .createImage()
                        .resize(imageDescription.width, imageDescription.height)
                }
                else
                {
                    GameImage.loadThumbnail(this.inputStream(imageDescription.path),
                                            imageDescription.width,
                                            imageDescription.height)
                }
        }
    }

    /** Sounds cache */
    private val soundsCache : Cache<String, Sound> by lazy {
        Cache<String, Sound>(64) { path ->
            val sound = soundFromStream({ this.inputStream(path) }, path)
            sound.soundStateObservable.register { state ->
                if (state == SoundState.DESTROYED)
                {
                    this.soundsCache.remove(path)
                }
            }
            sound
        }
    }

    /** Videos cache */
    private val videosCache = Cache<String, Video>(32) { path -> videoReader(this.inputStream(path)) }

    /**
     * Obtain a resource text.
     *
     * @param path Base relative path, the system will recognise the language-specific versions automatically
     *
     * @return The resource text
     */
    fun resourcesText(path : String) : ResourcesText =
        synchronized(this.resourcesTexts) { this.resourcesTexts.getOrPut(path) { ResourcesText(path, this) } }

    /**
     * Get an image
     *
     * @param path Image relative path
     *
     * @return The image
     */
    fun image(path : String) : GameImage =
        this.imagesCache[ImageLoad(path)]

    /**
     * Get an image thumbnail
     *
     * @param path Image relative path
     * @param width Thumbnail width
     * @param height Thumbnail height
     *
     * @return The image thumbnail
     */
    fun imageThumbnail(path : String, width : Int, height : Int) : GameImage =
        this.imagesCache[ImageLoadThumbnail(path, width, height)]

    /**
     * Get a sound
     *
     * @param path Sound relative path
     *
     * @return The sound
     */
    fun sound(path : String) : Sound =
        this.soundsCache[path]

    /**
     * Get a video
     *
     * @param path Video relative path
     *
     * @return The video
     */
    fun video(path : String) : Video =
        this.videosCache[path]

    /**
     * Get a stream from a path
     *
     * @param path Stream on a relative path
     *
     * @return The stream
     */
    fun inputStream(path : String) : InputStream = this.source.inputStream(path)

    /**
     * Get a path url
     *
     * @param path Relative path to get the URL
     *
     * @return the url
     */
    fun url(path : String) : URL = this.source.url(path)

    /**
     * Indicates if a path exists
     *
     * @param path Relative path test
     *
     * @return Whether the relative path exists
     */
    fun exists(path : String) : Boolean = this.source.exists(path)

    /**
     * Replaces links to resources by the complete url
     *
     * @param string SString where replace
     *
     * @return String with replacements
     */
    fun replaceResourcesLinkIn(string : String) : String
    {
        val stringBuilder = StringBuilder()
        var start = 0
        val matcher = Resources.resourcesReferenceRegex.matcher(string)

        while (matcher.find())
        {
            stringBuilder.append(string.substring(start, matcher.start()))
            stringBuilder.append('"')
            stringBuilder.append(this.url(matcher.group(Resources.resourcePathGroup)))
            stringBuilder.append('"')
            start = matcher.end()
        }

        stringBuilder.append(string.substring(start))
        return stringBuilder.toString()
    }

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Resources)
        {
            return false
        }

        return this.source == other.source
    }

    override fun hashCode() : Int = this.source.hashCode()

    override fun toString() : String = "Resources : ${this.source}"
}