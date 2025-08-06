package fr.khelp.zegaime.engine3d.gui.style

import kotlin.math.max


enum class ComponentHighLevel(val level : Int)
{
    AT_GROUND(0),
    NEAR_GROUND(2),
    FLY(4),
    HIGHEST(8)
}

val COMPONENT_HIGHEST_LEVEL : Int by lazy {
    var maximum = 0

    for (componentHighLevel in ComponentHighLevel.entries)
    {
        maximum = max(maximum, componentHighLevel.level)
    }

    maximum
}
