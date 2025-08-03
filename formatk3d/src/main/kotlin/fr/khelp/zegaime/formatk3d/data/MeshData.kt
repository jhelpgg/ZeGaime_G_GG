package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Mesh
import kotlinx.serialization.Serializable

@Serializable
data class MeshData(val faces : List<FaceData>)
{
    constructor(mesh : Mesh) : this(faces = mesh.map { face -> FaceData(face) })

    val mesh : Mesh by lazy {
        val mesh = Mesh()

        for (faceData in this.faces)
        {
            mesh.addFace(face = faceData.face)
        }

        mesh
    }
}