package fr.khelp.zegaime.engine3d.geometry

import fr.khelp.zegaime.engine3d.utils.BarycenterPoint3D
import fr.khelp.zegaime.utils.collections.iterations.transform
import kotlin.math.max
import org.lwjgl.opengl.GL11

/**
 * Represents a face of a 3D object.
 *
 * A face is a polygon defined by a list of vertices.
 *
 * **Creation example:**
 * ```kotlin
 * val face = Face()
 * face.add(Vertex(Point3D(0f, 0f, 0f), Point2D(0f, 0f), Point3D(0f, 0f, 1f)))
 * face.add(Vertex(Point3D(1f, 0f, 0f), Point2D(1f, 0f), Point3D(0f, 0f, 1f)))
 * face.add(Vertex(Point3D(1f, 1f, 0f), Point2D(1f, 1f), Point3D(0f, 0f, 1f)))
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val mesh = Mesh()
 * mesh.addFace(face)
 * ```
 *
 * @property size The number of vertices in the face.
 * @constructor Creates a new face. For internal use only.
 */
class Face : Iterable<Vertex>
{
    internal var barycenter : BarycenterPoint3D = BarycenterPoint3D()
    internal var virtualBox : VirtualBox = VirtualBox()
    private val vertices = ArrayList<Vertex>()

    /**
     * The number of vertices in the face.
     */
    val size : Int get() = this.vertices.size

    /**
     * Adds a vertex to the face.
     *
     * @param x The X coordinate of the vertex.
     * @param y The Y coordinate of the vertex.
     * @param z The Z coordinate of the vertex.
     * @param uvU The U coordinate of the texture.
     * @param uvV The V coordinate of the texture.
     * @param normalX The X coordinate of the normal.
     * @param normalY The Y coordinate of the normal.
     * @param normalZ The Z coordinate of the normal.
     */
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

    /**
     * Adds a vertex to the face.
     *
     * @param position The position of the vertex.
     * @param uv The texture coordinates of the vertex.
     * @param normal The normal of the vertex.
     */
    fun add(position : Point3D, uv : Point2D, normal : Point3D)
    {
        this.barycenter.add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        this.virtualBox.add(position.x, position.y, position.z)
        this.vertices.add(Vertex(position, uv, normal))
    }

    /**
     * Adds a vertex to the face.
     *
     * @param vertex The vertex to add.
     */
    fun add(vertex : Vertex)
    {
        this.barycenter.add(vertex.position.x.toDouble(), vertex.position.y.toDouble(), vertex.position.z.toDouble())
        this.virtualBox.add(vertex.position.x, vertex.position.y, vertex.position.z)
        this.vertices.add(vertex)
    }

    /**
     * Inserts a vertex at the specified index.
     *
     * @param index The index at which to insert the vertex.
     * @param vertex The vertex to insert.
     */
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

    /**
     * Returns the vertex at the specified index.
     *
     * @param index The index of the vertex.
     * @return The vertex at the specified index.
     */
    operator fun get(index : Int) : Vertex = this.vertices[index]

    /**
     * Creates a copy of the face.
     *
     * @return A copy of the face.
     */
    fun copy() : Face
    {
        val copy = Face()

        for (vertex in this.vertices)
        {
            copy.add(vertex.copy())
        }

        return copy
    }

    /**
     * Copies the vertices of another face to this one.
     *
     * @param face The face to copy from.
     */
    fun copy(face : Face)
    {
        this.vertices.clear()

        for (vertex in face.vertices)
        {
            this.vertices.add(vertex.copy())
        }
    }

    /**
     * Renders the face.
     *
     * For internal use only.
     */
    internal fun render()
    {
        GL11.glBegin(GL11.GL_POLYGON)

        for (vertex in this.vertices)
        {
            vertex.render()
        }

        GL11.glEnd()
    }

    /**
     * Moves a point of the face.
     *
     * For internal use only.
     *
     * @param point The point to move.
     * @param vector The vector of the movement.
     * @param solidity The solidity of the movement.
     * @param solidityFactor The solidity factor of the movement.
     * @param near The number of near points to move.
     */
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

    /**
     * Refills the barycenter and the virtual box of the face.
     *
     * For internal use only.
     */
    internal fun refillBarycenterAndVirtualBox()
    {
        for (point in this.vertices.transform { vertex -> vertex.position })
        {
            this.barycenter.add(point)
            this.virtualBox.add(point)
        }
    }

    /**
     * Returns an iterator over the vertices of the face.
     */
    override fun iterator() : Iterator<Vertex> = this.vertices.iterator()
}
