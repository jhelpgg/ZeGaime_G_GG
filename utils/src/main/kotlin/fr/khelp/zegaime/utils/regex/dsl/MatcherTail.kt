package fr.khelp.zegaime.utils.regex.dsl

/**
 * DSL for `forEachMatch` tail part
 * @param tail The tail part
 * @see fr.khelp.zegaime.utils.regex.ResultMatcher.forEachMatch
 */
@TailDSL
class MatcherTail internal constructor(val tail: String)
{
    /** The replacement for the tail */
    internal val toAppend = StringBuilder()

    /**
     * Append text to the replacement
     */
    @TailDSL
    operator fun String.unaryPlus()
    {
        this@MatcherTail.toAppend.append(this)
    }
}