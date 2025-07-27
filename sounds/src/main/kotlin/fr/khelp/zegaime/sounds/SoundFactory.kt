package fr.khelp.zegaime.sounds

import fr.khelp.zegaime.sounds.midi.SoundMidi
import fr.khelp.zegaime.sounds.mp3.SoundMP3
import fr.khelp.zegaime.sounds.other.SoundOther
import fr.khelp.zegaime.utils.io.createDirectory
import fr.khelp.zegaime.utils.io.outsideDirectory
import fr.khelp.zegaime.utils.io.write
import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * The directory where sounds are stored.
 */
val DIRECTORY_SOUNDS : File by lazy {
    val directory = File(outsideDirectory, "media/sounds/")
    directory.createDirectory()
    directory
}

/**
 * Creates a sound from a file.
 *
 * **Usage example**
 * ```kotlin
 * val sound = soundFromFile(myFile)
 * ```
 *
 * @param file The sound file.
 * @return The created sound.
 */
fun soundFromFile(file : File) : Sound
{
    val sound =
        when (file.extension.lowercase())
        {
            "mp3"  -> SoundMP3(file)
            "mid"  -> SoundMidi(file)
            "midi" -> SoundMidi(file)
            else   -> SoundOther(file)
        }

    return Sound(sound)
}

/**
 * Creates a sound from a distant URL.
 *
 * **Usage example**
 * ```kotlin
 * val sound = soundFromURL(myUrl)
 * ```
 *
 * @param url The URL where to get the sound.
 * @return The created sound.
 */
fun soundFromURL(url : URL) : Sound
{
    val name = url.toString()
    val fileName = name.replace("://", "/")
        .replace(":/", "/")
        .replace(":", "/")
        .replace("?", "/")
        .replace("&", "/")
        .replace("=", "/")
    return soundFromStream({ url.openStream() }, fileName)
}

/**
 * Creates a sound from a stream.
 *
 * **Usage example**
 * ```kotlin
 * val sound = soundFromStream({ myInputStream }, "mySound.wav")
 * ```
 *
 * @param streamProducer The stream producer.
 * @param fileName The file name to give to the sound.
 * @return The created sound.
 */
fun soundFromStream(streamProducer : () -> InputStream, fileName : String) : Sound
{
    val destination = File(DIRECTORY_SOUNDS, fileName)

    if (!destination.exists())
    {
        val stream = streamProducer()
        write(stream, destination)
        stream.close()
    }

    return soundFromFile(destination)
}