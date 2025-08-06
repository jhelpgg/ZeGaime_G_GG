package fr.khelp.zegaime.engine3d.gui.layout

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.events.MouseStatus
import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.utils.collections.iterations.transform
import fr.khelp.zegaime.utils.collections.lists.inverted
import java.awt.Dimension

abstract class GUILayout<C : GUIConstraints>
{
    private val components = ArrayList<Pair<GUIComponent, C>>()
    private var previousComponent : GUIComponent? = null

    fun add(component : GUIComponent, constraint : C)
    {
        this.components.add(Pair<GUIComponent, C>(component, constraint))
    }

    internal fun components() : Iterable<GUIComponent> =
        this.components.transform { (component, _) -> component }

    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        val x = mouseState.x
        val y = mouseState.y

        for ((component, _) in this.components.inverted())
        {
            if (component.visible && component.contains(x, y))
            {
                if (this.previousComponent != component)
                {
                    this.previousComponent?.let { previous ->
                        previous.mouseState(MouseState(MouseStatus.EXIT,
                                                       mouseState.x - previous.x, mouseState.y - previous.y,
                                                       mouseState.leftButtonDown, mouseState.middleButtonDown,
                                                       mouseState.rightButtonDown,
                                                       mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                                       mouseState.clicked))
                    }

                    component.mouseState(MouseState(MouseStatus.ENTER,
                                                    mouseState.x - component.x, mouseState.y - component.y,
                                                    mouseState.leftButtonDown, mouseState.middleButtonDown,
                                                    mouseState.rightButtonDown,
                                                    mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                                    mouseState.clicked))
                }

                this.previousComponent = component

                // Enter and exit are treated in another way, so we avoid double report that may lead issue
                if (mouseState.mouseStatus != MouseStatus.ENTER && mouseState.mouseStatus != MouseStatus.EXIT)
                {
                    val state = MouseState(mouseState.mouseStatus,
                                           mouseState.x - component.x, mouseState.y - component.y,
                                           mouseState.leftButtonDown, mouseState.middleButtonDown,
                                           mouseState.rightButtonDown,
                                           mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                           mouseState.clicked)

                    if (component.mouseState(state))
                    {
                        return true
                    }
                }

                if (mouseState.mouseStatus == MouseStatus.ENTER)
                {
                    return true
                }
            }
        }

        this.previousComponent?.let { previous ->
            previous.mouseState(MouseState(MouseStatus.EXIT,
                                           mouseState.x - previous.x, mouseState.y - previous.y,
                                           mouseState.leftButtonDown, mouseState.middleButtonDown,
                                           mouseState.rightButtonDown,
                                           mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                           mouseState.clicked))
        }

        this.previousComponent = null
        return false
    }

    internal fun layout(parentWidth : Int, parentHeight : Int)
    {
        this.layout(parentWidth, parentHeight, this.components)
    }

    internal fun preferredSize() : Dimension =
        this.preferredSize(this.components)

    protected abstract fun layout(parentWidth : Int, parentHeight : Int, components : List<Pair<GUIComponent, C>>)

    protected abstract fun preferredSize(components : List<Pair<GUIComponent, C>>) : Dimension
}
