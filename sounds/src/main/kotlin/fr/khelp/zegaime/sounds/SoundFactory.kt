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
 * Sounds directory
 */
val DIRECTORY_SOUNDS : File by lazy {
    val directory = File(outsideDirectory, "media/sounds/")
    directory.createDirectory()
    directory
}

/**
 * Creates sound from file
 *
 * @param file Sound file
 *
 * @return Created sound
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
 * Create sound from distant URL
 *
 * @param url URL where get the sound
 *
 * @return Created sound
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
 * Create sound from a stream
 *
 * @param streamProducer Stream producer
 * @param fileName File name to give to the sound
 *
 * @return Created sound
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
