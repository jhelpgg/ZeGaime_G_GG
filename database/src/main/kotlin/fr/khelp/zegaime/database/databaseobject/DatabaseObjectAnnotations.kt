package fr.khelp.zegaime.database.databaseobject

/**
 * Annotation to mark a field as a primary key.
 *
 * A primary key is a field that uniquely identifies a record in a table.
 * A table can have multiple primary keys.
 *
 * If no primary key is defined, the database object will be updated based on its ID.
 *
 * **Usage example**
 * ```kotlin
 * class User(database: Database, @PrimaryKey val name: String, val age: Int) : DatabaseObject(database)
 * ```
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PrimaryKey