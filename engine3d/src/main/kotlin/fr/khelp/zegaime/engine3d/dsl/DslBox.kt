package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Box
import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Defines the UV mapping for a box.
 *
 * @param fill The lambda function to create the UV mapping.
 */
@PrebuiltDSL
fun Box.boxUV(fill : BoxUVCreator.() -> Unit)
{
    val boxUVCreator = BoxUVCreator()
    boxUVCreator.fill()
    this.boxUV(boxUVCreator())
}

/**
 * Defines the UV mapping for a cross.
 *
 * @param fill The lambda function to create the UV mapping.
 */
@PrebuiltDSL
fun Box.crossUV(fill : CrossUVCreator.() -> Unit)
{
    val crossUVCreator = CrossUVCreator()
    crossUVCreator.fill()
    this.boxUV(crossUVCreator())
}

/**
 * Creates a box using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val box = box("myBox") {
 *     position = NodePosition(x = 1f)
 *     boxUV {
 *         top {
 *              minU = 0.1f
 *              maxU = 0.85f
 *              minV = 0f
 *              maxV = 2f // For repeat 2 times in V
 *         }
 *         // ...
 *     }
 * }
 * ```
 *
 * @param boxID The ID of the box.
 * @param create The lambda function to create the box.
 * @return The created box.
 */
@PrebuiltDSL
fun box(boxID : String, create : BoxCreator.() -> Unit) : Box
{
    val boxCreator = BoxCreator(boxID)
    boxCreator.create()
    return boxCreator()
}

/**
 * DSL creator for boxes.
 *
 * @property boxID The ID of the box.
 * @constructor Creates a new box creator.
 */
@PrebuiltDSL
class BoxCreator(private val boxID : String)
{
    /**
     * The position of the box.
     */
    var position = NodePosition()

    /**
     * The material of the box.
     */
    var material : Material = Material()

    /**
     * The material of the box when it is selected.
     */
    var materialForSelection : Material = Material()

    /**
     * The color of the wireframe.
     */
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR
    private var boxUV = BoxUV()

    /**
     * Defines the UV mapping for the box.
     *
     * @param create The lambda function to create the UV mapping.
     */
    @UvDSL
    fun boxUV(create : BoxUVCreator.() -> Unit)
    {
        val boxUVCreator = BoxUVCreator()
        boxUVCreator.create()
        this.boxUV = boxUVCreator()
    }

    /**
     * Defines the UV mapping for a cross.
     *
     * @param create The lambda function to create the UV mapping.
     */
    @UvDSL
    fun crossUV(create : CrossUVCreator.() -> Unit)
    {
        val crossUVCreator = CrossUVCreator()
        crossUVCreator.create()
        this.boxUV = crossUVCreator()
    }

    /**
     * Creates the material for the box.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    /**
     * Creates the material for the box when it is selected.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    /**
     * Creates the box.
     *
     * For internal use only.
     *
     * @return The created box.
     */
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
