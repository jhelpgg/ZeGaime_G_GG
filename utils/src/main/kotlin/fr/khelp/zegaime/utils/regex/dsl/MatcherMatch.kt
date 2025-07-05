package fr.khelp.zegaime.utils.regex.dsl

import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup
import fr.khelp.zegaime.utils.regex.Replacement
import fr.khelp.zegaime.utils.regex.ResultMatcher

@MatchDSL
class MatcherMatch internal constructor(private val regularExpression : RegularExpression,
                                        private val matcher : ResultMatcher,
                                        val match : String)
{
    internal var replacement : Replacement? = null

    @MatchDSL
    fun value(regularExpressionGroup : RegularExpressionGroup) : String =
        this.matcher.group(regularExpressionGroup)

    @ReplacementDSL
    fun replace(replacementBuilder : (MatcherMatch) -> MatcherReplacement.() -> Unit)
    {
        this.replacement = this.regularExpression.replacement(replacementBuilder(this))
    }
}