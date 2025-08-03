package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Point2D
import kotlinx.serialization.Serializable

@Serializable
data class Point2DData(val x : Float, val y : Float)
{
    constructor(point : Point2D) : this(x = point.x, y = point.y)

    val point : Point2D by lazy { Point2D(x = this.x, y = this.y) }
}
