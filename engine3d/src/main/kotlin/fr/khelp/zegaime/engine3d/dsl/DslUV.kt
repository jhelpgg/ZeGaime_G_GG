package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.scene.prebuilt.BoxUV
import fr.khelp.zegaime.engine3d.scene.prebuilt.CrossUV
import fr.khelp.zegaime.engine3d.scene.prebuilt.FaceUV

/**
 * Creates a face UV mapping using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val faceUV = faceUV {
 *     minU = 0.25f
 *     maxU = 0.75f
 * }
 * ```
 *
 * @param create The lambda function to create the face UV mapping.
 * @return The created face UV mapping.
 */
@UvDSL
fun faceUV(create: FaceUVCreator.() -> Unit): FaceUV
{
    val faceUVCreator = FaceUVCreator()
    faceUVCreator.create()
    return faceUVCreator()
}

/**
 * DSL creator for face UV mappings.
 *
 * @constructor Creates a new face UV creator.
 */
@UvDSL
class FaceUVCreator
{
    /**
     * The minimum U coordinate.
     */
    var minU: Float = 0f
    /**
     * The maximum U coordinate.
     */
    var maxU: Float = 1f

    /**
     * The minimum V coordinate.
     */
    var minV: Float = 0f
    /**
     * The maximum V coordinate.
     */
    var maxV: Float = 1f

    /**
     * Creates the face UV mapping.
     *
     * For internal use only.
     *
     * @return The created face UV mapping.
     */
    operator fun invoke(): FaceUV =
        FaceUV(this.minU, this.maxU, this.minV, this.maxV)
}

/**
 * Creates a box UV mapping using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val boxUV = boxUV {
 *     face {
 *         minU = 0.25f
 *         maxU = 0.75f
 *     }
 * }
 * ```
 *
 * @param create The lambda function to create the box UV mapping.
 * @return The created box UV mapping.
 */
@UvDSL
fun boxUV(create: BoxUVCreator.() -> Unit): BoxUV
{
    val boxUVCreator = BoxUVCreator()
    boxUVCreator.create()
    return boxUVCreator()
}

/**
 * DSL creator for box UV mappings.
 *
 * @constructor Creates a new box UV creator.
 */
@UvDSL
class BoxUVCreator
{
    private var faceCreator = FaceUVCreator()
    private var backCreator = FaceUVCreator()
    private var topCreator = FaceUVCreator()
    private var bottomCreator = FaceUVCreator()
    private var leftCreator = FaceUVCreator()
    private var rightCreator = FaceUVCreator()

    /**
     * Defines the UV mapping for the front face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun face(create: FaceUVCreator.() -> Unit)
    {
        this.faceCreator.create()
    }

    /**
     * Defines the UV mapping for the back face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun back(create: FaceUVCreator.() -> Unit)
    {
        this.backCreator.create()
    }

    /**
     * Defines the UV mapping for the top face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun top(create: FaceUVCreator.() -> Unit)
    {
        this.topCreator.create()
    }

    /**
     * Defines the UV mapping for the bottom face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun bottom(create: FaceUVCreator.() -> Unit)
    {
        this.bottomCreator.create()
    }

    /**
     * Defines the UV mapping for the left face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun left(create: FaceUVCreator.() -> Unit)
    {
        this.leftCreator.create()
    }

    /**
     * Defines the UV mapping for the right face.
     *
     * @param create The lambda function to create the UV mapping.
     */
    fun right(create: FaceUVCreator.() -> Unit)
    {
        this.rightCreator.create()
    }

    /**
     * Creates the box UV mapping.
     *
     * For internal use only.
     *
     * @return The created box UV mapping.
     */
    internal operator fun invoke(): BoxUV =
        BoxUV(face = this.faceCreator(), back = this.backCreator(),
              top = this.topCreator(), bottom = this.bottomCreator(),
              left = this.leftCreator(), right = this.rightCreator())
}

/**
 * Creates a cross UV mapping using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val crossUV = crossUV {
 *     u1 = 0.4f
 *     u2 = 0.6f
 * }
 * ```
 *
 * @param create The lambda function to create the cross UV mapping.
 * @return The created cross UV mapping.
 */
@UvDSL
fun crossUV(create: CrossUVCreator.() -> Unit): CrossUV
{
    val crossUVCreator = CrossUVCreator()
    crossUVCreator.create()
    return crossUVCreator()
}

/**
 * DSL creator for cross UV mappings.
 *
 * @constructor Creates a new cross UV creator.
 */
@UvDSL
class CrossUVCreator
{
    /**
     * The first U coordinate.
     */
    var u1: Float = 1f / 3f
    /**
     * The second U coordinate.
     */
    var u2: Float = 2f / 3f

    /**
     * The first V coordinate.
     */
    var v1: Float = 0.25f
    /**
     * The second V coordinate.
     */
    var v2: Float = 0.5f
    /**
     * The third V coordinate.
     */
    var v3: Float = 0.75f

    /**
     * Creates the cross UV mapping.
     *
     * For internal use only.
     *
     * @return The created cross UV mapping.
     */
    internal operator fun invoke(): CrossUV =
        CrossUV(u1 = this.u1, u2 = this.u2,
                v1 = this.v1, v2 = this.v2, v3 = this.v3)
}
