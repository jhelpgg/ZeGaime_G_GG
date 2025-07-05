package fr.khelp.zegaime.database.exception

import fr.khelp.zegaime.database.type.DataType

/**
 * Throws if data not have expected type
 */
class InvalidDataTypeException(invalidType : DataType, expectedType : DataType) :
    Exception("This method require data type $expectedType but used with $invalidType")

