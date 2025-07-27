package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.basic.AnimationDouble
import fr.khelp.zegaime.animations.basic.AnimationFloat
import fr.khelp.zegaime.animations.basic.AnimationInt
import fr.khelp.zegaime.animations.basic.AnimationLong
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

/**
 * Creates an [AnimationDouble] in DSL way
 *
 * ```kotlin
 * val animation = animationDouble(0.0) {
 *      1.0.at(0L)
 *      5.0.at(100L, InterpolationSine)
 *      2.0.at(200L)
 *      8.0.at(500L, InterpolationHesitate)
 * }
 * ```
 *
 * @param initialValue Initial value
 * @param create Animation creation
 *
 * @return Created animation
 */
@AnimationDSL
fun animationDouble(initialValue : Double, create : AnimationDoubleCreator.() -> Unit) : AnimationDouble
{
    val animationDoubleCreator = AnimationDoubleCreator(initialValue)
    animationDoubleCreator.create()
    return animationDoubleCreator.animation
}

/**
 * Creates an [AnimationFloat] in DSL way
 *
 * ```kotlin
 * val animation = animationFloat(0f) {
 *      1f.at(0L)
 *      5f.at(100L, InterpolationSine)
 *      2f.at(200L)
 *      8f.at(500L, InterpolationHesitate)
 * }
 * ```
 *
 * @param initialValue Initial value
 * @param create Animation creation
 *
 * @return Created animation
 */
@AnimationDSL
fun animationFloat(initialValue : Float, create : AnimationFloatCreator.() -> Unit) : AnimationFloat
{
    val animationFloatCreator = AnimationFloatCreator(initialValue)
    animationFloatCreator.create()
    return animationFloatCreator.animation
}

/**
 * Creates an [AnimationLong] in DSL way
 *
 * ```kotlin
 * val animation = animationLong(0L) {
 *      1L.at(0L)
 *      5L.at(100L, InterpolationSine)
 *      2L.at(200L)
 *      8L.at(500L, InterpolationHesitate)
 * }
 * ```
 *
 * @param initialValue Initial value
 * @param create Animation creation
 *
 * @return Created animation
 */
@AnimationDSL
fun animationLong(initialValue : Long, create : AnimationLongCreator.() -> Unit) : AnimationLong
{
    val animationLongCreator = AnimationLongCreator(initialValue)
    animationLongCreator.create()
    return animationLongCreator.animation
}

/**
 * Creates an [AnimationInt] in DSL way
 *
 * ```kotlin
 * val animation = animationInt(0) {
 *      1.at(0L)
 *      5.at(100L, InterpolationSine)
 *      2.at(200L)
 *      8.at(500L, InterpolationHesitate)
 * }
 * ```
 *
 * @param initialValue Initial value
 * @param create Animation creation
 *
 * @return Created animation
 */
@AnimationDSL
fun animationInt(initialValue : Int, create : AnimationIntCreator.() -> Unit) : AnimationInt
{
    val animationIntCreator = AnimationIntCreator(initialValue)
    animationIntCreator.create()
    return animationIntCreator.animation
}

/**
 * [AnimationDouble] creator
 *
 * @param initialValue Initial value
 */
@AnimationDSL
class AnimationDoubleCreator(initialValue : Double)
    : DslAnimationKeyFrame<ObservableSource<Double>, Double>(AnimationDouble(initialValue))
{
    /** Created [AnimationDouble] */
    val animation : AnimationDouble = this.animationKeyTime as AnimationDouble
}

/**
 * [AnimationFloat] creator
 *
 * @param initialValue Initial value
 */
@AnimationDSL
class AnimationFloatCreator(initialValue : Float)
    : DslAnimationKeyFrame<ObservableSource<Float>, Float>(AnimationFloat(initialValue))
{
    /** Created [AnimationFloat] */
    val animation : AnimationFloat = this.animationKeyTime as AnimationFloat
}

/**
 * [AnimationLong] creator
 *
 * @param initialValue Initial value
 */
@AnimationDSL
class AnimationLongCreator(initialValue : Long)
    : DslAnimationKeyFrame<ObservableSource<Long>, Long>(AnimationLong(initialValue))
{
    /** Created [AnimationLong] */
    val animation : AnimationLong = this.animationKeyTime as AnimationLong
}

/**
 * [AnimationInt] creator
 *
 * @param initialValue Initial value
 */
@AnimationDSL
class AnimationIntCreator(initialValue : Int)
    : DslAnimationKeyFrame<ObservableSource<Int>, Int>(AnimationInt(initialValue))
{
    /** Created [AnimationInt] */
    val animation : AnimationInt = this.animationKeyTime as AnimationInt
}