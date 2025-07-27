package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.path.Path
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.DEFAULT_WIRE_FRAME_COLOR
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.scene.prebuilt.Revolution
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Edits a revolution object using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * myRevolution.edit {
 *     angle = 180f
 * }
 * ```
 *
 * @param pathFiller The lambda function to edit the revolution object.
 */
@PrebuiltDSL
fun Revolution.edit(pathFiller: RevolutionCreator.() -> Unit)
{
    val revolutionCreator = RevolutionCreator(this)
    revolutionCreator.pathFiller()
    revolutionCreator()
}

/**
 * Creates a revolution object using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val revolution = revolution("myRevolution") {
 *     path {
 *         moveTo(0f, 0f)
 *         lineTo(1f, 1f)
 *     }
 * }
 * ```
 *
 * @param id The ID of the revolution object.
 * @param create The lambda function to create the revolution object.
 * @return The created revolution object.
 */
@PrebuiltDSL
fun revolution(id: String, create: RevolutionCreator.() -> Unit): Revolution
{
    val revolutionCreator = RevolutionCreator(Revolution(id))
    revolutionCreator.create()
    return revolutionCreator()
}

/**
 * DSL creator for revolution objects.
 *
 * @property revolution The revolution object to create.
 * @constructor Creates a new revolution creator.
 */
@PrebuiltDSL
class RevolutionCreator(private val revolution: Revolution)
{
    /**
     * The position of the revolution object.
     */
    var position = NodePosition()
    /**
     * The material of the revolution object.
     */
    var material: Material = Material()
    /**
     * The material of the revolution object when it is selected.
     */
    var materialForSelection: Material = Material()
    /**
     * The color of the wireframe.
     */
    var wireColor: Color4f = DEFAULT_WIRE_FRAME_COLOR

    /**
     * The precision of the path.
     */
    var precision: Int = 5
        set(value)
        {
            field = value.coerceIn(2, 12)
        }

    /**
     * The angle of the revolution.
     */
    var angle: Float = 360f
        set(value)
        {
            field = value.coerceIn(1f, 360f)
        }

    /**
     * The precision of the rotation.
     */
    var rotationPrecision: Int = 12
        set(value)
        {
            field = value.coerceIn(3, 32)
        }

    /**
     * The start of the path.
     */
    var start: Float = 0f

    /**
     * The end of the path.
     */
    var end: Float = 1f

    /**
     * The multiplier for the U coordinate.
     */
    var multiplierU: Float = 1f

    private var path = Path()

    /**
     * Defines the path for the revolution.
     *
     * @param pathFiller The lambda function to create the path.
     */
    @PathDSL
    fun path(pathFiller: Path.() -> Unit)
    {
        this.path.clear()
        this.path.pathFiller()
    }

    /**
     * Creates the material for the revolution object.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialCreate(create: Material.() -> Unit)
    {
        this.material = material(create)
    }

    /**
     * Creates the material for the revolution object when it is selected.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialForSelectionCreate(create: Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    /**
     * Creates the revolution object.
     *
     * For internal use only.
     *
     * @return The created revolution object.
     */
    internal operator fun invoke(): Revolution
    {
        this.revolution.path(this.precision,
                             this.angle, this.rotationPrecision,
                             this.start, this.end,
                             this.multiplierU,
                             this.path)
        this.revolution.position(this.position)
        this.revolution.material = this.material
        this.revolution.materialForSelection = this.materialForSelection
        this.revolution.wireColor = this.wireColor
        return this.revolution
    }
}
