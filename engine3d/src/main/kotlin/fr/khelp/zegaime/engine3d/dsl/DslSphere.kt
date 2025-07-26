package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position
import kotlin.math.max

@PrebuiltDSL
fun sphere(sphereID : String, create : SphereCreator.() -> Unit) : Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.create()
    return sphereCreator()
}

@PrebuiltDSL
class SphereCreator(private val sphereID : String)
{
    var position = NodePosition()
    var material : Material = Material()
    var materialForSelection : Material = Material()
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    var slice : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    var stack : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    var multiplierU : Float = 1f
    var multiplierV : Float = 1f

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

    internal operator fun invoke() : Sphere
    {
        val sphere = Sphere(id = this.sphereID,
                            slice = this.slice, stack = this.stack,
                            multiplierU = this.multiplierU, multiplierV = this.multiplierV)
        sphere.position(this.position)
        sphere.material = this.material
        sphere.materialForSelection = this.materialForSelection
        sphere.wireColor = this.wireColor
        return sphere
    }
}