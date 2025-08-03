package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.geometry.Mesh
import kotlinx.serialization.Serializable

/**
 * Mesh data for save
 * @param faces Mesh faces
 */
@Serializable
data class MeshData(val faces : List<FaceData>)
{
    /**
     * Create a new instance of MeshData from a Mesh
     * @param mesh Mesh to copy
     */
    constructor(mesh : Mesh) : this(faces = mesh.map { face -> FaceData(face) })

    /**
     * The Mesh
     */
    val mesh : Mesh by lazy {
        val mesh = Mesh()

        for (faceData in this.faces)
        {
            mesh.addFace(face = faceData.face)
        }

        mesh
    }
}
