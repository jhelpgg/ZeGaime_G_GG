package fr.khelp.zegaime.utils.regex.dsl

import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup
import fr.khelp.zegaime.utils.regex.Replacement
import fr.khelp.zegaime.utils.regex.ResultMatcher

/**
 * DSL for `forEachMatch` match part
 * @param regularExpression Regular expression used for matching
 * @param matcher Matcher that found the match
 * @param match The matched part
 * @see fr.khelp.zegaime.utils.regex.ResultMatcher.forEachMatch
 */
@MatchDSL
class MatcherMatch internal constructor(private val regularExpression : RegularExpression,
                                        private val matcher : ResultMatcher,
                                        val match : String)
{
    /** Replacement if any */
    internal var replacement : Replacement? = null

    /**
     * Get a captured group value
     * @param regularExpressionGroup Group to retrieve
     * @return The captured value
     */
    @MatchDSL
    fun value(regularExpressionGroup : RegularExpressionGroup) : String =
        this.matcher.group(regularExpressionGroup)

    /**
     * Define a replacement for the matched part
     * @param replacementBuilder Lambda for create the replacement
     */
    @ReplacementDSL
    fun replace(replacementBuilder : (MatcherMatch) -> MatcherReplacement.() -> Unit)
    {
        this.replacement = this.regularExpression.replacement(replacementBuilder(this))
    }
}