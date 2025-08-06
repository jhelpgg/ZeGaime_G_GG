package fr.khelp.zegaime.engine3d.events

import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.images.toGameImage
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.utils.logs.verbose
import fr.khelp.zegaime.utils.tasks.TaskContext
import fr.khelp.zegaime.utils.tasks.parallel
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Icon
import javax.swing.KeyStroke

class GenericAction(private var resourcesText : ResourcesText,
                    private var keyName : String,
                    smallImage : GameImage?,
                    largeImage : GameImage?,
                    private var keyToolTip : String?,
                    private val taskContext : TaskContext,
                    private val actionClick : () -> Unit) : AbstractAction()
{
    constructor(resourcesText : ResourcesText,
                keyName : String,
                image : GameImage?,
                keyToolTip : String?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, image, image, keyToolTip, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                smallImage : GameImage?,
                largeImage : GameImage?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, smallImage, largeImage, null, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                image : GameImage?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, image, image, null, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                keyToolTip : String?,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, null, null, keyToolTip, taskContext, actionClick)

    constructor(resourcesText : ResourcesText,
                keyName : String,
                taskContext : TaskContext,
                actionClick : () -> Unit)
            : this(resourcesText, keyName, null, null, null, taskContext, actionClick)

    init
    {
        (smallImage ?: largeImage)?.let { image -> this.smallImage(image) }
        (largeImage ?: smallImage)?.let { image -> this.largeImage(image) }
        this.resourcesText.observableChange.register { this.updateTexts() }
    }

    fun keyName() : String = this.keyName

    fun keyName(keyName : String)
    {
        this.keyName = keyName
        this.putValue(NAME, keyName)
    }

    fun name() : String = this.getValue(NAME) as String

    fun keyToolTip() : String? = this.keyToolTip

    fun keyToolTip(keyToolTip : String)
    {
        this.keyToolTip = keyToolTip
        this.putValue(SHORT_DESCRIPTION, keyToolTip)
    }

    fun removeKeyToolTip()
    {
        this.keyToolTip = null
        this.putValue(SHORT_DESCRIPTION, null)
    }

    fun toolTip() : String? = this.getValue(SHORT_DESCRIPTION) as String?

    fun smallAndLargeImage(image : GameImage)
    {
        this.smallImage(image)
        this.largeImage(image)
    }

    fun removeImages()
    {
        this.removeSmallImage()
        this.removeLargeImage()
    }

    fun smallImage() : GameImage? = this.getValue(SMALL_ICON) as GameImage?

    fun smallImage(smallImage : GameImage)
    {
        this.putValue(SMALL_ICON, smallImage)
    }

    fun removeSmallImage()
    {
        this.putValue(SMALL_ICON, null)
    }

    fun largeImage() : GameImage? = this.getValue(LARGE_ICON_KEY) as GameImage?

    fun largeImage(largeImage : GameImage)
    {
        this.putValue(LARGE_ICON_KEY, largeImage)
    }

    fun removeLargeImage()
    {
        this.putValue(LARGE_ICON_KEY, null)
    }

    fun shortCut() : KeyStroke? = this.getValue(ACCELERATOR_KEY) as KeyStroke?

    fun shortCut(keyStroke : KeyStroke)
    {
        this.putValue(ACCELERATOR_KEY, keyStroke)
    }

    fun removeShortCut()
    {
        this.putValue(ACCELERATOR_KEY, null)
    }

    override fun actionPerformed(actionEvent : ActionEvent)
    {
        this.actionClick.parallel(this.taskContext)
    }

    override fun putValue(key : String, newValue : Any?)
    {
        var value : Any? = newValue

        when
        {
            NAME == key                                ->
                if (value == null)
                {
                    throw NullPointerException("Value for NAME must not be null")
                }
                else
                {
                    this.keyName = value.toString()
                    value = this.resourcesText[this.keyName]
                }
            SHORT_DESCRIPTION == key                   ->
                if (value != null)
                {
                    this.keyToolTip = value.toString()
                    value = this.resourcesText[this.keyToolTip !!]
                }
                else
                {
                    this.keyToolTip = null
                }
            SMALL_ICON == key || LARGE_ICON_KEY == key ->
                if (value != null)
                {
                    if (value !is Icon)
                    {
                        throw IllegalArgumentException(
                            "A ${Icon::class.java.name} or a ${GameImage::class.java.name} should be associate to the key $key, not a ${value.javaClass.name}")
                    }

                    value = value.toGameImage()
                }
            ACCELERATOR_KEY != key && "enabled" != key ->
            {
                verbose("The key $key is not managed here!")
                return
            }
        }

        super.putValue(key, value)
    }

    private fun updateTexts()
    {
        this.putValue(NAME, this.keyName)
        this.putValue(SHORT_DESCRIPTION, this.keyToolTip)
    }
}
