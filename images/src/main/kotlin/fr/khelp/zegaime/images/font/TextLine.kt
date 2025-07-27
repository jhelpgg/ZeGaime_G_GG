package fr.khelp.zegaime.images.font

/**
 * Represents a line of text to be drawn.
 *
 * **Creation example:**
 * This class is not meant to be instantiated directly.
 * It is created by the `JHelpFont.computeTextParagraph` method.
 *
 * @property text The string represented by the given mask.
 * @property x The relative x position where to draw the part.
 * @property y The relative y position where to draw the part.
 * @property width The width of the part.
 * @property height The height of the part.
 * @property endOfLine Indicates if the part is the end of a line of the original complete text.
 * @constructor Creates a new text line.
 */
data class TextLine(val text : String,
                    var x : Int, val y : Int, val width : Int, val height : Int,
                    val endOfLine : Boolean)