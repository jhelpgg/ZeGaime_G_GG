package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.engine3d.scene.Object3D

@MeshDSL
fun Object3D.mesh(meshFiller : Mesh.() -> Unit)
{
    this.mesh.meshFiller()
    this.refresh()
}