package fr.khelp.zegame.video

/**
 * Represents the progress of a video.
 *
 * @property timeMilliseconds The current time in milliseconds.
 * @property totalTimeMilliseconds The total time in milliseconds.
 * @constructor Creates a new video progress.
 */
data class VideoProgress(val timeMilliseconds : Long, val totalTimeMilliseconds : Long)