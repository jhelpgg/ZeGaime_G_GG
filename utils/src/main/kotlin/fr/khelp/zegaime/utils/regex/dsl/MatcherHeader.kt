package fr.khelp.zegaime.utils.regex.dsl

/**
 * DSL for `forEachMatch` header part
 * @param header The header part
 * @see fr.khelp.zegaime.utils.regex.ResultMatcher.forEachMatch
 */
@HeaderDSL
class MatcherHeader internal constructor(val header : String)
{
    /** The replacement for the header */
    internal val toAppend = StringBuilder()

    /**
     * Append text to the replacement
     */
    @HeaderDSL
    operator fun String.unaryPlus()
    {
        this@MatcherHeader.toAppend.append(this)
    }
}