package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.Face
import fr.khelp.zegaime.engine3d.geometry.Mesh

@FaceDSL
fun Mesh.face(faceFiller : Face.() -> Unit)
{
    val face = Face()
    face.faceFiller()
    this.addFace(face)
}