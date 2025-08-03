package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Plane data for save
 * @param faceUV Plane UV
 */
@Serializable
data class PlaneData(val faceUV : FaceUVData)
