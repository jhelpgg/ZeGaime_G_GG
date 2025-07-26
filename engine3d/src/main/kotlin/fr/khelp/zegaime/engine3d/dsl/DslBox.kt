package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Box
import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@PrebuiltDSL
fun Box.boxUV(fill : BoxUVCreator.() -> Unit)
{
    val boxUVCreator = BoxUVCreator()
    boxUVCreator.fill()
    this.boxUV(boxUVCreator())
}

@PrebuiltDSL
fun Box.crossUV(fill : CrossUVCreator.() -> Unit)
{
    val crossUVCreator = CrossUVCreator()
    crossUVCreator.fill()
    this.boxUV(crossUVCreator())
}

@PrebuiltDSL
fun box(boxID : String, create : BoxCreator.() -> Unit) : Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    return boxCreator()
}

@PrebuiltDSL
class BoxCreator(private val boxID : String)
{
    var position = NodePosition()
    var material : Material = Material()
    var materialForSelection : Material = Material()
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR
    private var boxUV = BoxUV()

    @UvDSL
    fun boxUV(create : BoxUVCreator.() -> Unit)
    {
        val boxUVCreator = BoxUVCreator()
        boxUVCreator.create()
        this.boxUV = boxUVCreator()
    }

    @UvDSL
    fun crossUV(create : CrossUVCreator.() -> Unit)
    {
        val crossUVCreator = CrossUVCreator()
        crossUVCreator.create()
        this.boxUV = crossUVCreator()
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

    internal operator fun invoke() : Box
    {
        val box = Box(this.boxID, this.boxUV)
        box.position(this.position)
        box.material = this.material
        box.materialForSelection = this.materialForSelection
        box.wireColor = this.wireColor
        return box
    }
}