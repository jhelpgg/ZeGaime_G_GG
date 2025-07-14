package fr.khelp.zegaime.database.databaseobject

/**
 * Annotation to mark a field as a primary key.
 *
 * A primary key is a field that uniquely identifies a record in a table.
 * A table can have multiple primary keys.
 *
 * **Usage example:**
 * ```kotlin
 * @TableName("users")
 * class User(database: Database, @PrimaryKey val name: String, val age: Int) : DatabaseObject(database)
 * ```
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PrimaryKey
