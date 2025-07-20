package fr.khelp.zegaime.animations.group

import fr.khelp.zegaime.animations.Animation

/**
 * Animation parallel element
 *
 * @property animation Animation to play
 * @property playing Whether if animation playing
 */
internal data class AnimationParallelElement(val animation:Animation, var playing:Boolean)
