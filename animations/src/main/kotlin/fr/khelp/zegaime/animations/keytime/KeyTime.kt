package fr.khelp.zegaime.animations.keytime

import fr.khelp.zegaime.animations.interpolation.Interpolation

/**
 * Represents a keyframe in an [AnimationKeyTime].
 *
 * A keyframe defines a value at a specific time, and an interpolation method to reach this value.
 *
 * This class is for internal use of the animation system.
 *
 * @param V The type of the value.
 * @property timeMilliseconds The time in milliseconds from the start of the animation.
 * @property value The value at the given time.
 * @property interpolation The interpolation to use to reach this value.
 * @constructor Create a new key time.
 */
internal data class KeyTime<V : Any>(val timeMilliseconds: Long,
                                     val value: V,
                                     val interpolation: Interpolation) : Comparable<KeyTime<V>>
{
    /**
     * Compares this key time with another one based on their time.
     *
     * @param other The other key time to compare with.
     * @return A negative integer, zero, or a positive integer as this key time is less than, equal to, or greater than the specified key time.
     */
    override fun compareTo(other: KeyTime<V>): Int =
        this.timeMilliseconds.compareTo(other.timeMilliseconds)
}