package fr.khelp.zegaime.engine3d.gui.component

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.engine3d.gui.GUIMargin
import fr.khelp.zegaime.engine3d.gui.layout.GUIConstraints
import fr.khelp.zegaime.engine3d.gui.layout.GUILayout
import fr.khelp.zegaime.engine3d.gui.style.ComponentHighLevel
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackground
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundColor
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShape
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShapeRoundRectangle
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.base.Green
import fr.khelp.zegaime.images.color.semiVisible
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

class GUIDialog<C : GUIConstraints, L : GUILayout<C>> internal constructor(internal val panel : GUIComponentPanel<C, L>,
                                                                           private val gui : GUI)
{
    private val showObservableData = ObservableSource<Boolean>(false)
    val showing = this.showObservableData.observable
    var margin : GUIMargin
        get() = this.panel.margin
        set(value)
        {
            this.panel.margin = value
        }

    var borderColor : Color
        get() = this.panel.borderColor
        set(value)
        {
            this.panel.borderColor = value
        }

    var shape : StyleShape
        get() = this.panel.shape
        set(value)
        {
            this.panel.shape = value
        }

    var background : StyleBackground
        get() = this.panel.background
        set(value)
        {
            this.panel.background = value
        }

    init
    {
        this.panel.shape = StyleShapeRoundRectangle
        this.panel.background = StyleBackgroundColor(Green.GREEN_0300.color.semiVisible)
        this.panel.borderColor = Green.GREEN_0050.color
        this.panel.margin = GUIMargin(8, 8, 8, 8)
        this.panel.componentHighLevel = ComponentHighLevel.NEAR_GROUND
    }

    fun show()
    {
        if (this.showObservableData.setValueIf(value = true) { value -> !value })
        {
            this.gui.show(this)
        }
    }

    fun close()
    {
        if (this.showObservableData.setValueIf(value = false) { value -> value })
        {
            this.gui.hide(this)
        }
    }
}
