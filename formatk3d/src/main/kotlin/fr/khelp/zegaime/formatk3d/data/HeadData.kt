package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Head
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.images.color.argb
import kotlinx.serialization.Serializable

@Serializable
data class HeadData(val leftEye : Eye, val rightEye : Eye, val mouth : Mouth, val hairColor : Int)
{
    constructor(head : Head) : this(leftEye = head.leftEye,
                                    rightEye = head.rightEye,
                                    mouth = head.mouth,
                                    hairColor = head.hairColor.argb)

    constructor(robot : Robot) : this(head = robot.head)

    fun fillHead(head : Head)
    {
        head.leftEye = this.leftEye
        head.rightEye = this.rightEye
        head.mouth = this.mouth
        head.hairColor = this.hairColor.argb
    }

    fun fillHead(robot : Robot)
    {
        this.fillHead(robot.head)
    }
}
