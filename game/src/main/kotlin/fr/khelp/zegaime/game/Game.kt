package fr.khelp.zegaime.game

import fr.khelp.zegaime.engine3d.events.ActionCode
import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.engine3d.scene.Node
import fr.khelp.zegaime.engine3d.window3DFull
import fr.khelp.zegaime.game.characters.HairLong
import fr.khelp.zegaime.game.characters.HeadBase
import fr.khelp.zegaime.images.color.base.Brown
import fr.khelp.zegaime.images.color.base.Yellow

const val step = 10f

fun main()
{
    window3DFull("Test") {
        val node = Node("manip")
        val head = HeadBase("head")
        val hair = HairLong("hair")
        node.addChild(head)
        node.addChild(hair)
        scene.root.addChild(node)
        actionManager.actionCodes.register { actions ->
            if (ActionCode.ACTION_UP in actions)
            {
                node.angleX -= step
            }

            if (ActionCode.ACTION_DOWN in actions)
            {
                node.angleX += step
            }

            if (ActionCode.ACTION_LEFT in actions)
            {
                node.angleY -= step
            }

            if (ActionCode.ACTION_RIGHT in actions)
            {
                node.angleY += step
            }

            if (ActionCode.ACTION_BUTTON_1 in actions)
            {
                hair.color = Yellow.YELLOW_0500
            }

            if (ActionCode.ACTION_BUTTON_2 in actions)
            {
                head.skinColor = Brown.BROWN_0900.color
            }

            if (ActionCode.ACTION_BUTTON_3 in actions)
            {
                head.mouth = Mouth.SAD_3
            }

            if (ActionCode.ACTION_BUTTON_4 in actions)
            {
                head.rightEye = Eye.TONE_BLUE
            }

            if (ActionCode.ACTION_EXIT in actions)
            {
                closeWindow()
            }
        }
    }
}