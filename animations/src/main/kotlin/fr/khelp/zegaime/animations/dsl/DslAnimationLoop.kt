package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.Animation
import fr.khelp.zegaime.animations.AnimationDoesNothing
import fr.khelp.zegaime.animations.group.AnimationLoop
import kotlin.math.max

/**
 * Creates an [AnimationLoop] in DSL way
 *
 * ```kotlin
 * val animation = animationLoop {
 *      animationHeader = animationParallel {
 *          +animationAction{
 *             action = { println("Animation started !" } }
 *          }
 *          // ...
 *      }
 *
 *      animationLooped = animationSequential {
 *          // ....
 *      }
 *
 *      animationFooter = animationAction {
 *             action = { println("Animation finished !" } }*
 *      }
 *
 *      numberMaximumLoop = 5
 * }
 * ```
 *
 * It is recommended to specify at least `animationLooped`
 *
 * @param create Animation creator
 *
 * @return Created [AnimationLoop]
 */
@AnimationDSL
fun animationLoop(create : AnimationLoopCreator.() -> Unit) : AnimationLoop
{
    val animationLoopCreator = AnimationLoopCreator()
    animationLoopCreator.create()
    return animationLoopCreator()
}

/**
 * [AnimationLoop] creator
 */
@AnimationDSL
class AnimationLoopCreator
{
    /** Animation loop header. Played before loping. Does nothing by default */
    var animationHeader : Animation = AnimationDoesNothing

    /** Animation looped. Played at most [numberMaximumLoop] time */
    var animationLooped : Animation = AnimationDoesNothing

    /** Animation loop footer. Played after loping. Does nothing by default */
    var animationFooter : Animation = AnimationDoesNothing

    /** Number maximum of repeat [animationLooped]. "Infinite" by default */
    var numberMaximumLoop : Int = Int.MAX_VALUE
        set(value)
        {
            field = max(1, value)
        }

    /**
     * Creates the [AnimationLoop]
     *
     * @return created [AnimationLoop]
     */
    internal operator fun invoke() : AnimationLoop =
        AnimationLoop(this.animationLooped, this.animationHeader, this.animationFooter, this.numberMaximumLoop)
}