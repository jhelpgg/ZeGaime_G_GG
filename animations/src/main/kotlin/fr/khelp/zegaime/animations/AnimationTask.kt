package fr.khelp.zegaime.animations

/**
 * Indicates that the annotated method will be called in the animation thread loop.
 *
 * This annotation is a marker to inform developers that the annotated method is sensible.
 *
 * The animation thread loop is a single thread that manages all animations.
 * If a method called in this thread takes too much time to execute, it will slow down all animations.
 *
 * So, it is important to not put heavy things in a method annotated with `@AnimationTask`.
 *
 * For example, the [Animation.animate] method is annotated with `@AnimationTask`.
 *
 * ```kotlin
 * interface Animation
 * {
 *     @AnimationTask
 *     fun animate(millisecondsSinceStarted: Long): Boolean
 * }
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class AnimationTask