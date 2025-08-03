package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Face
import kotlinx.serialization.Serializable

/**
 * Face data for save
 * @param vertices Face vertices
 */
@Serializable
data class FaceData(val vertices : List<VertexData>)
{
    /**
     * Create a new instance of FaceData from a Face
     * @param face Face to copy
     */
    constructor(face : Face) : this(vertices = face.map { vertex -> VertexData(vertex) })

    /**
     * The Face
     */
    val face : Face by lazy {
        val face = Face()

        for (vertexData in this.vertices)
        {
            face.add(vertexData.vertex)
        }

        face
    }
}