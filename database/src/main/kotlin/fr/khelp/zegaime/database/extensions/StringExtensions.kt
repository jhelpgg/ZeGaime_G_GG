package fr.khelp.zegaime.database.extensions

import java.util.regex.Pattern

/**
 * Pattern for a valid table or column name.
 *
 * A valid name must start with a letter and can be followed by letters, numbers, or underscores.
 */
val NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*")

/**
 * Indicates if a string is a valid name for a table or a column.
 *
 * A valid name must start with a letter and can be followed by letters, numbers, or underscores.
 *
 * **Usage example:**
 * ```kotlin
 * val isValid = "my_table".validName() // true
 * val isNotValid = "1table".validName() // false
 * ```
 *
 * @return `true` if the string is a valid name, `false` otherwise.
 */
fun String.validName() =
    NAME_PATTERN.matcher(this)
        .matches()
