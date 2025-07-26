package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@PrebuiltDSL
fun plane(planeID : String, create : PlaneCreator.() -> Unit) : Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.create()
    return planeCreator()
}

@PrebuiltDSL
class PlaneCreator(private val planeID : String)
{
    var position = NodePosition()
    var material : Material = Material()
    var materialForSelection : Material = Material()
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    private val faceUVCreator = FaceUVCreator()

    @UvDSL
    fun faceUV(create : FaceUVCreator.() -> Unit)
    {
        this.faceUVCreator.create()
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

    internal operator fun invoke() : Plane
    {
        val plane = Plane(this.planeID, this.faceUVCreator())
        plane.position(this.position)
        plane.material = this.material
        plane.materialForSelection = this.materialForSelection
        plane.wireColor = this.wireColor
        return plane
    }
}
