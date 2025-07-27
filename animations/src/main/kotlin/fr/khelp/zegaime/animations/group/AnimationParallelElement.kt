package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation

/**
 * Represents an element of an [AnimationParallel].
 *
 * It associates an [Animation] with its playing state.
 *
 * This class is for internal use of the animation system.
 *
 * @property animation The animation to play.
 * @property playing Indicates if the animation is currently playing.
 * @constructor Create a new parallel animation element.
 */
internal data class AnimationParallelElement(val animation : Animation, var playing : Boolean)