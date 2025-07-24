package fr.khelp.zegaime.engine3d.geometry.path

import fr.khelp.zegaime.engine3d.geometry.path.element.PathElement
import fr.khelp.zegaime.utils.math.square
import kotlin.math.sqrt

data class PathLine(val x1 : Float, val y1 : Float, var information1 : Float,
                    val x2 : Float, val y2 : Float, var information2 : Float,
                    val pathElement : PathElement, val elementIndex : Int)
{
    val distance : Float = sqrt(square(this.x1 - this.x2) + square(this.y1 - this.y2))
}
