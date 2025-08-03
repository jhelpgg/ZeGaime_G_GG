package fr.khelp.zegaime.formatk3d.data

import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Head
import fr.khelp.zegaime.engine3d.scene.prebuilt.complex.robot.Robot
import fr.khelp.zegaime.images.color.argb
import kotlinx.serialization.Serializable

/**
 * Head data for save
 * @param leftEye Left eye texture
 * @param rightEye Right eye texture
 * @param mouth Mouth texture
 * @param hairColor Hair color
 */
@Serializable
data class HeadData(val leftEye : Eye, val rightEye : Eye, val mouth : Mouth, val hairColor : Int)
{
    /**
     * Create a new instance of HeadData from a Head
     * @param head Head to copy
     */
    constructor(head : Head) : this(leftEye = head.leftEye,
                                    rightEye = head.rightEye,
                                    mouth = head.mouth,
                                    hairColor = head.hairColor.argb)

    /**
     * Create a new instance of HeadData from a Robot
     * @param robot Robot to copy head from
     */
    constructor(robot : Robot) : this(head = robot.head)

    /**
     * Fill a Head with this data
     * @param head Head to fill
     */
    fun fillHead(head : Head)
    {
        head.leftEye = this.leftEye
        head.rightEye = this.rightEye
        head.mouth = this.mouth
        head.hairColor = this.hairColor.argb
    }

    /**
     * Fill a Robot's head with this data
     * @param robot Robot to fill head
     */
    fun fillHead(robot : Robot)
    {
        this.fillHead(robot.head)
    }
}
