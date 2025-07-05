package fr.khelp.zegaime.utils.regex.dsl

import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.regex.RegularExpression
import fr.khelp.zegaime.utils.regex.RegularExpressionGroup

@ReplacementDSL
class MatcherReplacement internal constructor(private val regularExpression : RegularExpression)
{
    internal val replacement = StringBuilder()

    @ReplacementDSL
    operator fun String.unaryPlus()
    {
        this@MatcherReplacement.replacement.append(this)
    }

    @ReplacementDSL
    operator fun RegularExpressionGroup.unaryPlus()
    {
        argumentCheck(this in this@MatcherReplacement.regularExpression) { "The given group is not linked to regular expression" }
        this@MatcherReplacement.replacement.append('$')
        this@MatcherReplacement.replacement.append(this.groupID)
    }
}