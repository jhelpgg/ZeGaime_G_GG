package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.formatk3d.data.HeadData
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.argb

/**
 * Head creator for robot
 */
class HeadCreator
{
    /** Left eye texture */
    var leftEye : Eye = Eye.GREEN_2
    /** Right eye texture */
    var rightEye : Eye = Eye.GREEN_2
    /** Mouth texture */
    var mouth : Mouth = Mouth.SMILE_2
    /** Hair color */
    var hairColor : Color = 0xFFA0661C.toInt().argb

    /** Head data for save */
    val headData : HeadData
        get() = HeadData(leftEye = this.leftEye, rightEye = this.rightEye,
                         mouth = this.mouth,
                         hairColor = this.hairColor.argb)
}