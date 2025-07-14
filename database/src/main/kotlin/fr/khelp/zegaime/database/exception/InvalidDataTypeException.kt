package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.type.DataType

/**
 * Exception thrown when a data type is not the expected one.
 *
 * **Creation example:**
 * ```kotlin
 * throw InvalidDataTypeException(DataType.STRING, DataType.INTEGER)
 * ```
 *
 * @param invalidType The invalid data type.
 * @param expectedType The expected data type.
 */
class InvalidDataTypeException(invalidType : DataType, expectedType : DataType) :
    Exception("This method require data type $expectedType but used with $invalidType")

