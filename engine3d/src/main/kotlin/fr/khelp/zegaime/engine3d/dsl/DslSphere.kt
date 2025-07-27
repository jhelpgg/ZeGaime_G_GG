package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Sphere
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position
import kotlin.math.max

/**
 * Creates a sphere using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val sphere = sphere("mySphere") {
 *     position = NodePosition(x = 1f)
 *     slice = 16
 *     stack = 16
 * }
 * ```
 *
 * @param sphereID The ID of the sphere.
 * @param create The lambda function to create the sphere.
 * @return The created sphere.
 */
@PrebuiltDSL
fun sphere(sphereID : String, create : SphereCreator.() -> Unit) : Sphere
{
    val sphereCreator = SphereCreator(sphereID)
    sphereCreator.create()
    return sphereCreator()
}

/**
 * DSL creator for spheres.
 *
 * @property sphereID The ID of the sphere.
 * @constructor Creates a new sphere creator.
 */
@PrebuiltDSL
class SphereCreator(private val sphereID : String)
{
    /**
     * The position of the sphere.
     */
    var position = NodePosition()

    /**
     * The material of the sphere.
     */
    var material : Material = Material()

    /**
     * The material of the sphere when it is selected.
     */
    var materialForSelection : Material = Material()

    /**
     * The color of the wireframe.
     */
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    /**
     * The number of the sphere's slices.
     */
    var slice : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    /**
     * The number of the sphere's stacks.
     */
    var stack : Int = 33
        set(value)
        {
            field = max(2, value)
        }

    /**
     * The multiplier for the U coordinate.
     */
    var multiplierU : Float = 1f

    /**
     * The multiplier for the V coordinate.
     */
    var multiplierV : Float = 1f

    /**
     * Creates the material for the sphere.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    /**
     * Creates the material for the sphere when it is selected.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    /**
     * Creates the sphere.
     *
     * For internal use only.
     *
     * @return The created sphere.
     */
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
