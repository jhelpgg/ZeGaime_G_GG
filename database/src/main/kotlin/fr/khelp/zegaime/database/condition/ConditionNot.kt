package fr.khelp.zegaime.database.condition

/**
 * Condition that reverse given condition
 */
fun not(condition: Condition) =
    Condition(condition.columns, "NOT(${condition.sqlCondition})")
