package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Point3D
import kotlinx.serialization.Serializable

/**
 * Point 3D data for save
 * @param x X coordinate
 * @param y Y coordinate
 * @param z Z coordinate
 */
@Serializable
data class Point3DData(val x : Float, val y : Float, val z : Float)
{
    /**
     * Create a new instance of Point3DData from a Point3D
     * @param point Point3D to copy
     */
    constructor(point : Point3D) : this(x = point.x, y = point.y, z = point.z)

    /**
     * The Point3D
     */
    val point : Point3D by lazy { Point3D(x = this.x, y = this.y, z = this.z) }
}
