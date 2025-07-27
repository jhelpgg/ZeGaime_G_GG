package fr.khelp.zegaime.database.query

import fr.khelp.zegaime.database.Table
import fr.khelp.zegaime.database.WhereDatabaseObjectDSL
import fr.khelp.zegaime.database.condition.Condition
import fr.khelp.zegaime.database.databaseobject.DatabaseObject
import fr.khelp.zegaime.database.type.DataDate
import fr.khelp.zegaime.database.type.DataTime
import fr.khelp.zegaime.utils.extensions.transformInt
import fr.khelp.zegaime.utils.regex.RegularExpression
import java.util.Calendar
import java.util.regex.Pattern
import kotlin.reflect.KProperty1

/**
 * Represents a `WHERE` clause in a query for a database object.
 *
 * This class provides methods to create conditions for the `WHERE` clause using property references.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is used in the `where` block of a `DatabaseObject.select` or `DatabaseObject.delete` query.
 *
 * **Standard usage:**
 * ```kotlin
 * where {
 *     condition = User::name EQUALS "John" AND User::age GREATER_THAN 18
 * }
 * ```
 *
 * @param DO The type of the database object.
 * @constructor Creates a new where clause for a database object. For internal use only.
 */
@WhereDatabaseObjectDSL
class WhereDatabaseObject<DO : DatabaseObject>(table: Table) : Where(table)
{
    /**
     * Creates a condition that checks if the property's value is equal to the given ID.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS_ID(id: Int): Condition =
        name EQUALS_ID id

    /**
     * Creates a condition that checks if the property's value is equal to the given boolean.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.EQUALS(value: Boolean): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.EQUALS(value: Byte): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.EQUALS(value: Short): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.EQUALS(value: Int): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.EQUALS(value: Long): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.EQUALS(value: Float): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.EQUALS(value: Double): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.EQUALS(value: String): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.EQUALS(value: Calendar): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.EQUALS(value: DataTime): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.EQUALS(value: DataDate): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given byte array.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.EQUALS(value: ByteArray): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given integer array.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, IntArray>.EQUALS(value: IntArray): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given enum.
     */
    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.EQUALS(value: E): Condition =
        name EQUALS value

    /**
     * Creates a condition that checks if the property's value is equal to the given database object.
     */
    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.EQUALS(value: DO2): Condition =
        name EQUALS value.databaseID

    //

    /**
     * Creates a condition that checks if the property's value is not equal to the given ID.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS_ID(id: Int): Condition =
        name NOT_EQUALS_ID id

    /**
     * Creates a condition that checks if the property's value is not equal to the given boolean.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.NOT_EQUALS(value: Boolean): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.NOT_EQUALS(value: Byte): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.NOT_EQUALS(value: Short): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.NOT_EQUALS(value: Int): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.NOT_EQUALS(value: Long): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.NOT_EQUALS(value: Float): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.NOT_EQUALS(value: Double): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.NOT_EQUALS(value: String): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.NOT_EQUALS(value: Calendar): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.NOT_EQUALS(value: DataTime): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.NOT_EQUALS(value: DataDate): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given byte array.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.NOT_EQUALS(value: ByteArray): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given integer array.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, IntArray>.NOT_EQUALS(value: IntArray): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given enum.
     */
    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.NOT_EQUALS(value: E): Condition =
        name NOT_EQUALS value

    /**
     * Creates a condition that checks if the property's value is not equal to the given database object.
     */
    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.NOT_EQUALS(value: DO2): Condition =
        name NOT_EQUALS value.databaseID

    //

    /**
     * Creates a condition that checks if the property's value is one of the given IDs.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF_ID(id: IntArray): Condition =
        name ONE_OF_ID id

    /**
     * Creates a condition that checks if the property's value is one of the given booleans.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Boolean>.ONE_OF(value: BooleanArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given bytes.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.ONE_OF(value: ByteArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given shorts.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.ONE_OF(value: ShortArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given integers.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.ONE_OF(value: IntArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given longs.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.ONE_OF(value: LongArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given floats.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.ONE_OF(value: FloatArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given doubles.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.ONE_OF(value: DoubleArray): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given strings.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.ONE_OF(value: Array<String>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given calendars.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.ONE_OF(value: Array<Calendar>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given times.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.ONE_OF(value: Array<DataTime>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given dates.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.ONE_OF(value: Array<DataDate>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given byte arrays.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, ByteArray>.ONE_OF(value: Array<ByteArray>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given integer arrays.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, IntArray>.ONE_OF(value: Array<IntArray>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given enums.
     */
    @WhereDatabaseObjectDSL
    infix fun <E : Enum<E>> KProperty1<DO, E>.ONE_OF(value: Array<E>): Condition =
        name ONE_OF value

    /**
     * Creates a condition that checks if the property's value is one of the given database objects.
     */
    @WhereDatabaseObjectDSL
    infix fun <DO2 : DatabaseObject> KProperty1<DO, DO2>.ONE_OF(value: Array<DO2>): Condition =
        name ONE_OF value.transformInt { it.databaseID }

    //

    /**
     * Creates a condition that checks if the property's value is lower than the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER(value: Byte): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER(value: Short): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER(value: Int): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER(value: Long): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER(value: Float): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER(value: Double): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER(value: String): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER(value: Calendar): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER(value: DataTime): Condition =
        name LOWER value

    /**
     * Creates a condition that checks if the property's value is lower than the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER(value: DataDate): Condition =
        name LOWER value

    //

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.LOWER_EQUALS(value: Byte): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.LOWER_EQUALS(value: Short): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.LOWER_EQUALS(value: Int): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.LOWER_EQUALS(value: Long): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.LOWER_EQUALS(value: Float): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.LOWER_EQUALS(value: Double): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.LOWER_EQUALS(value: String): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.LOWER_EQUALS(value: Calendar): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.LOWER_EQUALS(value: DataTime): Condition =
        name LOWER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is lower than or equal to the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.LOWER_EQUALS(value: DataDate): Condition =
        name LOWER_EQUALS value

    //

    /**
     * Creates a condition that checks if the property's value is upper than the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER(value: Byte): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER(value: Short): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER(value: Int): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER(value: Long): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER(value: Float): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER(value: Double): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER(value: String): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER(value: Calendar): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER(value: DataTime): Condition =
        name UPPER value

    /**
     * Creates a condition that checks if the property's value is upper than the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER(value: DataDate): Condition =
        name UPPER value

    //

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given byte.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Byte>.UPPER_EQUALS(value: Byte): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given short.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Short>.UPPER_EQUALS(value: Short): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given integer.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Int>.UPPER_EQUALS(value: Int): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given long.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Long>.UPPER_EQUALS(value: Long): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given float.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Float>.UPPER_EQUALS(value: Float): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given double.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Double>.UPPER_EQUALS(value: Double): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given string.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.UPPER_EQUALS(value: String): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given calendar.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, Calendar>.UPPER_EQUALS(value: Calendar): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given time.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataTime>.UPPER_EQUALS(value: DataTime): Condition =
        name UPPER_EQUALS value

    /**
     * Creates a condition that checks if the property's value is upper than or equal to the given date.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, DataDate>.UPPER_EQUALS(value: DataDate): Condition =
        name UPPER_EQUALS value

    //

    /**
     * Creates a condition that checks if the property's value is in the result of a subquery.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, *>.IN(matchCreator: Match.() -> Unit): Condition =
        name IN matchCreator

    //

    /**
     * Creates a condition that checks if the property's value matches the given regular expression pattern.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(pattern: Pattern): Condition =
        name REGEX pattern

    /**
     * Creates a condition that checks if the property's value matches the given regular expression.
     */
    @WhereDatabaseObjectDSL
    infix fun KProperty1<DO, String>.REGEX(regularExpression: RegularExpression): Condition =
        name REGEX regularExpression
}