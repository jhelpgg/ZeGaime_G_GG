package fr.khelp.zegaime.formatk3d.data

import kotlinx.serialization.Serializable

@Serializable
data class ObjectData(val mesh: MeshData, val showWire: Boolean, val wireColor : ColorData)
