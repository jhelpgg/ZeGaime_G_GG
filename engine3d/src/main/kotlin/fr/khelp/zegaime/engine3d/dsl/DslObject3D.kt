package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@MeshDSL
fun Object3D.mesh(meshFiller : Mesh.() -> Unit)
{
    this.mesh.meshFiller()
    this.refresh()
}

@PrebuiltDSL
fun object3D(objectID : String, create : Object3DCreator.() -> Unit) : Object3D
{
    val object3DCreator = Object3DCreator(objectID)
    object3DCreator.create()
    return object3DCreator.object3D
}

@PrebuiltDSL
class Object3DCreator(objectID : String)
{
    internal val object3D = Object3D(objectID)

    var position : NodePosition
        get() = this.object3D.position
        set(value)
        {
            this.object3D.position(value)
        }

    var material : Material
        get() = this.object3D.material
        set(value)
        {
            this.object3D.material = value
        }

    var materialForSelection : Material
        get() = this.object3D.materialForSelection
        set(value)
        {
            this.object3D.materialForSelection = value
        }

    var wireColor : Color4f
        get() = this.object3D.wireColor
        set(value)
        {
            this.object3D.wireColor = value
        }

    var twoSidedRule : TwoSidedRule
        get() = this.object3D.twoSidedRule
        set(value)
        {
            this.object3D.twoSidedRule = value
        }

    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    @MeshDSL
    fun mesh(meshFiller : Mesh.() -> Unit)
    {
        this.object3D.mesh(meshFiller)
    }
}