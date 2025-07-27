package fr.khelp.zegaime.utils.regex.dsl

/**
 * DSL for `forEachMatch` intermediate part
 * @param intermediate The intermediate part
 * @see fr.khelp.zegaime.utils.regex.ResultMatcher.forEachMatch
 */
@IntermediateDSL
class MatcherIntermediate internal constructor(val intermediate : String)
{
    /** The replacement for the intermediate part */
    internal val toAppend = StringBuilder()

    /**
     * Append text to the replacement
     */
    @IntermediateDSL
    operator fun String.unaryPlus()
    {
        this@MatcherIntermediate.toAppend.append(this)
    }
}