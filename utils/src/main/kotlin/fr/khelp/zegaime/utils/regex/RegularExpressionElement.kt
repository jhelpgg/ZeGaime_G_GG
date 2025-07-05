package fr.khelp.zegaime.utils.regex

abstract class RegularExpressionElement
{
   internal abstract fun regexString(resolveGroup:Boolean): String
}