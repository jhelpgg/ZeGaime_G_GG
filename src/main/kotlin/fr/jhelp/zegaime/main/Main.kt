package fr.jhelp.zegaime.main

import fr.khelp.zegaime.engine3d.gui.component.GUIComponentEmpty
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentImage
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.component.ImageConstraint
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.constraintLayout
import fr.khelp.zegaime.engine3d.gui.dsl.scrollVertical
import fr.khelp.zegaime.engine3d.window3DFull
import fr.khelp.zegaime.images.GameImage
import fr.khelp.zegaime.resources.CANCEL
import fr.khelp.zegaime.resources.OK
import fr.khelp.zegaime.resources.ResourcesText
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
        helloText.keyText = ResourcesText.standardTextKey("""
                Click on Ok !
                Second line
                An other line just for add lines
                Whats upon a time, a line of text that's exists only to be there and do some test.
                I knock, knock to the imagination door, to find a text to write.
                Write a line or not write a line ? That is the question.
                Theres a famous theory, more line you right, more line you get.
                Hope this time is enough
                Its funny, but not look enough
                More and more
                Did you know it exists at least one space where PI is exactly 4 ?
                In a sphere you can draw a triangle with three right angles
            """.trimIndent())

        val file = File("C:\\Users\\jhelp\\Pictures\\977253.jpg")
        val image = GUIComponentImage(image = GameImage.load(FileInputStream(file)),
                                      imageConstraint = ImageConstraint.FIT)
        val scroll = scrollVertical {
            image.center
        }

        val buttonOk = buttonText(OK)
        buttonOk.click = { helloText.keyText = ResourcesText.standardTextKey("Ok clicked !") }

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

            scroll.with {
                horizontalExpanded
                verticalExpanded

                topAtBottomOf(helloText)
                bottomAtTopOf(buttonOk)
                leftAtLeftOf(helloText)
                rightAtRightOf(helloText)
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
