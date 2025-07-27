package fr.khelp.zegaime.sounds

/**
 * Represents the progress of a sound.
 *
 * @property progress The current progress of the sound.
 * @property total The total size of the sound.
 * @constructor Creates a new sound progress.
 */
data class SoundProgress(val progress : Long, val total : Long)