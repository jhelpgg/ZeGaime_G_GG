package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.geometry.Mesh
import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.Material
import fr.khelp.zegaime.engine3d.render.TwoSidedRule
import fr.khelp.zegaime.engine3d.scene.Object3D
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Fills the mesh with the 3D object using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myObject3D.mesh {
 *     // ...
 * }
 * ```
 *
 * @param meshFiller The lambda function to fill the mesh.
 */
@MeshDSL
fun Object3D.mesh(meshFiller : Mesh.() -> Unit)
{
    this.mesh.meshFiller()
    this.refresh()
}

/**
 * Creates a 3D object using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val object3D = object3D("myObject") {
 *     position = NodePosition(x = 1f)
 *     mesh {
 *         // ...
 *     }
 * }
 * ```
 *
 * @param objectID The ID of the 3D object.
 * @param create The lambda function to create the 3D object.
 * @return The created 3D object.
 */
@PrebuiltDSL
fun object3D(objectID : String, create : Object3DCreator.() -> Unit) : Object3D
{
    val object3DCreator = Object3DCreator(objectID)
    object3DCreator.create()
    return object3DCreator.object3D
}

/**
 * DSL creator for 3D objects.
 *
 * @param objectID The ID of the 3D object.
 * @constructor Creates a new 3D object creator.
 */
@PrebuiltDSL
class Object3DCreator(objectID : String)
{
    /**
     * The created 3D object.
     */
    internal val object3D = Object3D(objectID)

    /**
     * The position of the 3D object.
     */
    var position : NodePosition
        get() = this.object3D.position
        set(value)
        {
            this.object3D.position(value)
        }

    /**
     * The material of the 3D object.
     */
    var material : Material
        get() = this.object3D.material
        set(value)
        {
            this.object3D.material = value
        }

    /**
     * The material of the 3D object when it is selected.
     */
    var materialForSelection : Material
        get() = this.object3D.materialForSelection
        set(value)
        {
            this.object3D.materialForSelection = value
        }

    /**
     * The color of the wireframe.
     */
    var wireColor : Color4f
        get() = this.object3D.wireColor
        set(value)
        {
            this.object3D.wireColor = value
        }

    /**
     * The two-sided rule for the 3D object.
     */
    var twoSidedRule : TwoSidedRule
        get() = this.object3D.twoSidedRule
        set(value)
        {
            this.object3D.twoSidedRule = value
        }

    /**
     * Creates the material for the 3D object.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialCreate(create : Material.() -> Unit)
    {
        this.material = material(create)
    }

    /**
     * Creates the material for the 3D object when it is selected.
     *
     * @param create The lambda function to create the material.
     */
    @MaterialDSL
    fun materialForSelectionCreate(create : Material.() -> Unit)
    {
        this.materialForSelection = material(create)
    }

    /**
     * Fills the mesh with the 3D object.
     *
     * @param meshFiller The lambda function to fill the mesh.
     */
    @MeshDSL
    fun mesh(meshFiller : Mesh.() -> Unit)
    {
        this.object3D.mesh(meshFiller)
    }
}
