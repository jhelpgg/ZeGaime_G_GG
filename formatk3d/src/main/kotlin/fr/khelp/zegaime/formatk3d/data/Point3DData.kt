package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Point3D
import kotlinx.serialization.Serializable

@Serializable
data class Point3DData(val x : Float, val y : Float, val z : Float)
{
    constructor(point : Point3D) : this(x = point.x, y = point.y, z = point.z)

    val point : Point3D by lazy { Point3D(x = this.x, y = this.y, z = this.z) }
}
