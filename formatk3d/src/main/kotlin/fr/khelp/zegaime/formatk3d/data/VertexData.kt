package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Vertex
import kotlinx.serialization.Serializable

/**
 * Data to store vertx information
 *
 * @property position Vertex position
 * @property uv Vertex UV
 * @property normal Vertex normal
 */
@Serializable
data class VertexData(val position : Point3DData, val uv : Point2DData, val normal : Point3DData)
{
    /**
     * Creates data from vertex
     *
     * @param vertex Vertex where get information
     */
    constructor(vertex : Vertex) : this(position = Point3DData(vertex.position),
                                        uv = Point2DData(vertex.uv),
                                        normal = Point3DData(vertex.normal))

    /** Vertex represented by the information */
    val vertex : Vertex by lazy {
        Vertex(position = this.position.point,
               uv = this.uv.point,
               normal = this.normal.point)
    }
}
