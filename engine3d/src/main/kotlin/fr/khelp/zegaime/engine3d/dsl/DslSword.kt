package fr.khelp.zegaime.engine3d.dsl

import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.Sword
import fr.khelp.zegaime.engine3d.utils.NodePosition
import fr.khelp.zegaime.engine3d.utils.position

@PrebuiltDSL
fun sword(swordID : String, create : SwordCreator.() -> Unit) : Sword
{
    val swordCreator = SwordCreator(swordID)
    swordCreator.create()
    return swordCreator()
}

class SwordCreator(private val swordID : String)
{
    var size : Float = 3.3f
        set(value)
        {
            field = value.coerceIn(2f, 5f)
        }

    var position = NodePosition()

    internal operator fun invoke() : Sword
    {
        val sword = Sword(this.swordID, this.size)
        sword.position(this.position)
        return sword
    }
}