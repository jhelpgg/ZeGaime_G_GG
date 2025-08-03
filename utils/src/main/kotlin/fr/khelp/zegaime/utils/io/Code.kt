package fr.khelp.zegaime.utils.io

import java.util.Stack

object Code
{
    private val codes =
        mapOf<Char, CharArray>(
            '0' to charArrayOf('O', 'Q'),
            '1' to charArrayOf('I', 'J', 'L'),
            '2' to charArrayOf('N', 'Z'),
            '3' to charArrayOf('M', 'W'),
            '4' to charArrayOf('H'),
            '5' to charArrayOf('S'),
            '6' to charArrayOf('U', 'V'),
            '7' to charArrayOf('T'),
            '8' to charArrayOf('K', 'R', 'X'),
            '9' to charArrayOf('G', 'P', 'Y'),
            'A' to charArrayOf('A'),
            'B' to charArrayOf('B'),
            'C' to charArrayOf('C'),
            'D' to charArrayOf('D'),
            'E' to charArrayOf('E'),
            'F' to charArrayOf('F'),
            // separations
            ' ' to charArrayOf(' '),
            '\n' to charArrayOf('\n'),
            '\r' to charArrayOf('\r'),
            '\t' to charArrayOf('\t'),
            ',' to charArrayOf(','),
            ';' to charArrayOf(';'),
            '.' to charArrayOf('.'),
            '!' to charArrayOf('!'),
            '?' to charArrayOf('?')
                              )

    private val encodes =
        mapOf<Char, Char>(
            'A' to 'A',
            'B' to 'B',
            'C' to 'C',
            'D' to 'D',
            'E' to 'E',
            'F' to 'F',
            'G' to '9',
            'H' to '4',
            'I' to '1',
            'J' to '1',
            'K' to '8',
            'L' to '1',
            'M' to '3',
            'N' to '2',
            'O' to '0',
            'P' to '9',
            'Q' to '0',
            'R' to '8',
            'S' to '5',
            'T' to '7',
            'U' to '6',
            'V' to '6',
            'W' to '3',
            'X' to '8',
            'Y' to '9',
            'Z' to '2',
            // separations
            ' ' to ' ',
            '\n' to '\n',
            '\r' to '\r',
            '\t' to '\t',
            ',' to ',',
            ';' to ';',
            '.' to '.',
            '!' to '!',
            '?' to '?'
                         )

    fun cipher(word : String) : String
    {
        if (word.isEmpty())
        {
            return ""
        }

        val characters = word.uppercase().toCharArray()
        return String(CharArray(characters.size) { index -> encodes.getOrDefault(characters[index], '?') })
    }

    fun decipher(code : String, collector : (String) -> Unit)
    {
        if (code.isEmpty())
        {
            collector("")
            return
        }

        val empty = CharArray(0)
        val codeChars = code.uppercase().toCharArray()
        val size = codeChars.size
        val responseChars = CharArray(size)
        // (response index, possible choices, choice index)
        val stack = Stack<Triple<Int, CharArray, Int>>()
        stack.push(Triple(0, codes.getOrDefault(codeChars[0], empty), 0))

        while (stack.isNotEmpty())
        {
            val (responseIndex, choices, choiceIndex) = stack.pop()

            if (choices.isEmpty())
            {
                responseChars[responseIndex] = '?'
            }
            else
            {
                responseChars[responseIndex] = choices[choiceIndex]

                if (choiceIndex + 1 < choices.size)
                {
                    stack.push(Triple(responseIndex, choices, choiceIndex + 1))
                }

                if (responseIndex + 1 == size)
                {
                    collector(String(responseChars))
                }
                else
                {
                    stack.push(Triple(responseIndex + 1,
                                      codes.getOrDefault(codeChars[responseIndex + 1], empty),
                                      0))
                }
            }
        }
    }
}