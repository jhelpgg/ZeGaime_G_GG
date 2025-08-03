package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Point2D
import kotlinx.serialization.Serializable

/**
 * Point 2D data for save
 * @param x X coordinate
 * @param y Y coordinate
 */
@Serializable
data class Point2DData(val x : Float, val y : Float)
{
    /**
     * Create a new instance of Point2DData from a Point2D
     * @param point Point2D to copy
     */
    constructor(point : Point2D) : this(x = point.x, y = point.y)

    /**
     * The Point2D
     */
    val point : Point2D by lazy { Point2D(x = this.x, y = this.y) }
}
