package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.render.Color4f
import fr.khelp.zegaime.engine3d.render.GRAY
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Dice
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position
import fr.khelp.zegaime.utils.math.random

@PrebuiltDSL
fun dice(diceID : String, create : DiceCreator.() -> Unit) : Dice
{
    val diceCreator = DiceCreator(diceID)
    diceCreator.create()
    return diceCreator()
}

@PrebuiltDSL
class DiceCreator(private val diceID : String)
{
    var value : Int = random(1, 6)
        set(value)
        {
            field = value.coerceIn(1, 6)
        }

    var color : Color4f = GRAY

    var position : NodePosition = NodePosition()

    internal operator fun invoke() : Dice
    {
        val dice = Dice(this.diceID, this.value)
        dice.position(this.position)
        dice.color(this.color)
        return dice
    }
}