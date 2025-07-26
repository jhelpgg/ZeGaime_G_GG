package fr.khelp.zegaime.animations.dsl

import fr.khelp.zegaime.animations.basic.AnimationDouble
import fr.khelp.zegaime.animations.basic.AnimationFloat
import fr.khelp.zegaime.animations.basic.AnimationInt
import fr.khelp.zegaime.animations.basic.AnimationLong
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

@AnimationDSL
fun animationDouble(initialValue : Double, create : AnimationDoubleCreator.() -> Unit) : AnimationDouble
{
    val animationDoubleCreator = AnimationDoubleCreator(initialValue)
    animationDoubleCreator.create()
    return animationDoubleCreator.animation
}

@AnimationDSL
fun animationFloat(initialValue : Float, create : AnimationFloatCreator.() -> Unit) : AnimationFloat
{
    val animationFloatCreator = AnimationFloatCreator(initialValue)
    animationFloatCreator.create()
    return animationFloatCreator.animation
}

@AnimationDSL
fun animationLong(initialValue : Long, create : AnimationLongCreator.() -> Unit) : AnimationLong
{
    val animationLongCreator = AnimationLongCreator(initialValue)
    animationLongCreator.create()
    return animationLongCreator.animation
}

@AnimationDSL
fun animationInt(initialValue : Int, create : AnimationIntCreator.() -> Unit) : AnimationInt
{
    val animationIntCreator = AnimationIntCreator(initialValue)
    animationIntCreator.create()
    return animationIntCreator.animation
}

@AnimationDSL
class AnimationDoubleCreator(initialValue : Double)
    : DslAnimationKeyFrame<ObservableSource<Double>, Double>(AnimationDouble(initialValue))
{
    val animation : AnimationDouble = this.animationKeyTime as AnimationDouble
}

@AnimationDSL
class AnimationFloatCreator(initialValue : Float)
    : DslAnimationKeyFrame<ObservableSource<Float>, Float>(AnimationFloat(initialValue))
{
    val animation : AnimationFloat = this.animationKeyTime as AnimationFloat
}

@AnimationDSL
class AnimationLongCreator(initialValue : Long)
    : DslAnimationKeyFrame<ObservableSource<Long>, Long>(AnimationLong(initialValue))
{
    val animation : AnimationLong = this.animationKeyTime as AnimationLong
}

@AnimationDSL
class AnimationIntCreator(initialValue : Int)
    : DslAnimationKeyFrame<ObservableSource<Int>, Int>(AnimationInt(initialValue))
{
    val animation : AnimationInt = this.animationKeyTime as AnimationInt
}