package fr.khelp.zegaime.engine3d.gui.dialogs

import fr.khelp.zegaime.engine3d.gui.GUI
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentButton
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentImage
import fr.khelp.zegaime.engine3d.gui.component.GUIComponentText
import fr.khelp.zegaime.engine3d.gui.component.GUIDialog
import fr.khelp.zegaime.engine3d.gui.dsl.buttonText
import fr.khelp.zegaime.engine3d.gui.dsl.dialogConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.GUIConstraintConstraint
import fr.khelp.zegaime.engine3d.gui.layout.constraint.GUIConstraintLayout
import fr.khelp.zegaime.engine3d.gui.message.MessageAction
import fr.khelp.zegaime.engine3d.gui.message.MessageButtons
import fr.khelp.zegaime.engine3d.gui.message.MessageType
import fr.khelp.zegaime.images.VerticalAlignment
import fr.khelp.zegaime.images.font.TITLE_FONT
import fr.khelp.zegaime.resources.CANCEL
import fr.khelp.zegaime.resources.NO
import fr.khelp.zegaime.resources.OK
import fr.khelp.zegaime.resources.ResourcesText
import fr.khelp.zegaime.resources.SAVE
import fr.khelp.zegaime.resources.SAVE_AS
import fr.khelp.zegaime.resources.YES
import fr.khelp.zegaime.resources.defaultTexts
import fr.khelp.zegaime.utils.tasks.future.Future
import fr.khelp.zegaime.utils.tasks.future.Promise

class DialogMessage internal constructor(gui : GUI)
{
    private val dialog : GUIDialog<GUIConstraintConstraint, GUIConstraintLayout>
    private val icon : GUIComponentImage = GUIComponentImage()
    private val message : GUIComponentText = GUIComponentText()
    private val okButton : GUIComponentButton = buttonText(OK, defaultTexts)
    private val cancelButton : GUIComponentButton = buttonText(CANCEL, defaultTexts)
    private val yesButton : GUIComponentButton = buttonText(YES, defaultTexts)
    private val noButton : GUIComponentButton = buttonText(NO, defaultTexts)
    private val saveButton : GUIComponentButton = buttonText(SAVE, defaultTexts)
    private val saveAsButton : GUIComponentButton = buttonText(SAVE_AS, defaultTexts)
    private lateinit var promiseMessageAction : Promise<MessageAction>

    init
    {
        this.message.font = TITLE_FONT
        this.message.verticalAlignment = VerticalAlignment.CENTER

        this.dialog = gui.dialogConstraint {
            this@DialogMessage.icon with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginLeft = 16
                this.marginTop = 16

                this.topAtParent
                this bottomAtTopOf this@DialogMessage.okButton
                this.leftAtParent
                this.rightFree
            }

            this@DialogMessage.message with {
                this.horizontalExpanded
                this.verticalWrapped

                this.marginRight = 4
                this.marginTop = 16

                this.topAtParent
                this bottomAtTopOf this@DialogMessage.okButton
                this leftAtRightOf this@DialogMessage.icon
                this.rightAtParent
            }

            this@DialogMessage.okButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginLeft = 16
                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this.leftAtParent
                this.rightFree
            }

            this@DialogMessage.yesButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginLeft = 16
                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this.leftAtParent
                this.rightFree
            }

            this@DialogMessage.noButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginRight = 32
                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this leftAtRightOf this@DialogMessage.yesButton
                this rightAtRightOf this@DialogMessage.cancelButton
            }

            this@DialogMessage.cancelButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginRight = 16
                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this.leftFree
                this.rightAtParent
            }

            this@DialogMessage.saveButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginRight = 32
                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this.leftAtParent
                this.rightFree
            }

            this@DialogMessage.saveAsButton with {
                this.horizontalWrapped
                this.verticalWrapped

                this.marginBottom = 4

                this.topFree
                this.bottomAtParent
                this leftAtRightOf this@DialogMessage.yesButton
                this rightAtRightOf this@DialogMessage.cancelButton
            }
        }

        this.okButton.click = {
            this.promiseMessageAction.result(MessageAction.OK)
            this.dialog.close()
        }
        this.cancelButton.click = {
            this.promiseMessageAction.result(MessageAction.CANCEL)
            this.dialog.close()
        }
        this.yesButton.click = {
            this.promiseMessageAction.result(MessageAction.YES)
            this.dialog.close()
        }
        this.noButton.click = {
            this.promiseMessageAction.result(MessageAction.NO)
            this.dialog.close()
        }
        this.saveButton.click = {
            this.promiseMessageAction.result(MessageAction.SAVE)
            this.dialog.close()
        }
        this.saveAsButton.click = {
            this.promiseMessageAction.result(MessageAction.SAVE_AS)
            this.dialog.close()
        }
    }

    fun show(messageType : MessageType, messageButtons : MessageButtons, keyText : String,
             resourcesText : ResourcesText = defaultTexts) : Future<MessageAction>
    {
        this.promiseMessageAction = Promise<MessageAction>()

        this.message.keyText = keyText
        this.message.resourcesText = resourcesText

        this.icon.image = messageType.icon()

        val actions = messageButtons.actions
        this.okButton.visible = MessageAction.OK in actions
        this.cancelButton.visible = MessageAction.CANCEL in actions
        this.yesButton.visible = MessageAction.YES in actions
        this.noButton.visible = MessageAction.NO in actions
        this.saveButton.visible = MessageAction.SAVE in actions
        this.saveAsButton.visible = MessageAction.SAVE_AS in actions

        this.dialog.show()

        return this.promiseMessageAction.future
    }
}
