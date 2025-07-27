package fr.khelp.zegaime.animations.dsl

/**
 * DSL marker for animations
 *
 * It helps to create animation in more easy way
 *
 * For example, to create a sequential animation:
 *
 * ```kotlin
 * val animation = sequential {
 *      loop {
 *          basic(1f, 2f, 1000) {
 *              //...
 *          }
 *          action {
 *              //...
 *          }
 *      }
 *      parallel {
 *          basic(4, 8, 500) {
 *              //...
 *          }
 *          keyFrame<Int> {
 *              frame(0, 1)
 *              frame(100, 5)
 *              frame(250, 2)
 *              frame(500, 8)
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
 * val animation = keyFrame<Float> {
 *      frame(0, 1f)
 *      frame(100, 5f)
 *      frame(250, 2f)
 *      frame(500, 8f)
 * }
 * ```
 */
@DslMarker
annotation class KeyTimeDSL
