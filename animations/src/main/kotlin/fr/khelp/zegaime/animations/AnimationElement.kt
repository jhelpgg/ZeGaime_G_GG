package fr.khelp.zegaime.animations

/**
 * Represents an animation to play in the [AnimationManager].
 *
 * It associates an [Animation] with its start time.
 *
 * This class is for internal use of the animation system.
 *
 * @property animation Animation to play.
 * @property startTimeInMilliseconds Time when animation was started, in milliseconds.
 * @constructor Create the animation element.
 */
internal data class AnimationElement(val animation : Animation,
                                     val startTimeInMilliseconds : Long = System.currentTimeMillis())