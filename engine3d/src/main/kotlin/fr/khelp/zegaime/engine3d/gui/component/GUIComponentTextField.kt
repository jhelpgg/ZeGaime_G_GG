package fr.khelp.zegaime.engine3d.gui.component

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.gui.GUIMargin
import fr.khelp.zegaime.engine3d.gui.focus.Key
import fr.khelp.zegaime.engine3d.gui.focus.KeyFocusManager
import fr.khelp.zegaime.engine3d.gui.focus.KeyFocusable
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.GRAY
import fr.khelp.zegaime.images.color.WHITE
import fr.khelp.zegaime.images.color.alpha
import fr.khelp.zegaime.images.drawText
import fr.khelp.zegaime.images.font.DEFAULT_FONT
import fr.khelp.zegaime.images.font.JHelpFont
import fr.khelp.zegaime.images.setColor
import fr.khelp.zegaime.resources.HINT_TYPE_TEXT
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.resources.defaultTexts
import fr.khelp.zegaime.utils.argumentCheck
import fr.khelp.zegaime.utils.injection.injected
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import fr.khelp.zegaime.utils.tasks.synchro.Mutex
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.min

/**
 * Component Text fiels to permit user to type some text
 *
 * @property numberCharacterShow Number characters that the text filed will show
 */
class GUIComponentTextField(val numberCharacterShow : Int = 42) : GUIComponent(),
                                                                  KeyFocusable
{
    /** Current text typed string buffer */
    private val typedText = StringBuffer()

    /** Current text typed */
    var actualTypedText
        get() = this.typedText.toString()
        set(value)
        {
            this.typedText.delete(0, this.typedText.length)
            this.typedText.append(value)
            this.cursorPosition = min(this.cursorPosition, value.length)
            this.typedTextSource.value = value
        }

    /** Key text code for the hint to show */
    var keyHintText = HINT_TYPE_TEXT

    /** Text resources used to resolve [keyHintText] */
    var resourcesHintText : ResourcesText = defaultTexts

    /** Text font to use */
    var font : JHelpFont = DEFAULT_FONT
        set(value)
        {
            field = value
            this.updateMaximumWidth()
        }

    /** Text cursor color */
    var cursorColor : Color = WHITE

    /** Text main color */
    var textColorMain : Color = WHITE

    /** Text border color */
    var textColorBorder : Color = BLACK

    /** Hint text color */
    var textHintColorMain : Color = GRAY

    /** Number of spaces in tab */
    var tabSize = 3
        set(value)
        {
            field = value.coerceIn(2, 5)
        }

    /** Text typed observable source */
    private val typedTextSource = ObservableSource<String>("")

    /** Observable on the text typed changed */
    val typedTextObservable : Observable<String> = this.typedTextSource.observable

    /** Text maximum width */
    private var maximumWidth : Int = 0

    /** Current cursor position */
    private var cursorPosition = 0

    /** Key focus manager */
    private val keyFocusManager : KeyFocusManager by injected<KeyFocusManager>()

    /** Synchronization mutex */
    private val mutex = Mutex()

    /** Whether the component have the key focus */
    private var hasFocus = false

    init
    {
        argumentCheck(this.numberCharacterShow > 0) { "Must have at least one column, not ${this.numberCharacterShow}" }
        this.borderColor = WHITE
        this.background = StyleBackgroundColor(BLACK)
        this.updateMaximumWidth()
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val (text, color) =
            if (this.typedText.isEmpty())
            {
                this.resourcesHintText[this.keyHintText] to this.textHintColorMain
            }
            else
            {
                this.actualTypedText to this.textColorMain
            }

        val x = margin.left + 8
        val y = (this.height - this.font.fontHeight) / 2
        graphics2D.font = this.font.font

        if (this.textColorBorder.alpha > 0)
        {
            graphics2D.setColor(this.textColorBorder)

            for (yy in -1..1)
            {
                for (xx in -1..1)
                {
                    graphics2D.drawText(xx + x, yy + y, text)
                }
            }
        }

        graphics2D.setColor(color)
        graphics2D.drawText(x, y, text)

        if (this.hasFocus)
        {
            val xCursor =
                when
                {
                    this.cursorPosition == 0 || this.actualTypedText.isEmpty() ->
                        0

                    this.cursorPosition >= this.actualTypedText.length         ->
                        this.font.stringWidth(this.actualTypedText)

                    else                                                       ->
                        this.font.stringWidth(this.actualTypedText.substring(0, this.cursorPosition))
                }

            graphics2D.setColor(this.cursorColor)
            graphics2D.fillRect(x + xCursor, y, 3, this.font.fontHeight)
        }
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        val more = if (this.textColorBorder.alpha == 0) 0 else 2
        val shapeMargin = this.shape.margin(0, 0,
                                            more + this.maximumWidth,
                                            more + this.font.fontHeight)
        return Dimension(margin.width + more + this.maximumWidth + shapeMargin.left + shapeMargin.right + 16,
                         margin.height + more + this.font.fontHeight + shapeMargin.top + shapeMargin.bottom + 16)
    }

    override fun receiveCharacter(character : Char)
    {
        this.mutex { this.unsafeReceiveCharacter(character) }
    }

    override fun receiveKey(key : Key)
    {
        this.mutex {
            when (key)
            {
                Key.BACKSPACE ->
                    if (this.cursorPosition > 0)
                    {
                        this.typedText.deleteCharAt(this.cursorPosition - 1)
                        this.cursorPosition--
                        this.typedTextSource.value = this.actualTypedText
                    }

                Key.DELETE    ->
                    if (this.cursorPosition < this.typedText.length)
                    {
                        this.typedText.deleteCharAt(this.cursorPosition)
                        this.typedTextSource.value = this.actualTypedText
                    }

                Key.END       ->
                    this.cursorPosition = this.typedText.length

                Key.HOME      ->
                    this.cursorPosition = 0

                Key.LEFT      ->
                    if (this.cursorPosition > 0)
                    {
                        this.cursorPosition--
                    }

                Key.RIGHT     ->
                    if (this.cursorPosition < this.typedText.length)
                    {
                        this.cursorPosition++
                    }

                Key.TAB       ->
                    for (count in 0 until this.tabSize)
                    {
                        this.unsafeReceiveCharacter(' ')
                    }

                else          ->
                    Unit
            }
        }
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        if (mouseState.leftButtonDown)
        {
            this.mutex {
                val text = this.actualTypedText

                if (text.isEmpty())
                {
                    this.cursorPosition = 0
                }
                else
                {
                    var position = 0
                    val x = mouseState.x

                    for (index in 1..text.length)
                    {
                        val width = this.font.stringWidth(text.substring(0, index))

                        if (x < width)
                        {
                            break
                        }

                        position = index
                    }

                    this.cursorPosition = position
                }
            }

            this.hasFocus = true
            this.keyFocusManager.gainFocus(this)
            return true
        }

        return super.mouseState(mouseState)
    }

    override fun onFocusLost()
    {
        this.hasFocus = false
    }

    private fun updateMaximumWidth()
    {
        this.maximumWidth = this.font.maximumCharacterWidth * this.numberCharacterShow
    }

    private fun unsafeReceiveCharacter(character : Char)
    {
        if (this.cursorPosition >= this.typedText.length)
        {
            this.typedText.append(character)
        }
        else
        {
            this.typedText.insert(this.cursorPosition, character)
        }

        this.cursorPosition++
        this.typedTextSource.value = this.actualTypedText
    }
}