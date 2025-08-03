package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

@Serializable
data class RevolutionData(val precision : Int,
                          val angle : Float, val rotationPrecision : Int,
                          val start : Float, val end : Float,
                          val multiplierU : Float,
                          val path : PathData)
