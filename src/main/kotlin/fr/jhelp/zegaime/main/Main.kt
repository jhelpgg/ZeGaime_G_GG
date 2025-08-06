package fr.jhelp.zegaime.main

import fr.khelp.zegaime.engine3d.gui.component.GUIComponentEmpty
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.constraintLayout
import fr.khelp.zegaime.engine3d.window3DFull
import fr.khelp.zegaime.resources.CANCEL
import fr.khelp.zegaime.resources.OK
import fr.khelp.zegaime.resources.ResourcesText

/**
 * Main function
 */
fun main()
{
    window3DFull("Test") {
        gui.constraintLayout {
            val helloText = GUIComponentText()
            helloText.keyText = ResourcesText.standardTextKey("Click on OK !")

            val buttonOk = buttonText(OK)
            buttonOk.click = { helloText.keyText = ResourcesText.standardTextKey("Ok clicked !") }

            val buttonCancel = buttonText(CANCEL)
            buttonCancel.click = { gui.visible = false }

            val buttonSeparator = GUIComponentEmpty(1)

            helloText.with {
                horizontalWrapped
                verticalWrapped

                topAtParent
                bottomAtTopOf(buttonOk)
                leftAtParent
                rightAtParent
            }

            buttonSeparator.with {
                horizontalWrapped
                verticalWrapped

                topFree
                bottomAtParent
                leftAtParent
                rightAtParent
            }

            buttonOk.with {
                horizontalWrapped
                verticalWrapped

                topFree
                bottomAtParent
                leftAtParent
                rightAtLeftOf(buttonSeparator)
            }

            buttonCancel.with {
                horizontalWrapped
                verticalWrapped

                topFree
                bottomAtParent
                leftAtRightOf(buttonSeparator)
                rightAtParent
            }
        }
    }
}
