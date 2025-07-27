package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Plane
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Creates a plane using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val plane = plane("myPlane") {
 *     position = NodePosition(x = 1f)
 *     faceUV {
 *         minU = 0.1f
 *         maxU = 0.42f
 *         minV = 0.23f
 *         maxV = 0.73f
 *     }
 * }
 * ```
 *
 * @param planeID The ID of the plane.
 * @param create The lambda function to create the plane.
 * @return The created plane.
 */
@PrebuiltDSL
fun plane(planeID : String, create : PlaneCreator.() -> Unit) : Plane
{
    val planeCreator = PlaneCreator(planeID)
    planeCreator.create()
    return planeCreator()
}

/**
 * DSL creator for planes.
 *
 * @property planeID The ID of the plane.
 * @constructor Creates a new plane creator.
 */
@PrebuiltDSL
class PlaneCreator(private val planeID : String)
{
    /**
     * The position of the plane.
     */
    var position = NodePosition()

    /**
     * The material of the plane.
     */
    var material : Material = Material()

    /**
     * The material of the plane when it is selected.
     */
    var materialForSelection : Material = Material()

    /**
     * The color of the wireframe.
     */
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    private val faceUVCreator = FaceUVCreator()

    /**
     * Defines the UV mapping for the plane.
     *
     * @param create The lambda function to create the UV mapping.
     */
    @UvDSL
    fun faceUV(create : FaceUVCreator.() -> Unit)
    {
        this.faceUVCreator.create()
    }

    /**
     * Creates the material for the plane.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    /**
     * Creates the material for the plane when it is selected.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    /**
     * Creates the plane.
     *
     * For internal use only.
     *
     * @return The created plane.
     */
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