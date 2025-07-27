package fr.khelp.zegaime.utils.regex

/**
 * Parent of regular expression and group.
 *
 * For internal use
 */
abstract class RegularExpressionElement
{
    /**
     * String representation of the regular expression
     * @param resolveGroup Indicates if group ID must be resolved
     * @return String representation
     */
    internal abstract fun regexString(resolveGroup : Boolean) : String
}