package fr.khelp.zegaime.images.font

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.TextAlignment
import fr.khelp.zegaime.utils.logs.exception
import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.parallel
import fr.khelp.zegaime.utils.texts.StringExtractor
import fr.khelp.zegaime.utils.texts.lastIndexOf
import fr.khelp.zegaime.utils.ui.FONT_RENDER_CONTEXT
import java.awt.Dimension
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Shape
import java.awt.font.GlyphVector
import java.io.InputStream
import java.util.Collections
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round

/**
 * Represents a font that can be used to draw text on an image.
 *
 * **Creation example:**
 * ```kotlin
 * val font = JHelpFont("Arial", 12, bold = true)
 * ```
 *
 * **Standard usage:**
 * ```kotlin
 * val image = GameImage(100, 100)
 * image.draw { graphics ->
 *     graphics.font = font.font
 *     graphics.drawString("Hello, world!", 10, 50)
 * }
 * ```
 *
 * @property font The underlying AWT font.
 * @property underline Indicates if the font is underlined.
 * @property fontHeight The height of the font.
 * @property ascent The ascent of the font.
 * @property size The size of the font.
 * @property bold Indicates if the font is bold.
 * @property italic Indicates if the font is italic.
 * @property family The family of the font.
 * @property maximumCharacterWidth The maximum width of a character in the font.
 */
class JHelpFont(val font: Font, val underline: Boolean = false)
{
    companion object
    {
        /**
         * Creates a font.
         *
         * @param family The font family.
         * @param size The font size.
         * @param bold Indicates if the font should be bold.
         * @param italic Indicates if the font should be italic.
         * @return The created font.
         */
        internal fun createFont(family: String, size: Int, bold: Boolean = false, italic: Boolean = false): Font
        {
            var style = Font.PLAIN

            if (bold)
            {
                style = style or Font.BOLD
            }

            if (italic)
            {
                style = style or Font.ITALIC
            }

            return Font(family, style, size)
        }

        /**
         * Creates a font from a stream.
         *
         * **Usage example:**
         * ```kotlin
         * val font = JHelpFont.createFont(FontType.TRUETYPE, stream, 12, FontValue.TRUE, FontValue.FALSE, false).waitAndGet()
         * ```
         *
         * @param type The type of the font.
         * @param stream The stream to get the font data from.
         * @param size The size of the created font.
         * @param bold The bold value.
         * @param italic The italic value.
         * @param underline Indicates if the font should be underlined.
         * @return A future that will contain the created font.
         */
        fun createFont(type: FontType, stream: InputStream, size: Int,
                       bold: FontValue, italic: FontValue, underline: Boolean): Future<JHelpFont> =
            obtainFont(type, stream, size, bold, italic, underline)
                .afterSucceed { font ->
                    if (font == DEFAULT_FONT)
                    {
                        val newSize = if (size < 1)
                        {
                            18
                        }
                        else
                        {
                            size
                        }

                        JHelpFont("Arial", newSize, bold == FontValue.TRUE, italic == FontValue.TRUE, underline)
                    }
                    else
                    {
                        font
                    }
                }

        /**
         * Obtains a font from a stream.
         *
         * **Usage example:**
         * ```kotlin
         * val font = JHelpFont.obtainFont(FontType.TRUETYPE, stream, 12).waitAndGet()
         * ```
         *
         * @param type The type of the font.
         * @param stream The stream to get the font data from.
         * @param size The size of the created font.
         * @param bold The bold value.
         * @param italic The italic value.
         * @param underline Indicates if the font should be underlined.
         * @return A future that will contain the created font, or `null` if the stream is not a managed font.
         */
        fun obtainFont(type: FontType, stream: InputStream, size: Int,
                       bold: FontValue = FontValue.AS_DEFINED, italic: FontValue = FontValue.AS_DEFINED,
                       underline: Boolean = false): Future<JHelpFont> =
            {
                try
                {
                    val fontFormat =
                        if (type == FontType.TYPE1)
                        {
                            Font.TYPE1_FONT
                        }
                        else
                        {
                            Font.TRUETYPE_FONT
                        }

                    var font = Font.createFont(fontFormat, stream)
                    val fontSize = font.size
                    val fontStyle = font.style

                    var style = 0

                    when (bold)
                    {
                        FontValue.FALSE -> Unit
                        FontValue.AS_DEFINED -> style = style or (fontStyle and Font.BOLD)
                        FontValue.TRUE -> style = style or Font.BOLD
                    }

                    when (italic)
                    {
                        FontValue.FALSE -> Unit
                        FontValue.AS_DEFINED -> style = style or (fontStyle and Font.ITALIC)
                        FontValue.TRUE -> style = style or Font.ITALIC
                    }

                    if (fontSize != size || style != fontStyle)
                    {
                        if (fontSize == size)
                        {
                            font = font.deriveFont(style)
                        }
                        else if (style == fontStyle)
                        {
                            font = font.deriveFont(size.toFloat())
                        }
                        else
                        {
                            font = font.deriveFont(style, size.toFloat())
                        }
                    }

                    JHelpFont(font, underline)
                }
                catch (exception: Exception)
                {
                    exception(exception, "Failed to create the font")
                    DEFAULT_FONT
                }
            }.parallel(TaskContext.UI)
    }

    /**Font measure metrics*/
    private lateinit var fontMetrics: FontMetrics
    /**
     * The height of the font.
     */
    val fontHeight: Int get() = this.fontMetrics.height
    /**
     * The ascent of the font.
     */
    val ascent: Int get() = this.fontMetrics.ascent

    /**
     * Font size
     */
    val size: Int = this.font.size

    /**
     * Indicates if font is bold
     *
     * @return `true` if font is bold
     */
    val bold: Boolean = this.font.style and Font.BOLD != 0

    /**
     * Indicates if font is italic
     *
     * @return `true` if font is italic
     */
    val italic: Boolean = this.font.style and Font.ITALIC != 0

    /**
     * The family of the font.
     */
    val family: String = this.font.family

    /**Maximum width of a character*/
    val maximumCharacterWidth: Int by lazy {
        var maximum = 0

        for (character in 32..127)
        {
            maximum = max(maximum, this.fontMetrics.charWidth(character))
        }

        maximum
    }

    init
    {
        GameImage.DUMMY.draw { graphics2D -> this@JHelpFont.fontMetrics = graphics2D.getFontMetrics(this.font) }
    }

    /**
     * Create font with detailed parameters
     * @param family Font family name
     * @param size Font size
     * @param bold Indicates if have to bold
     * @param italic Indicates if have to italic
     * @param underline Indicates if have to underline
     */
    constructor(family: String, size: Int, bold: Boolean = false, italic: Boolean = false,
                underline: Boolean = false) :
            this(createFont(family, size, bold, italic), underline)

    /**
     * Computes the text lines representation with this font with an alpha mask.
     *
     * **Usage example:**
     * ```kotlin
     * val textParagraph = font.computeTextParagraph("Hello, world!", TextAlignment.CENTER, 100)
     * ```
     *
     * @param text The text to use.
     * @param textAlign The alignment to use.
     * @param limitWidth The maximum number of pixels in width.
     * @param limitHeight The limit height in pixels.
     * @param trim Indicates if it has to trim lines.
     * @return A couple of the list of each computed line and the total size of all lines together.
     */
    fun computeTextParagraph(text: String, textAlign: TextAlignment,
                             limitWidth: Int = Int.MAX_VALUE, limitHeight: Int = Int.MAX_VALUE,
                             trim: Boolean = true): TextParagraph
    {
        val limit = max(this.maximumCharacterWidth + 2, limitWidth)

        val textLines = ArrayList<TextLine>()
        val lines = StringExtractor(text, "\n\r", "", "")
        val size = Dimension()

        var width: Int
        var index: Int
        var start: Int
        val height = this.fontHeight

        var line = lines.next()
        var head: String
        var tail: String

        while (line != null)
        {
            if (trim)
            {
                line = line.trim { it <= ' ' }
            }

            width = this.stringWidth(line)
            index = line.length - 1

            while (width > limit && index > 0)
            {
                start = index
                index = lastIndexOf(line!!, index, ' ', '\t', '\'', '&', '~', '"', '#', '{', '(', '[', '-', '|',
                                    '`', '_', '\\', '^', '@', '°', ')', ']',
                                    '+', '=', '}', '"', 'µ', '*', ',', '?', '.', ';', ':', '/', '!', '§', '<',
                                    '>', '²')

                if (index >= 0)
                {
                    if (trim)
                    {
                        head = line.substring(0, index)
                            .trim { it <= ' ' }
                        tail = line.substring(index)
                            .trim { it <= ' ' }
                    }
                    else
                    {
                        head = line.substring(0, index)
                        tail = line.substring(index)
                    }
                }
                else
                {
                    start--
                    index = start
                    head = line.substring(0, index) + "-"
                    tail = line.substring(index)
                }

                width = this.stringWidth(head)

                if (width <= limit)
                {
                    size.width = max(size.width, width)

                    textLines.add(TextLine(head, 0, size.height, width, height, false))

                    size.height += height

                    line = tail
                    width = this.stringWidth(line)
                    index = line.length - 1

                    if (size.height >= limitHeight)
                    {
                        break
                    }
                }
                else
                {
                    index--
                }
            }

            if (size.height >= limitHeight)
            {
                break
            }

            size.width = max(size.width, width)

            textLines.add(TextLine(line!!, 0, size.height, width, height, true))

            size.height += height

            if (size.height >= limitHeight)
            {
                break
            }

            line = lines.next()
        }

        for (textLine in textLines)
        {
            when (textAlign)
            {
                TextAlignment.CENTER -> textLine.x = (size.width - textLine.width) shr 1
                TextAlignment.LEFT -> textLine.x = 0
                TextAlignment.RIGHT -> textLine.x = size.width - textLine.width
            }
        }

        size.width = max(1, size.width)
        size.height = max(1, size.height)
        textLines.trimToSize()
        return TextParagraph(Collections.unmodifiableList(textLines), size)
    }

    /**
     * Computes the size of a string.
     *
     * **Usage example:**
     * ```kotlin
     * val size = font.stringSize("Hello, world!")
     * ```
     *
     * @param string The string to measure.
     * @return The size of the string.
     */
    fun stringSize(string: String): Dimension
    {
        val bounds = this.font.getStringBounds(string, FONT_RENDER_CONTEXT)
        return Dimension(ceil(bounds.width).toInt(), ceil(bounds.height).toInt())
    }

    /**
     * Computes the width of a string.
     *
     * **Usage example:**
     * ```kotlin
     * val width = font.stringWidth("Hello, world!")
     * ```
     *
     * @param string The string to measure.
     * @return The width of the string.
     */
    fun stringWidth(string: String): Int
    {
        val bounds = this.font.getStringBounds(string, FONT_RENDER_CONTEXT)
        return ceil(bounds.width + 1.0).toInt()
    }

    /**
     * Computes the underline position.
     *
     * @param string The string.
     * @param y The y coordinate of the top.
     * @return The y result.
     */
    fun underlinePosition(string: String, y: Int): Int
    {
        val lineMetrics = this.font.getLineMetrics(string, FONT_RENDER_CONTEXT)

        return round(y.toFloat() + lineMetrics.underlineOffset + lineMetrics.ascent).toInt()
    }

    /**
     * Returns the glyph vector for the given string.
     *
     * @param string The string.
     * @return The glyph vector.
     */
    fun glyphVector(string: String): GlyphVector =
        this.font.createGlyphVector(FONT_RENDER_CONTEXT, string)

    /**
     * Returns the shape of the given string.
     *
     * @param string The string.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The shape of the string.
     */
    fun shape(string: String, x: Int = 0, y: Int = 0): Shape =
        this.glyphVector(string)
            .getOutline(x.toFloat(), y + this.font.getLineMetrics(string, FONT_RENDER_CONTEXT).ascent)

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param other The reference object with which to compare.
     * @return `true` if this object is the same as the obj argument; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is JHelpFont)
        {
            return false
        }

        return this.underline == other.underline && this.font == other.font
    }
}