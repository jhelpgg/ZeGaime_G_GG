package fr.khelp.zegaime.formatk3d.creator

import fr.khelp.zegaime.engine3d.resources.Eye
import fr.khelp.zegaime.engine3d.resources.Mouth
import fr.khelp.zegaime.formatk3d.data.HeadData
import fr.khelp.zegaime.images.color.Color
import fr.khelp.zegaime.images.color.argb

class HeadCreator
{
    var leftEye : Eye = Eye.GREEN_2
    var rightEye : Eye = Eye.GREEN_2
    var mouth : Mouth = Mouth.SMILE_2
    var hairColor : Color = 0xFFA0661C.toInt().argb

    val headData : HeadData
        get() = HeadData(leftEye = this.leftEye, rightEye = this.rightEye,
                         mouth = this.mouth,
                         hairColor = this.hairColor.argb)
}