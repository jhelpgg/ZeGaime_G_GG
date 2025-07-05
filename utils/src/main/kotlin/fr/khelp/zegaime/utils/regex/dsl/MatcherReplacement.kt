package fr.khelp.zegaime.utils.regex.dsl

import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup

/**
 * DSL for creating replacement part in [RegularExpression.replacement] and [fr.khelp.zegaime.utils.regex.ResultMatcher.appendReplacement]
 * @param regularExpression Regular expression used for matching
 */
@ReplacementDSL
class MatcherReplacement internal constructor(private val regularExpression : RegularExpression)
{
    /** The result replacement */
    internal val replacement = StringBuilder()

    /**
     * Append a free text to the replacement
     */
    @ReplacementDSL
    operator fun String.unaryPlus()
    {
        this@MatcherReplacement.replacement.append(this)
    }

    /**
     * Append a captured group to the replacement
     */
    @ReplacementDSL
    operator fun RegularExpressionGroup.unaryPlus()
    {
        argumentCheck(this in this@MatcherReplacement.regularExpression) { "The given group is not linked to regular expression" }
        this@MatcherReplacement.replacement.append('$')
        this@MatcherReplacement.replacement.append(this.groupID)
    }
}