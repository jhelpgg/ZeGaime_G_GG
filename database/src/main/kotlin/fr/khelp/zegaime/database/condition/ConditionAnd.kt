package fr.khelp.zegaime.database.condition

import fr.khelp.zegaime.utils.extensions.merge

/**
 * Creates a new condition that is the logical AND of this condition and the given condition.
 *
 * The resulting condition is valid if and only if both this condition and the given condition are valid.
 *
 * **Usage example**
 * ```kotlin
 * // Create a condition to select users with name "test" AND age greater than 18
 * val condition = (COLUMN_NAME EQUALS "test") AND (COLUMN_AGE GREATER_THAN 18)
 * ```
 *
 * @param condition The condition to be combined with this condition.
 * @return A new condition that is the logical AND of this condition and the given condition.
 */
infix fun Condition.AND(condition : Condition) =
    Condition(this.columns.merge(condition.columns), "(${this.sqlCondition}) AND (${condition.sqlCondition})")