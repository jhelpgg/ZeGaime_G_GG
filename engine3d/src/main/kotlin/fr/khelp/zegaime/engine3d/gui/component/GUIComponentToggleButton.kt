package fr.khelp.zegaime.engine3d.gui.component

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.events.MouseStatus
import fr.khelp.zegaime.engine3d.gui.GUIMargin
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class GUIComponentToggleButton(private val normal : GUIComponent,
                               private val over : GUIComponent,
                               private val down : GUIComponent,
                               private val disabled : GUIComponent) : GUIComponent()
{
    private val selectedObservableData = ObservableSource<Boolean>(false)
    val selected : Observable<Boolean> = this.selectedObservableData.observable
    var enabled : Boolean = true

    private var isOver = false
    private var mayDeselect = false

    fun select(select : Boolean)
    {
        if (this.enabled)
        {
            this.mayDeselect = false
            this.selectedObservableData.setValueIf(select) { value -> value != select }
        }
    }

    override fun drawIntern(graphics2D : Graphics2D, margin : GUIMargin)
    {
        val component =
            when
            {
                !this.enabled                     -> this.disabled
                this.selectedObservableData.value -> this.down
                this.isOver                       -> this.over
                else                              -> this.normal
            }

        component.margin = this.margin
        component.x = 0
        component.y = 0
        component.width = this.width
        component.height = this.height
        component.draw(graphics2D)
    }

    override fun preferredSize(margin : GUIMargin) : Dimension
    {
        var width = 16
        var height = 16

        var preferred = this.normal.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.over.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.down.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        preferred = this.disabled.preferredSize()
        width = max(width, preferred.width)
        height = max(height, preferred.height)

        return Dimension(width + margin.width, height + margin.height)
    }

    override fun mouseState(mouseState : MouseState) : Boolean
    {
        if (this.enabled)
        {
            when
            {
                mouseState.mouseStatus == MouseStatus.ENTER ->
                    if (!this.isOver)
                    {
                        this.isOver = true
                        this.mayDeselect = false
                    }

                mouseState.mouseStatus == MouseStatus.EXIT  ->
                    if (this.isOver)
                    {
                        this.isOver = false
                        this.mayDeselect = false
                    }

                mouseState.leftButtonDown                   ->
                    if (this.selectedObservableData.value)
                    {
                        this.mayDeselect = true
                    }
                    else
                    {
                        this.selectedObservableData.value = true
                    }

                !mouseState.leftButtonDown                  ->
                    if (this.mayDeselect)
                    {
                        this.mayDeselect = false
                        this.selectedObservableData.value = false
                    }
            }

            return true
        }

        return false
    }
}
