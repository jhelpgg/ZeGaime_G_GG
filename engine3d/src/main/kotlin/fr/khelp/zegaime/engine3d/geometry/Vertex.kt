package fr.khelp.zegaime.engine3d.geometry

class Vertex(position : Point3D, uv : Point2D, normal : Point3D)
{
    companion object
    {
        fun provider() : Vertex = Vertex(Point3D(), Point2D(), Point3D())
    }

    var position = position
        private set
    var uv = uv
        private set
    var normal = normal
        private set

    fun copy() : Vertex = Vertex(this.position.copy(), this.uv.copy(), this.normal.copy())

    fun copy(vertex : Vertex)
    {
        this.position.copy(vertex.position)
        this.uv.copy(vertex.uv)
        this.normal.copy(vertex.normal)
    }

    fun middle(vertex : Vertex) : Vertex = Vertex(this.position.middle(vertex.position),
                                                  this.uv.middle(vertex.uv),
                                                  this.normal.middle(vertex.normal))

    internal fun render()
    {
        this.normal.glNormal3f()
        this.uv.glTexCoord2f()
        this.position.glVertex3f()
    }
}
