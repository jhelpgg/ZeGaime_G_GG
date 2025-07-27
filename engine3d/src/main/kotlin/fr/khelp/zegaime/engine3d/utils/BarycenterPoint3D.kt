package fr.khelp.zegaime.engine3d.utils

import fr.khelp.zegaime.engine3d.geometry.Point3D
import fr.khelp.zegaime.utils.math.Barycenter

/**
 * Barycenter of several points in space.
 *
 * **Creation example:**
 * ```kotlin
 * val barycenter = BarycenterPoint3D()
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * barycenter.add(Point3D(1f, 2f, 3f))
 * barycenter.add(Point3D(4f, 5f, 6f))
 * val center = barycenter.barycenter()
 * ```
 *
 * @property empty Indicates if the barycenter is empty.
 * @constructor Creates a new barycenter.
 */
class BarycenterPoint3D
{
    /**
     * Barycenter for X
     */
    private val barycenterX = Barycenter()

    /**
     * Barycenter for Y
     */
    private val barycenterY = Barycenter()

    /**
     * Barycenter for Z
     */
    private val barycenterZ = Barycenter()

    /**
     * Add point to the set.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    fun add(x: Double, y: Double, z: Double)
    {
        this.barycenterX.add(x)
        this.barycenterY.add(y)
        this.barycenterZ.add(z)
    }

    /**
     * Add point to the set.
     *
     * @param point Point to add
     */
    fun add(point: Point3D) = this.add(point.x.toDouble(), point.y.toDouble(), point.z.toDouble())

    /**
     * Indicates if barycenter is empty.
     *
     * That is to say if no point are already add.
     */
    val empty get() = this.barycenterX.empty

    /**
     * Barycenter of added points.
     *
     * Accurate if at at least one point was add.
     *
     * @return The barycenter of the added points.
     */
    fun barycenter() = Point3D(this.barycenterX.barycenter.toFloat(),
                               this.barycenterY.barycenter.toFloat(),
                               this.barycenterZ.barycenter.toFloat())

    /**
     * Resets the barycenter.
     */
    fun reset()
    {
        this.barycenterX.reset()
        this.barycenterY.reset()
        this.barycenterZ.reset()
    }
}
