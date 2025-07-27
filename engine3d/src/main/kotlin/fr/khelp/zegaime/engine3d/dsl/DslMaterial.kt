package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Material

/**
 * Creates a material using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * val material = material {
 *     colorDiffuse = RED
 *     colorSpecular = WHITE
 * }
 * ```
 *
 * @param create The lambda function to create the material.
 * @return The created material.
 */
@MaterialDSL
fun material(create : Material.() -> Unit) : Material
{
    val material = Material()
    material.create()
    return material
}

/**
 * Edits a material using the DSL.
 *
 * **Usage example**
 * ```kotlin
 * myMaterial.edit {
 *     colorDiffuse = BLUE
 * }
 * ```
 *
 * @param edit The lambda function to edit the material.
 */
@MaterialDSL
fun Material.edit(edit : Material.() -> Unit)
{
    this.edit()
}
