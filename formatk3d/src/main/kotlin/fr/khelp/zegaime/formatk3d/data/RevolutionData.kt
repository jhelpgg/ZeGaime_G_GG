package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Revolution data for save
 * @param precision Revolution precision
 * @param angle Revolution angle
 * @param rotationPrecision Revolution rotation precision
 * @param start Revolution start position in path
 * @param end Revolution end position in path
 * @param multiplierU Revolution texture U multiplier
 * @param path Path to follow for revolution
 */
@Serializable
data class RevolutionData(val precision : Int,
                          val angle : Float, val rotationPrecision : Int,
                          val start : Float, val end : Float,
                          val multiplierU : Float,
                          val path : PathData)
