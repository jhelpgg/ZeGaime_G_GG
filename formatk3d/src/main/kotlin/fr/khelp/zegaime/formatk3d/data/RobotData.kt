package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Robot data for save
 * @param head Robot head
 * @param materialBody Robot body material name
 * @param robotPosition Robot position
 * @param rightArmColor Robot right arm color
 * @param leftArmColor Robot left arm color
 * @param rightLegColor Robot right leg color
 * @param leftLegColor Robot left leg color
 */
@Serializable
data class RobotData(val head : HeadData, val materialBody : String,
                     val robotPosition : RobotPositionData,
                     val rightArmColor : ColorData, val leftArmColor : ColorData,
                     val rightLegColor : ColorData, val leftLegColor : ColorData)
