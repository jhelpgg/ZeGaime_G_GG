package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.group.AnimationParallel

/**
 * Creates an [AnimationParallel] in DSL way
 *
 * ```kotlin
 * val animation = animationParallel {
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
 * @param create [AnimationParallel] creation
 *
 * @return [AnimationParallel] created
 */
@AnimationDSL
fun animationParallel(create : AnimationParallelCreator.() -> Unit) : AnimationParallel
{
    val animationsSequentialCreator = AnimationParallelCreator()
    animationsSequentialCreator.create()
    return animationsSequentialCreator.animationParallel
}

/**
 * [AnimationParallel] creator
 */
@AnimationDSL
class AnimationParallelCreator
{
    /** Created [AnimationParallel]  */
    internal val animationParallel = AnimationParallel()

    /**
     * Add animation to plat in parallel
     */
    operator fun Animation.unaryPlus()
    {
        this@AnimationParallelCreator.animationParallel += this
    }
}