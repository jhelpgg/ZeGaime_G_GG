package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.Column

/**
 * Throws when tries insert value and no value given for a column and its type not have default value
 */
class NoValueDefinedAndNoDefaultValue(column : Column) :
    Exception("No value defined for ${column.name} and no default value exists for ${column.type}")
