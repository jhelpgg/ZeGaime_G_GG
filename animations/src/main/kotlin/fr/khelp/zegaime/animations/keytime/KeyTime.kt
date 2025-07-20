package fr.khelp.zegaime.animations.keytime

import fr.khelp.zegaime.animations.interpolation.Interpolation

/**
 * Value at a defined time
 *
 * @property timeMilliseconds Time, in milliseconds, since animation starts key
 * @property value Associated value
 * @property interpolation Interpolation to use to get there
 */
internal data class KeyTime<V : Any>(val timeMilliseconds : Long,
                                     val value : V,
                                     val interpolation : Interpolation) : Comparable<KeyTime<V>>
{
    override fun compareTo(other : KeyTime<V>) : Int =
        this.timeMilliseconds.compareTo(other.timeMilliseconds)
}
