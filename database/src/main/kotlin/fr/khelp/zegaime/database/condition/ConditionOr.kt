package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.utils.extensions.merge

/**
 * Creates a new condition that is the logical OR of this condition and the given condition.
 *
 * The resulting condition is valid if at least one of this condition and the given condition is valid.
 *
 * **Usage example**
 * ```kotlin
 * // Create a condition to select users with name "test" OR age greater than 18
 * val condition = (COLUMN_NAME EQUALS "test") OR (COLUMN_AGE GREATER_THAN 18)
 * ```
 *
 * @param condition The condition to be combined with this condition.
 * @return A new condition that is the logical OR of this condition and the given condition.
 */
infix fun Condition.OR(condition : Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) OR (${condition.sqlCondition})")