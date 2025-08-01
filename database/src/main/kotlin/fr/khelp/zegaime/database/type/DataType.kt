package fr.khelp.zegaime.database.type

import java.util.Calendar

enum class DataType(internal val typeSQL : String, internal val defaultValueSerialized : String)
{
    ID("INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY", ""),
    STRING("VARCHAR(8192)", "''"),
    BOOLEAN("BOOLEAN", "FALSE"),
    BYTE("TINYINT", "0"),
    SHORT("SMALLINT", "0"),
    INTEGER("INTEGER", "0"),
    LONG("BIGINT", "0"),
    FLOAT("DOUBLE", "0"),
    DOUBLE("DOUBLE", "0"),
    BYTE_ARRAY("VARCHAR(16384)", "''"),
    INT_ARRAY("VARCHAR(16384)", "''"),
    CALENDAR("BIGINT", "0"),
    DATE("INTEGER", "0"),
    TIME("INTEGER", "0"),
    ENUM("VARCHAR(1024)", "")

    ;

    fun compatible(type : DataType) =
        this == type || ((this == DataType.ID || this == DataType.INTEGER) && (type == DataType.ID || type == DataType.INTEGER))
}

fun Class<*>.toDataType() : DataType =
    when (this)
    {
        String::class.java    -> DataType.STRING
        Boolean::class.java   -> DataType.BOOLEAN
        Byte::class.java      -> DataType.BYTE
        Short::class.java     -> DataType.SHORT
        Int::class.java       -> DataType.INTEGER
        Long::class.java      -> DataType.LONG
        Float::class.java     -> DataType.FLOAT
        Double::class.java    -> DataType.DOUBLE
        ByteArray::class.java -> DataType.BYTE_ARRAY
        IntArray::class.java  -> DataType.INT_ARRAY
        Calendar::class.java  -> DataType.CALENDAR
        DataDate::class.java  -> DataType.DATE
        DataTime::class.java  -> DataType.TIME
        else                  ->
            if (this.isEnum)
            {
                DataType.ENUM
            }
            else
            {
                throw IllegalArgumentException("${this.name} have no corresponding data type")
            }
    }