package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.group.AnimationsSequential

/**
 * Creates an [AnimationsSequential] in DSL way
 *
 * ```kotlin
 * val animation = animationsSequential {
 *      + animationAction {
 *          // ...
 *      }
 *
 *      + animationDouble(42.73) {
 *          // ...
 *      }
 *
 *      // ....
 * }
 * ```
 *
 * @param create [AnimationsSequential] creation
 *
 * @return [AnimationsSequential] created
 */
@AnimationDSL
fun animationsSequential(create : AnimationsSequentialCreator.() -> Unit) : AnimationsSequential
{
    val animationsSequentialCreator = AnimationsSequentialCreator()
    animationsSequentialCreator.create()
    return animationsSequentialCreator.animationsSequential
}

/**
 * [AnimationsSequential] creator
 */
@AnimationDSL
class AnimationsSequentialCreator
{
    /** Created [AnimationsSequential] */
    internal val animationsSequential = AnimationsSequential()

    /**
     * Add animation to play in sequence
     */
    operator fun Animation.unaryPlus()
    {
        this@AnimationsSequentialCreator.animationsSequential += this
    }
}