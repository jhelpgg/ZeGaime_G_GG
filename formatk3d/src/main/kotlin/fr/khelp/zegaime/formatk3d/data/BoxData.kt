package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Box data for save
 * @param boxUV Box UV
 */
@Serializable
data class BoxData(val boxUV: BoxUVData)
