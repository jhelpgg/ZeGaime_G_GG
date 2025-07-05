package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.utils.extensions.merge

/**
 * Condition valid if at least one of given condition is valid
 */
infix fun Condition.OR(condition : Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) OR (${condition.sqlCondition})")
