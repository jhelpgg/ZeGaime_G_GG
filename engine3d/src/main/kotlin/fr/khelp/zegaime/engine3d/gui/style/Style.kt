package fr.khelp.zegaime.engine3d.gui.style

import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackground
import fr.khelp.zegaime.engine3d.gui.style.background.StyleBackgroundTransparent
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShape
import fr.khelp.zegaime.engine3d.gui.style.shape.StyleShapeRectangle
import fr.khelp.zegaime.images.color.BLACK
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.utils.tasks.observable.Observable
import fr.khelp.zegaime.utils.tasks.observable.ObservableSource

open class Style
{
    private val changeObservableSource : ObservableSource<Style> by lazy { ObservableSource<Style>(this) }
    val changeStyleObservable : Observable<Style> by lazy { this.changeObservableSource.observable }

    var borderColor : Color = BLACK
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var shape : StyleShape = StyleShapeRectangle
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var background : StyleBackground = StyleBackgroundTransparent
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    var componentHighLevel : ComponentHighLevel = ComponentHighLevel.AT_GROUND
        set(value)
        {
            if (field != value)
            {
                field = value
                this.signalChange()
            }
        }

    protected fun signalChange()
    {
        this.changeObservableSource.value = this
    }
}