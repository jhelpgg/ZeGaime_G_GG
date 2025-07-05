package fr.khelp.zegaime.utils.regex

import fr.khelp.zegaime.utils.extensions.allCharactersExcludeThose
import fr.khelp.zegaime.utils.extensions.oneOrMore
import fr.khelp.zegaime.utils.extensions.plus
import fr.khelp.zegaime.utils.extensions.regularExpression
import fr.khelp.zegaime.utils.texts.digitInterval
import fr.khelp.zegaime.utils.texts.letterInterval
import fr.khelp.zegaime.utils.texts.letterOrDigitInterval
import fr.khelp.zegaime.utils.texts.letterOrDigitUnderscoreInterval
import fr.khelp.zegaime.utils.texts.lowerCaseInterval
import fr.khelp.zegaime.utils.texts.upperCaseInterval

/**
 * Any charachter except line return
 */
val ANY : RegularExpression get() = RegularExpression(".")

/**
 * Indicate we are at the start of the text
 */
val START_EXPRESSION : RegularExpression get() = RegularExpression("^")

/**
 * Indicate we are at the end of the text
 */
val END_EXPRESSION : RegularExpression get() = RegularExpression("$")

/**
 * Any white space (space, tabulation, line feed, line return)
 */
val WHITE_SPACE : RegularExpression get() = RegularExpression("\\s")

/**
 * Any character except white space
 */
val NOT_WHITE_SPACE : RegularExpression get() = RegularExpression("\\S")

/**
 * Letter lower case character
 */
val LOWER_CASE : RegularExpression get() = lowerCaseInterval.regularExpression

/**
 * Letter upper case character
 */
val UPPER_CASE : RegularExpression get() = upperCaseInterval.regularExpression

/**
 * Letter lower or upper case character
 */
val LETTER : RegularExpression get() = letterInterval.regularExpression

/**
 * All characters except lower or upper case character
 */
val NOT_LETTER : RegularExpression get() = letterInterval.allCharactersExcludeThose

/**
 * Digit character
 */
val DIGIT : RegularExpression get() = digitInterval.regularExpression

/**
 * Any character except digit
 */
val NOT_DIGIT : RegularExpression get() = digitInterval.allCharactersExcludeThose

/**
 * Letter or digit character
 */
val LETTER_OR_DIGIT : RegularExpression get() = letterOrDigitInterval.regularExpression

/**
 * Any String composed of letters only
 */
val WORD : RegularExpression get() = LETTER.oneOrMore()

/**
 * Any string composed of letters (with accent or not, latin or not), symbols (underscore, punctuation mark, operation), digits
 */
val WORD_EXTEND : RegularExpression get() = RegularExpression("\\w").oneOrMore()

/**
 * Any string start with letter follow by letters, digits, underscores
 */
val NAME : RegularExpression get() = LETTER + letterOrDigitUnderscoreInterval.zeroOrMore()

/**
 * Any email address
 */
val EMAIL : RegularExpression
    get() =
        (letterOrDigitInterval + '+' + '.' + '_' + '%' + '-').between(1, 256) +
        '@' +
        LETTER_OR_DIGIT + (letterOrDigitInterval + '-').atMost(64) +
        ('.' + LETTER_OR_DIGIT + (letterOrDigitInterval + '-').atMost(25)).oneOrMore()

/**
 * Any string look like an integer
 */
val INTEGER : RegularExpression get() = DIGIT.oneOrMore()

/**
 * Any string look like a real
 */
val REAL : RegularExpression get() = INTEGER + ('.' + INTEGER).zeroOrOne()

/**
 * Separator used in locale string representation
 */
val LOCALE_SEPARATOR : RegularExpression get() = charArrayOf('-', '_').regularExpression
