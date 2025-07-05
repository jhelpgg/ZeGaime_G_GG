package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.type.DataType

infix fun Column.LIKE(pattern : String) : Condition
{
    this.checkType(DataType.STRING)
    return Condition(arrayOf(this), "${this.name} LIKE '$pattern'")
}

