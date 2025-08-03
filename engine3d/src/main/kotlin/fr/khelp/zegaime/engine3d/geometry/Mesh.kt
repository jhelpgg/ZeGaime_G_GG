package fr.khelp.zegaime.engine3d.geometry

import fr.khelp.zegaime.animations.interpolation.InterpolationSine
import fr.khelp.zegaime.engine3d.utils.BarycenterPoint3D

class Mesh : Iterable<Face>
{
    private val faces = ArrayList<Face>()
    private val barycenter = BarycenterPoint3D()
    val center : Point3D get() = this.barycenter.barycenter()
    val virtualBox : VirtualBox = VirtualBox()
    val size : Int get() = this.faces.size

    fun addFace(face : Face)
    {
        face.barycenter = this.barycenter
        face.virtualBox = this.virtualBox
        this.faces.add(face)
    }

    fun movePoint(point : Point3D, vector : Point3D, solidity : Float, near : Int)
    {
        this.barycenter.reset()
        this.virtualBox.reset()
        val factor = InterpolationSine(solidity.toDouble()).toFloat()

        for (face in this.faces)
        {
            face.movePoint(point, vector, solidity, factor, near)
            face.refillBarycenterAndVirtualBox()
        }
    }

    fun removeLastFace()
    {
        if (this.faces.isNotEmpty())
        {
            this.faces.removeAt(this.faces.size - 1)
            this.barycenter.reset()
            this.virtualBox.reset()

            for (face in this.faces)
            {
                face.refillBarycenterAndVirtualBox()
            }
        }
    }

    fun removeFace(index : Int)
    {
        this.faces.removeAt(index)
        this.barycenter.reset()
        this.virtualBox.reset()

        for (face in this.faces)
        {
            face.refillBarycenterAndVirtualBox()
        }
    }

    fun refresh()
    {
        this.barycenter.reset()
        this.virtualBox.reset()

        for (face in this.faces)
        {
            face.refillBarycenterAndVirtualBox()
        }
    }

    fun clear()
    {
        this.virtualBox.reset()
        this.barycenter.reset()
        this.faces.clear()
    }

    operator fun get(index : Int) : Face = this.faces[index]

    fun copy() : Mesh
    {
        val copy = Mesh()

        for (face in this.faces)
        {
            copy.faces.add(face.copy())
        }

        return copy
    }

    fun copy(copy : Mesh)
    {
        this.faces.clear()

        for (face in copy.faces)
        {
            this.faces.add(face.copy())
        }
    }

    internal fun render()
    {
        for (face in this.faces)
        {
            face.render()
        }
    }

    override fun iterator() : Iterator<Face> = this.faces.iterator()
}
