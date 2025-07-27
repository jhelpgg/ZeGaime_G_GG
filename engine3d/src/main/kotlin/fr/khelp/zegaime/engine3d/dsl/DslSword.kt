package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Sword
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

/**
 * Creates a sword using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val sword = sword("mySword") {
 *     size = 4f
 *     position = NodePosition(x = 1f)
 * }
 * ```
 *
 * @param swordID The ID of the sword.
 * @param create The lambda function to create the sword.
 * @return The created sword.
 */
@PrebuiltDSL
fun sword(swordID: String, create: SwordCreator.() -> Unit): Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    return swordCreator()
}

/**
 * DSL creator for swords.
 *
 * @property swordID The ID of the sword.
 * @constructor Creates a new sword creator.
 */
class SwordCreator(private val swordID: String)
{
    /**
     * The size of the sword.
     */
    var size: Float = 3.3f
        set(value)
        {
            field = value.coerceIn(2f, 5f)
        }

    /**
     * The position of the sword.
     */
    var position = NodePosition()

    /**
     * Creates the sword.
     *
     * For internal use only.
     *
     * @return The created sword.
     */
    internal operator fun invoke(): Sword
    {
        val sword = Sword(this.swordID, this.size)
        sword.position(this.position)
        return sword
    }
}
