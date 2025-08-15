package fr.khelp.zegaime.engine3d.gui.layout

import fr.khelp.zegaime.engine3d.events.MouseState
import fr.khelp.zegaime.engine3d.events.MouseStatus
import fr.khelp.zegaime.engine3d.gui.component.GUIComponent
import fr.khelp.zegaime.utils.collections.iterations.transform
import fr.khelp.zegaime.utils.collections.lists.inverted
import java.awt.Dimension

/**
 * Generic GUI layout
 */
abstract class GUILayout<C : GUIConstraints>
{
    /** Number of components */
    internal val size : Int get() = this.components.size
    private val components = ArrayList<Pair<GUIComponent, C>>()
    private var previousComponent : GUIComponent? = null

    /**
     * Adds a component
     *
     * @param component Component to add
     * @param constraint Constraints associated to the component
     */
    fun add(component : GUIComponent, constraint : C)
    {
        synchronized(this.components)
        {
            this.components.add(Pair<GUIComponent, C>(component, constraint))
        }
    }

    /**
     * Gets a component
     *
     * @param index Component's index
     *
     * @return The component
     */
    internal operator fun get(index : Int) : GUIComponent =
        synchronized(this.components)
        {
            this.components[index].first
        }

    /**
     * Removes all components
     */
    internal fun clear()
    {
        synchronized(this.components)
        {
            this.components.clear()
            this.previousComponent = null
        }
    }

    /**
     * Removes one component
     *
     * @param index Component's index
     */
    internal fun remove(index : Int)
    {
        synchronized(this.components)
        {
            if (this.previousComponent == this.components.removeAt(index).first)
            {
                this.previousComponent = null
            }
        }
    }

    /**
     * Gets iterator on components
     *
     * @return Components' iterator
     */
    internal fun components() : Iterable<GUIComponent> =
        synchronized(this.components)
        {
            this.components.transform { (component, _) -> component }
        }

    /**
     * Called when mose sate changed
     *
     * @param mouseState New mouse state
     *
     * @return Whether the event is consumed
     */
    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        if (mouseState.mouseStatus == MouseStatus.OUTSIDE)
        {
            return false
        }

        val x = mouseState.x
        val y = mouseState.y

        synchronized(this.components)
        {
            for ((component, _) in this.components.inverted())
            {
                if (component.visible && component.contains(x, y))
                {
                    if (this.previousComponent != component)
                    {
                        this.previousComponent?.let { previous ->
                            previous.mouseState(MouseState(MouseStatus.EXIT,
                                                           mouseState.x - previous.x,
                                                           mouseState.y - previous.y,
                                                           mouseState.leftButtonDown,
                                                           mouseState.middleButtonDown,
                                                           mouseState.rightButtonDown,
                                                           mouseState.shiftDown,
                                                           mouseState.controlDown,
                                                           mouseState.altDown,
                                                           mouseState.clicked))
                        }

                        component.mouseState(MouseState(MouseStatus.ENTER,
                                                        mouseState.x - component.x,
                                                        mouseState.y - component.y,
                                                        mouseState.leftButtonDown,
                                                        mouseState.middleButtonDown,
                                                        mouseState.rightButtonDown,
                                                        mouseState.shiftDown,
                                                        mouseState.controlDown,
                                                        mouseState.altDown,
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
        }

        return false
    }

    /**
     * Layouts components
     *
     * @param parentWidth Parent width
     * @param parentHeight Parent height
     */
    internal fun layout(parentWidth : Int, parentHeight : Int)
    {
        synchronized(this.components)
        {
            this.layout(parentWidth, parentHeight, this.components)
        }
    }

    /**
     * Computes content preferred size
     *
     * @return Content preferred size
     */
    internal fun preferredSize() : Dimension =
        synchronized(this.components)
        {
            this.preferredSize(this.components)
        }

    protected abstract fun layout(parentWidth : Int, parentHeight : Int, components : List<Pair<GUIComponent, C>>)

    protected abstract fun preferredSize(components : List<Pair<GUIComponent, C>>) : Dimension
}
