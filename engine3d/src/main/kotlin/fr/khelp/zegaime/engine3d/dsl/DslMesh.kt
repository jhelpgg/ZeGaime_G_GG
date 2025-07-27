package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.Face
import fr.khelp.zegaime.engine3d.geometry.Mesh

/**
 * Adds a face to the mesh using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myMesh.face {
 *     add(position, uv, normal)
 * }
 * ```
 *
 * @param faceFiller The lambda function to create the face.
 */
@FaceDSL
fun Mesh.face(faceFiller : Face.() -> Unit)
{
    val face = Face()
    face.faceFiller()
    this.addFace(face)
}
