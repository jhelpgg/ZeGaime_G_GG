package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

@Serializable
data class RobotData(val head : HeadData, val materialBody : String,
                     val robotPosition : RobotPositionData,
                     val rightArmColor : ColorData, val leftArmColor : ColorData,
                     val rightLegColor : ColorData, val leftLegColor : ColorData)
