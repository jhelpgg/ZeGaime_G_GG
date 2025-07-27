package fr.khelp.zegaime.animations.dsl

/**
 * DSL marker for animations
 *
 * It helps to create animation in more easy way
 *
 * For example, to create a sequential animation:
 *
 * ```kotlin
 * val animation = animationsSequential {
 *      +animationLoop {
 *          animationHeader =  ...
 *
 *          animationLooped =   ...
 *      }
 *      +animationParallel {
 *          +animationInt(42) {
 *               ...
 *          }
 *          +animationAction {
 *              action = ...
 *          }
 *      }
 * }
 * ```
 */
@DslMarker
annotation class AnimationDSL

/**
 * DSL marker for key time animation
 *
 * It helps to create key time animation in more easy way
 *
 * For example, to create a key time animation:
 *
 * ```kotlin
 * val animation = animationInt(42) {
 *      73.at(128L)
 *      80.at(1024L, InterpolationSine)
 * }
 * ```
 */
@DslMarker
annotation class KeyTimeDSL
