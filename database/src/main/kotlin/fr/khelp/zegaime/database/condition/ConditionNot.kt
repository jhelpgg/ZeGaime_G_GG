package fr.khelp.zegaime.database.condition

/**
 * Creates a new condition that is the logical NOT of the given condition.
 *
 * The resulting condition is valid if and only if the given condition is not valid.
 *
 * **Usage example**
 * ```kotlin
 * // Create a condition to select users with name not equal to "test"
 * val condition = not(COLUMN_NAME EQUALS "test")
 * ```
 *
 * @param condition The condition to be negated.
 * @return A new condition that is the logical NOT of the given condition.
 */
fun not(condition : Condition) =
    Condition(condition.columns, "NOT(${condition.sqlCondition})")