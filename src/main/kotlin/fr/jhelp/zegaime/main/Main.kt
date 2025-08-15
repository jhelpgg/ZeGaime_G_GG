package fr.jhelp.zegaime.main

import fr.khelp.zegaime.engine3d.gui.component.GUIComponentEmpty
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentImage
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentTextField
import fr.khelp.zegaime.engine3d.gui.component.ImageConstraint
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.constraintLayout
import fr.khelp.zegaime.engine3d.gui.dsl.scrollVertical
import fr.khelp.zegaime.engine3d.window3DFull
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.resources.CANCEL
import fr.khelp.zegaime.resources.OK
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.utils.logs.debug
import fr.khelp.zegaime.utils.tasks.delay
import java.io.File
import java.io.FileInputStream

/**
 * Main function
 */
fun main()
{
    window3DFull("Test") {
        val helloText = GUIComponentText()
        helloText.keyText = OK

        val textField = GUIComponentTextField()

        val file = File("C:\\Users\\jhelp\\Pictures\\977253.jpg")
        val image = GUIComponentImage(image = GameImage.load(FileInputStream(file)),
                                      imageConstraint = ImageConstraint.FIT)
        val scroll = scrollVertical {
            image.center
        }

        val buttonOk = buttonText(OK)
        buttonOk.click = { helloText.keyText = ResourcesText.standardTextKey("Ok clicked !")
            this.gui.dialogFileChooser.onSelectFile = { file -> debug("Select : ", file.absolutePath) }
            this.gui.dialogFileChooser.show()
        }

        val buttonCancel = buttonText(CANCEL)
        buttonCancel.click = {
            gui.visible = false
            delay(4096L) { closeWindow() }
        }

        val buttonSeparator = GUIComponentEmpty(1)

        gui.constraintLayout {
            helloText.with {
                horizontalWrapped
                verticalWrapped

                topAtParent
                bottomFree
                leftAtParent
                rightAtParent
            }

            textField.with {
                horizontalWrapped
                verticalWrapped

                topAtBottomOf(helloText)
                bottomFree
                leftAtParent
                rightAtParent
            }

            scroll.with {
                horizontalExpanded
                verticalExpanded

                topAtBottomOf(textField)
                bottomAtTopOf(buttonOk)
                leftAtLeftOf(textField)
                rightAtRightOf(textField)
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
