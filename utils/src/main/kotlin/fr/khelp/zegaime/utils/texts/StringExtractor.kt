package fr.khelp.zegaime.utils.texts

import java.util.StringTokenizer

/**
 * Cut string with separator, like [StringTokenizer], but in addition it can detect Strings and not cut on them,
 * it can also ignore escaped character.
 *
 * For example :
 *
 *     val extractor = StringExtractor("Hello world ! 'This is a phrase'")
 *     println(extractor.next())
 *     println(extractor.next())
 *     println(extractor.next())
 *     println(extractor.next())
 *
 *  It prints:
 *
 *      Hello
 *      world
 *      !
 *      This is a phrase
 *
 * @param string           String to parse
 * @param separators       Separators list
 * @param stringLimiters   String delimiters
 * @param escapeCharacters Escape characters
 * @param returnSeparators Indicates if return separators
 */
class StringExtractor(string : String,
                      separators : String = DEFAULT_SEPARATORS,
                      stringLimiters : String = DEFAULT_STRING_LIMITERS,
                      escapeCharacters : String = DEFAULT_ESCAPE_CHARACTERS,
                      private val returnSeparators : Boolean = false)
{
    /**
     * Indicates if empty string result is allowed
     */
    var isCanReturnEmptyString : Boolean = false

    /**
     * Current word start index
     */
    var currentWordStart : Int = 0
        private set

    /**
     * Current word end index in original text
     */
    val currentWordEnd get(): Int = this.index

    /**
     * Escape characters
     */
    private val escapeCharacters : CharArray

    /**
     * Current read index
     */
    private var index : Int = 0

    /**
     * String to parse length
     */
    private val length : Int

    /**
     * Open/close pairs, to consider like "normal" character something between an open and a close character
     */
    private val openCloseIgnore : ArrayList<OpenCloseDelimiter>

    /**
     * Separators characters
     */
    private val separators : CharArray

    /**
     * Indicates if have to stop parsing when meet "string" to treat them separately `true` OR treat them as a
     * part of
     * something : `false`
     */
    var isStopAtString : Boolean = false

    /**
     * String to parse
     */
    private val string : CharArray

    /**
     * String delimiters
     */
    private val stringLimiters : CharArray

    /**
     * Indicates if last extracted part is considered as a String (Because inside String delimiters)
     */
    var isString = false
        private set

    /**
     * Indicates if last extracted part is considered as separator
     */
    var isSeparator = false
        private set

    init
    {
        this.string = string.toCharArray()
        this.separators = separators.toCharArray()
        this.stringLimiters = stringLimiters.toCharArray()
        this.escapeCharacters = escapeCharacters.toCharArray()

        this.index = 0
        this.currentWordStart = -1
        this.length = string.length

        this.openCloseIgnore = ArrayList<OpenCloseDelimiter>()
        this.isCanReturnEmptyString = true
        this.isStopAtString = true
    }

    /**
     * Add a open close pairs, to consider like "normal" character something between an open and a close character
     *
     * It can specifies if have to count the number of open/close.
     *
     * If have to count:
     * * Each time open is meet counter is incremented.
     * * Each time close is meet counter is decremented
     * * When a close meet and counter reach zero, the "normal" is resolved
     *
     * Note:
     * > If close meet and counter at 0 (No open match), the close character is treat as other characters
     *
     * @param open  Open character
     * @param close Close character
     * @param countOpenClose Indicates if have to count open/close
     */
    fun addOpenCloseIgnore(open : Char, close : Char, countOpenClose : Boolean = false)
    {
        if (open == close)
        {
            throw IllegalArgumentException("Open and close can't have same value")
        }

        for ((first, second, _) in this.openCloseIgnore)
        {
            if (first == open || second == open || first == close || second == close)
            {
                throw IllegalArgumentException("Open or close is already used !")
            }
        }

        this.openCloseIgnore.add(OpenCloseDelimiter(open, close, countOpenClose))
    }

    /**
     * Next extracted string.

     * It can be a separator if you ask for return them.

     * It returns `null` if no more string to extract
     *
     * @return Next part or `null` if no more to extract
     */
    operator fun next() : String?
    {
        this.isString = false
        this.isSeparator = false
        this.currentWordStart = this.index

        if (this.index >= this.length)
        {
            return null
        }

        var insideString = false
        var start = this.index
        var end = this.length
        var currentStringLimiter = ' '
        var openClose : OpenCloseDelimiter? = null
        var character = this.string[this.index]

        do
        {
            if (openClose == null)
            {
                for (openClos in this.openCloseIgnore)
                {
                    if (openClos.open == character)
                    {
                        openClose = openClos
                        openClose.counter = 0
                        break
                    }
                }

                if (openClose != null)
                {
                    if (this.separators.contains(character))
                    {
                        if (start < this.index)
                        {
                            end = this.index

                            break
                        }
                    }
                }
            }

            if (openClose == null)
            {
                if (this.escapeCharacters.contains(character))
                {
                    this.index++
                }
                else if (insideString)
                {
                    if (currentStringLimiter == character)
                    {
                        insideString = false

                        if (this.isStopAtString)
                        {
                            end = this.index
                            this.index++
                            break
                        }
                    }
                }
                else if (this.stringLimiters.contains(character))
                {
                    if (start < this.index && this.isStopAtString)
                    {
                        end = this.index

                        break
                    }

                    if (this.isStopAtString)
                    {
                        start++
                    }

                    insideString = true
                    this.isString = true
                    currentStringLimiter = character
                }
                else if (this.separators.contains(character))
                {
                    if (start < this.index)
                    {
                        end = this.index

                        break
                    }

                    if (this.returnSeparators)
                    {
                        end = start + 1
                        this.index++
                        this.isSeparator = true

                        break
                    }

                    start++
                }
            }
            else if (character == openClose.open && openClose.countOpenClose)
            {
                openClose.counter++
            }
            else if (character == openClose.close)
            {
                openClose.counter--

                if (!openClose.countOpenClose || openClose.counter == 0)
                {
                    openClose = null
                }
            }

            this.index++

            if (this.index < this.length)
            {
                character = this.string[this.index]
            }
        }
        while (this.index < this.length)

        return if (!this.isCanReturnEmptyString && end == start)
        {
            this.next()
        }
        else String(this.string, start, end - start)
    }
}