package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.GRAY
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Dice
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position
import fr.khelp.zegaime.utils.math.random

/**
 * Creates a dice using the DSL.
 *
 * **Usage example:**
 * ```kotlin
 * val dice = dice("myDice") {
 *     value = 6
 *     color = RED
 *     position = NodePosition(x = 1f)
 * }
 * ```
 *
 * @param diceID The ID of the dice.
 * @param create The lambda function to create the dice.
 * @return The created dice.
 */
@PrebuiltDSL
fun dice(diceID: String, create: DiceCreator.() -> Unit): Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.create()
    return diceCreator()
}

/**
 * DSL creator for dices.
 *
 * @property diceID The ID of the dice.
 * @constructor Creates a new dice creator.
 */
@PrebuiltDSL
class DiceCreator(private val diceID: String)
{
    /**
     * The value of the dice (1-6).
     */
    var value: Int = random(1, 6)
        set(value)
        {
            field = value.coerceIn(1, 6)
        }

    /**
     * The color of the dice.
     */
    var color: Color4f = GRAY

    /**
     * The position of the dice.
     */
    var position: NodePosition = NodePosition()

    /**
     * Creates the dice.
     *
     * For internal use only.
     *
     * @return The created dice.
     */
    internal operator fun invoke(): Dice
    {
        val dice = Dice(this.diceID, this.value)
        dice.position(this.position)
        dice.color(this.color)
        return dice
    }
}
