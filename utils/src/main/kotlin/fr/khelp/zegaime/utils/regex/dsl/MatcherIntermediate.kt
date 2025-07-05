package fr.khelp.zegaime.utils.regex.dsl

@IntermediateDSL
class MatcherIntermediate internal constructor(val intermediate: String)
{
    internal val toAppend = StringBuilder()

    @IntermediateDSL
    operator fun String.unaryPlus()
    {
        this@MatcherIntermediate.toAppend.append(this)
    }
}