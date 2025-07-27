package fr.khelp.zegaime.images.font

import java.awt.Dimension

/**
 * Represents a paragraph of text, which is a list of [TextLine]s.
 *
 * **Creation example**
 * This class is not meant to be instantiated directly.
 * It is returned by the `JHelpFont.computeTextParagraph` method.
 *
 * **Standard usage**
 * ```kotlin
 * val textParagraph = font.computeTextParagraph("Hello, world!", TextAlignment.CENTER, 100)
 * val lines = textParagraph.textLines
 * val size = textParagraph.size
 * ```
 *
 * @property textLines The list of text lines.
 * @property size The size of the paragraph.
 * @property string The string representation of the paragraph.
 * @constructor Creates a new text paragraph.
 */
class TextParagraph(val textLines : List<TextLine>, val size : Dimension)
{
    /**
     * The string representation of the paragraph.
     */
    val string : String by lazy {
        val stringBuilder = StringBuilder()
        var notFirst = false

        for (line in this.textLines)
        {
            if (notFirst)
            {
                stringBuilder.append('\n')
            }

            stringBuilder.append(line.text)
            notFirst = true
        }

        stringBuilder.toString()
    }
}