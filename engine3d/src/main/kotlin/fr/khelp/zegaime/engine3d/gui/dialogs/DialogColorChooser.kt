package fr.khelp.zegaime.engine3d.gui.dialogs

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentEmpty
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentToggleButton
import fr.khelp.zegaime.engine3d.gui.component.GUIDialog
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.dialogConstraint
import fr.khelp.zegaime.engine3d.gui.dsl.panelAbsolute
import fr.khelp.zegaime.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.GUIConstraintConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.GUIConstraintLayout
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.images.color.base.Amber
import fr.khelp.zegaime.images.color.base.BaseColor
import fr.khelp.zegaime.images.color.base.Blue
import fr.khelp.zegaime.images.color.base.BlueGrey
import fr.khelp.zegaime.images.color.base.Brown
import fr.khelp.zegaime.images.color.base.Cyan
import fr.khelp.zegaime.images.color.base.DeepOrange
import fr.khelp.zegaime.images.color.base.DeepPurple
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.base.Grey
import fr.khelp.zegaime.images.color.base.Indigo
import fr.khelp.zegaime.images.color.base.LightBlue
import fr.khelp.zegaime.images.color.base.LightGreen
import fr.khelp.zegaime.images.color.base.Lime
import fr.khelp.zegaime.images.color.base.Orange
import fr.khelp.zegaime.images.color.base.Pink
import fr.khelp.zegaime.images.color.base.Purple
import fr.khelp.zegaime.images.color.base.Red
import fr.khelp.zegaime.images.color.base.Teal
import fr.khelp.zegaime.images.color.base.Yellow
import fr.khelp.zegaime.images.color.invert
import fr.khelp.zegaime.images.font.TITLE_FONT
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import java.util.concurrent.atomic.AtomicBoolean

class DialogColorChooser internal constructor(gui : GUI)
{
    companion object
    {
        private val order : Array<Class<out BaseColor<*>>> =
            arrayOf(
                Grey::class.java, Red::class.java, Green::class.java, Blue::class.java,
                Amber::class.java, BlueGrey::class.java, Brown::class.java, Cyan::class.java,
                DeepOrange::class.java, DeepPurple::class.java, Indigo::class.java, LightBlue::class.java,
                LightGreen::class.java, Lime::class.java, Orange::class.java, Pink::class.java,
                Purple::class.java, Teal::class.java, Yellow::class.java
                   )

        private val colors : List<List<BaseColor<*>>> by lazy {
            val columns = ArrayList<List<BaseColor<*>>>()

            for (colorClass in order)
            {
                val line = ArrayList<BaseColor<*>>()
                var color = colorClass.enumConstants[0].lightest
                line.add(color)

                while (color != color.darker)
                {
                    color = color.darker
                    line.add(color)
                }

                columns.add(line)
            }

            columns
        }

        private const val COLOR_SIZE = 48
    }

    private val dialog : GUIDialog<GUIConstraintConstraint, GUIConstraintLayout>
    private val colorObservableData : ObservableSource<BaseColor<*>> = ObservableSource<BaseColor<*>>(Grey.WHITE)
    private val colorComponents = ArrayList<Pair<GUIComponentToggleButton, BaseColor<*>>>()
    val color : Observable<BaseColor<*>> = this.colorObservableData.observable
    val showing : Observable<Boolean>

    init
    {
        val colorStart = this.colorObservableData.value
        val panel = panelAbsolute {
            var x = 0

            for (column in colors)
            {
                var y = 0

                for (baseColor in column)
                {
                    val emptyUp = GUIComponentEmpty(COLOR_SIZE)
                    emptyUp.background = StyleBackgroundColor(baseColor.color)
                    val emptyDown = GUIComponentEmpty(COLOR_SIZE)
                    emptyDown.background = StyleBackgroundColor(baseColor.color)
                    emptyDown.borderColor = baseColor.color.invert
                    val toggle = GUIComponentToggleButton(emptyUp, emptyUp, emptyDown, emptyDown)
                    toggle.select(baseColor == colorStart)
                    val firstTime = AtomicBoolean(true)
                    toggle.selected.register { selected ->
                        if (!firstTime.compareAndSet(true, false) && selected)
                        {
                            this@DialogColorChooser.select(baseColor)
                        }
                    }
                    this@DialogColorChooser.colorComponents.add(Pair(toggle, baseColor))

                    toggle with GUIAbsoluteConstraint(x, y,
                                                      COLOR_SIZE, COLOR_SIZE)

                    y += COLOR_SIZE
                }

                x += COLOR_SIZE
            }
        }

        val buttonOk = buttonText(keyText = "ok",
                                  font = TITLE_FONT)

        this.dialog = gui.dialogConstraint {
            panel with {
                this.horizontalWrapped
                this.verticalWrapped

                this.topAtParent
                this.bottomFree
                this.leftAtParent
                this.rightAtParent
            }

            buttonOk with {
                this.horizontalExpanded
                this.verticalWrapped

                this.marginTop = 8

                this topAtBottomOf panel
                this.bottomAtParent
                this.leftAtParent
                this.rightAtParent
            }
        }

        buttonOk.click = { this.dialog.close() }
        this.showing = this.dialog.showing
    }

    fun show()
    {
        this.dialog.show()
    }

    private fun select(color : BaseColor<*>)
    {
        if (this.colorObservableData.setValueIf(color) { value -> value != color })
        {
            for ((component, baseColor) in this.colorComponents)
            {
                component.select(color == baseColor)
            }
        }
    }
}
