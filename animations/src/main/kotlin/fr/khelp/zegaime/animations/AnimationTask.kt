package fr.khelp.zegaime.animations

/**
 * Indicates the method will be called in animation thread loop.
 *
 * Don't put heavy things in it, else it will slow down all animations
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class AnimationTask()
