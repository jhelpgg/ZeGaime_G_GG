package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Face
import kotlinx.serialization.Serializable

@Serializable
data class FaceData(val vertices : List<VertexData>)
{
    constructor(face : Face) : this(vertices = face.map { vertex -> VertexData(vertex) })

    val face : Face by lazy {
        val face = Face()

        for (vertexData in this.vertices)
        {
            face.add(vertexData.vertex)
        }

        face
    }
}