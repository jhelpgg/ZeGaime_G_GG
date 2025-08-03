package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Dice data for save
 * @param color Dice color
 */
@Serializable
data class DiceData(val color : ColorData)
