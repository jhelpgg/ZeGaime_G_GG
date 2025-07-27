package fr.khelp.zegaime.engine3d.loader.obj

import fr.khelp.zegaime.engine3d.geometry.Point2D
import fr.khelp.zegaime.engine3d.geometry.Point3D

/**
 * Description of an `obj` object
 *
 * @property name Object name
 */
internal class ObjObject(var name : String)
{
    /** Object's points */
    val points = ArrayList<Point3D>()

    /** Object's UV */
    val uvs = ArrayList<Point2D>()

    /** Object's normals */
    val normals = ArrayList<Point3D>()

    /** Object's faces */
    val faces = ArrayList<ObjFace>()

    /**
     * Creates a sub-object
     *
     * @param name Sub-object name
     *
     * @return Created sub-object
     */
    fun subObject(name : String) : ObjObject
    {
        val subObject = ObjObject(name)
        subObject.points.addAll(this.points)
        subObject.uvs.addAll(this.uvs)
        subObject.normals.addAll(this.normals)
        return subObject
    }
}
