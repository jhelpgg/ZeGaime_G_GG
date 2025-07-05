package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.COLUMN_ID
import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.Table

class Condition internal constructor(internal val columns : Array<Column>, internal val sqlCondition : String)
{
    fun checkAllColumns(table : Table)
    {
        for (column in this.columns)
        {
            table.checkColumn(column)
        }
    }
}

val NEVER_MATCH_CONDITION = COLUMN_ID EQUALS_ID -123

