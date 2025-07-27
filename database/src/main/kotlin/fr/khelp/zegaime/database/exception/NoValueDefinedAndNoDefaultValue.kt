package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Column

/**
 * Exception thrown when trying to insert a row without a value for a column that has no default value.
 *
 * This exception is thrown when an `INSERT` statement is executed without providing a value for a column
 * that does not have a default value.
 *
 * **Creation example**
 * ```kotlin
 * throw NoValueDefinedAndNoDefaultValue(column)
 * ```
 *
 * @param column The column for which no value was defined.
 * @constructor Creates a new no value defined and no default value exception.
 */
class NoValueDefinedAndNoDefaultValue(column : Column) :
    Exception("No value defined for ${column.name} and no default value exists for ${column.type}")