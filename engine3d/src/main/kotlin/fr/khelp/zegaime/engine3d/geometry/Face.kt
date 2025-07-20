package fr.khelp.zegaime.engine3d.geometry

import fr.khelp.zegaime.engine3d.utils.BarycenterPoint3D
import fr.khelp.zegaime.utils.collections.iterations.transform
import kotlin.math.max
import org.lwjgl.opengl.GL11

class Face internal constructor() : Iterable<Vertex>
{
    companion object
    {
        fun provider() : Face = Face()
    }

    internal var barycenter : BarycenterPoint3D = BarycenterPoint3D()
    internal var virtualBox : VirtualBox = VirtualBox()
    private val vertices = ArrayList<Vertex>()
    val size : Int get() = this.vertices.size

    fun add(x : Float, y : Float, z : Float,
            uvU : Float, uvV : Float,
            normalX : Float, normalY : Float, normalZ : Float)
    {
        this.barycenter.add(x.toDouble(), y.toDouble(), z.toDouble())
        this.virtualBox.add(x, y, z)
        this.vertices.add(Vertex(Point3D(x, y, z),
                                 Point2D(uvU, uvV),
                                 Point3D(normalX, normalY, normalZ)))
    }

    fun add(position : Point3D, uv : Point2D, normal : Point3D)
    {
        this.barycenter.add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        this.virtualBox.add(position.x, position.y, position.z)
        this.vertices.add(Vertex(position, uv, normal))
    }

    fun add(vertex : Vertex)
    {
        this.barycenter.add(vertex.position.x.toDouble(), vertex.position.y.toDouble(), vertex.position.z.toDouble())
        this.virtualBox.add(vertex.position.x, vertex.position.y, vertex.position.z)
        this.vertices.add(vertex)
    }

    fun insert(index : Int, vertex : Vertex)
    {
        this.barycenter.add(vertex.position.x.toDouble(), vertex.position.y.toDouble(), vertex.position.z.toDouble())
        this.virtualBox.add(vertex.position.x, vertex.position.y, vertex.position.z)
        val indexInsert = max(0, index)

        if (indexInsert >= this.size)
        {
            this.vertices.add(vertex)
        }
        else
        {
            this.vertices.add(indexInsert, vertex)
        }
    }

    operator fun get(index : Int) : Vertex = this.vertices[index]

    fun copy() : Face
    {
        val copy = Face()

        for (vertex in this.vertices)
        {
            copy.add(vertex.copy())
        }

        return copy
    }

    fun copy(face : Face)
    {
        this.vertices.clear()

        for (vertex in face.vertices)
        {
            this.vertices.add(vertex.copy())
        }
    }

    internal fun render()
    {
        GL11.glBegin(GL11.GL_POLYGON)

        for (vertex in this.vertices)
        {
            vertex.render()
        }

        GL11.glEnd()
    }

    internal fun movePoint(point : Point3D, vector : Point3D, solidity : Float, solidityFactor : Float, near : Int)
    {
        val indexStart = this.vertices.indexOfFirst { vertex -> vertex.position == point }

        if (indexStart < 0)
        {
            return
        }

        var vertex = this.vertices[indexStart]
        this.vertices[indexStart] = Vertex(vertex.position + vector, vertex.uv, vertex.normal)
        var newVector = vector
        var newSolidity = solidity
        var indexForward = indexStart
        var indexBackward = indexStart
        var isLast : Boolean
        val size = this.vertices.size

        for (time in 0 until near)
        {
            isLast = true
            newVector *= newSolidity
            newSolidity *= solidityFactor

            indexForward = (indexForward + 1) % size

            if (indexForward != indexStart)
            {
                isLast = false
                vertex = this.vertices[indexForward]
                this.vertices[indexForward] = Vertex(vertex.position + newVector, vertex.uv, vertex.normal)
            }

            indexBackward = (indexBackward + size - 1) % size

            if (indexBackward != indexStart)
            {
                isLast = false
                vertex = this.vertices[indexBackward]
                this.vertices[indexBackward] = Vertex(vertex.position + newVector, vertex.uv, vertex.normal)
            }

            if (isLast)
            {
                break
            }
        }
    }

    internal fun refillBarycenterAndVirtualBox()
    {
        for (point in this.vertices.transform { vertex -> vertex.position })
        {
            this.barycenter.add(point)
            this.virtualBox.add(point)
        }
    }

    override fun iterator() : Iterator<Vertex> = this.vertices.iterator()
}