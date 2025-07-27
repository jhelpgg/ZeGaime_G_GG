package fr.khelp.zegaime.database

/**
 * DSL marker for creating a table.
 *
 */
@DslMarker
annotation class CreateTableDSL()

/**
 * DSL marker for deleting rows.
 *
 */
@DslMarker
annotation class DeleteSL()

/**
 * DSL marker for inserting rows.
 *
 */
@DslMarker
annotation class InsertDSL()

/**
 * DSL marker for selecting rows.
 *
 */
@DslMarker
annotation class SelectDSL()

/**
 * DSL marker for updating rows.
 *
 */
@DslMarker
annotation class UpdateDSL()

/**
 * DSL marker for processing a row result.
 *
 */
@DslMarker
annotation class RowResultDSL()

/**
 * DSL marker for matching a select query.
 *
 */
@DslMarker
annotation class MatchDSL

/**
 * DSL marker for a where clause.
 *
 */
@DslMarker
annotation class WhereDSL

/**
 * DSL marker for a where clause on a database object.
 *
 */
@DslMarker
annotation class WhereDatabaseObjectDSL


