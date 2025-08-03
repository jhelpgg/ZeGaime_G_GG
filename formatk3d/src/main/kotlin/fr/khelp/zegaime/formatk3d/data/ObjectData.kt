package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

/**
 * Object data for save
 * @param mesh Object mesh
 * @param showWire Indicates if wire frame is shown
 * @param wireColor Wire frame color
 */
@Serializable
data class ObjectData(val mesh: MeshData, val showWire: Boolean, val wireColor : ColorData)
