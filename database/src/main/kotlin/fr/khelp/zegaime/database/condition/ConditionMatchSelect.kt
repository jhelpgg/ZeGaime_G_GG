package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.database.Column
import fr.khelp.zegaime.database.MatchDSL
import fr.khelp.zegaime.database.query.Match
import fr.khelp.zegaime.utils.argumentCheck

/**
 * Creates a condition that checks if the column's value is present in the result of a subquery.
 *
 * This is equivalent to the `IN` operator in SQL.
 *
 * **Usage example:**
 * ```kotlin
 * val condition = COLUMN_ID IN {
 *     select(otherTable, listOf(COLUMN_ID)) {
 *         where { COLUMN_NAME EQUALS "test" }
 *     }
 * }
 * ```
 *
 * @param matchCreator A lambda function to create the subquery.
 * @return A new condition.
 * @throws IllegalStateException if the subquery does not select anything.
 * @throws IllegalArgumentException if the subquery selects more than one column.
 * @throws IllegalArgumentException if the column's data type is not compatible with the subquery's result type.
 */
@MatchDSL
infix fun Column.IN(matchCreator : Match.() -> Unit) : Condition
{
    val match = Match()
    matchCreator(match)
    val select = match.select ?: throw IllegalStateException("Must select something")
    argumentCheck(select.numberColumns == 1) { "Select must return one and only one column" }
    val selectType = select[0].type
    argumentCheck(this.type.compatible(selectType)) { "Column with data type ${this.type} not compatible with return select type $selectType" }
    return Condition(arrayOf(this), "${this.name} IN (${select.selectSQL()})")
}
