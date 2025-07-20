package fr.khelp.zegaime.animations

/**
 * Animation element
 *
 * @property animation Animation to play
 * @property startTimeInMilliseconds Time when animation was started
 */
internal data class AnimationElement(val animation : Animation,
                                     val startTimeInMilliseconds : Long = System.currentTimeMillis())
