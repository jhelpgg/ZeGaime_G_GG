package fr.khelp.zegaime.engine3d.gui.dsl

import fr.khelp.zegaime.engine3d.events.GenericAction
import fr.khelp.zegaime.engine3d.gui.menu.GUIMenu
import fr.khelp.zegaime.utils.tasks.TaskContext

class GUIMenuCreator internal constructor(private val menu : GUIMenu)
{
    operator fun GenericAction.unaryPlus()
    {
        this@GUIMenuCreator.menu.add(this)
    }

    fun action(keyText : String, action : () -> Unit)
    {
        this.menu.add(GenericAction(this.menu.resourcesText, keyText, TaskContext.INDEPENDENT, action))
    }

    fun action(keyText : String, imagePath : String, action : () -> Unit)
    {
        this.menu.add(GenericAction(this.menu.resourcesText, keyText,
                                    this.menu.resourcesText.resources.image(imagePath),
                                    TaskContext.INDEPENDENT, action))
    }
}
